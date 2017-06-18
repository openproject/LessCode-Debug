/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayfeng.lesscode.debug;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Copy from https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
 */
public final class DebugHttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    public interface Logger {
        void log(String message);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }
    public interface DebugHttpLoggingCallback {
        void onLog(boolean success, String log);
    }

    private StringBuilder logger;
    private volatile Level level = Level.NONE;
    private DebugHttpLoggingCallback debugHttpLoggingCallback;

    public DebugHttpLoggingCallback getDebugHttpLoggingCallback() {
        return debugHttpLoggingCallback;
    }

    public void setDebugHttpLoggingCallback(DebugHttpLoggingCallback debugHttpLoggingCallback) {
        this.debugHttpLoggingCallback = debugHttpLoggingCallback;
    }

    /**
     * Change the level at which this interceptor logs.
     */
    public DebugHttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        logger = new StringBuilder();

        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
            logger.append("\r\n");
        }
        logger.append(requestStartMessage);
        logger.append("\r\n");

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.append("Content-Type: " + requestBody.contentType());
                    logger.append("\r\n");
                }
                if (requestBody.contentLength() != -1) {
                    logger.append("Content-Length: " + requestBody.contentLength());
                    logger.append("\r\n");
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.append(name + ": " + headers.value(i));
                    logger.append("\r\n");
                }
            }

            if (!logBody || !hasRequestBody) {
                logger.append("--> END " + request.method());
                logger.append("\r\n");
            } else if (bodyEncoded(request.headers())) {
                logger.append("--> END " + request.method() + " (encoded body omitted)");
                logger.append("\r\n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logger.append("");
                if (isPlaintext(buffer)) {
                    logger.append(buffer.readString(charset));
                    logger.append("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                    logger.append("\r\n");
                } else {
                    logger.append("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)");
                    logger.append("\r\n");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.append("<-- HTTP FAILED: " + e);
            logger.append("\r\n");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.append("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');
        logger.append("\r\n");

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.append(headers.name(i) + ": " + headers.value(i));
                logger.append("\r\n");
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.append("<-- END HTTP");
                logger.append("\r\n");
            } else if (bodyEncoded(response.headers())) {
                logger.append("<-- END HTTP (encoded body omitted)");
                logger.append("\r\n");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logger.append("");
                    logger.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    logger.append("\r\n");
                    return response;
                }

                if (contentLength != 0) {
                    logger.append("\r\n");
                    // format body to json
                    String str = buffer.clone().readString(charset).trim();
                    String message = DebugUtils.formatBodyToJson(str);

                    logger.append(message);
                    logger.append("\r\n");
                }

                logger.append("<-- END HTTP (" + buffer.size() + "-byte body)");
                logger.append("\r\n");
            }
        }

        if (debugHttpLoggingCallback != null) {
            debugHttpLoggingCallback.onLog(response.isSuccessful(), logger.toString());
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}

package org.pomeluce.akir.common.core.http;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/5下午7:36
 * @className : HttpResult
 * @description : Http 请求结果对象
 */
public record HttpResult(
        int statusCode,
        String message,
        int series,
        String body,
        InputStream stream,
        Map<String, String> requestHeaders,
        Map<String, String> responseHeaders,
        Instant startTime,
        Instant endTime,
        Duration duration
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int statusCode;
        private String message;
        private int series;
        private String body;
        private InputStream stream;
        private Map<String, String> requestHeaders;
        private Map<String, String> responseHeaders;
        private Instant startTime;
        private Instant endTime;
        private Duration duration;

        public Builder statusCode(int code) {
            this.statusCode = code;
            return this;
        }

        public Builder message(String msg) {
            this.message = msg;
            return this;
        }

        public Builder series(int series) {
            this.series = series;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder stream(InputStream stream) {
            this.stream = stream;
            return this;
        }

        public Builder requestHeaders(Map<String, String> headers) {
            this.requestHeaders = headers;
            return this;
        }

        public Builder responseHeaders(Map<String, String> headers) {
            this.responseHeaders = headers;
            return this;
        }

        public Builder startTime(Instant start) {
            this.startTime = start;
            return this;
        }

        public Builder endTime(Instant end) {
            this.endTime = end;
            return this;
        }

        public Builder duration(Duration dur) {
            this.duration = dur;
            return this;
        }

        public HttpResult build() {
            return new HttpResult(statusCode, message, series, body, stream, requestHeaders, responseHeaders, startTime, endTime, duration);
        }
    }
}

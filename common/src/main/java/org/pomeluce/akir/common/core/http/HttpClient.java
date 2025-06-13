package org.pomeluce.akir.common.core.http;

import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.util.Timeout;
import org.pomeluce.akir.common.utils.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-07 15:55
 * @className : HttpClient
 * @description : HTTP 请求客户端对象
 */
public class HttpClient {
    private static final String CALLER_ID_HEADER = "X-Caller-ID";
    private final Executor executor;
    private final Config config;

    /**
     * Private constructor; use {@link #instance(Consumer)} to create an instance. * Initializes the underlying HttpClient and Fluent Executor.
     *
     * @param config configuration parameters for timeouts, headers, and credentials
     */
    private HttpClient(Config config) {
        RequestConfig baseReqCfg = RequestConfig.custom().setConnectionRequestTimeout(config.connectionRequestTimeout).setResponseTimeout(config.readTimeout).build();
        HttpClientBuilder clientBuilder = HttpClients.custom().setDefaultRequestConfig(baseReqCfg);
        if (config.credentialsProvider != null) clientBuilder.setDefaultCredentialsProvider(config.credentialsProvider);
        CloseableHttpClient client = clientBuilder.build();
        this.executor = Executor.newInstance(client);
        this.config = config;
    }

    /**
     * Determines if a ContentType represents text-like data for string conversion.
     *
     * @param ct the {@code ContentType} to check
     * @return {@code true} if JSON, XML, or text; {@code false} otherwise
     */
    private boolean isText(ContentType ct) {
        String mime = ct.getMimeType();
        return mime.startsWith("text/") || mime.contains("json") || mime.contains("xml");
    }

    /**
     * Creates an {@code HttpClient} instance using a lambda to configure options.
     *
     * @return a configured {@code HttpClient}
     */
    public static HttpClient instance() {
        return new HttpClient(new Config());
    }

    /**
     * Creates an {@code HttpClient} instance using a lambda to configure options.
     * <p>
     * Examples:
     * <pre><code>
     * HttpClient client = HttpClient.instance(cfg -> {
     *     cfg.baseUrl("https://api.example.com")
     *        .header("Accept", "application/json");
     * });
     * </code></pre>
     *
     * @param configurer lambda receiving a {@link Config} to set options
     * @return a configured {@code HttpClient}
     */
    public static HttpClient instance(Consumer<Config> configurer) {
        Config config = new Config();
        configurer.accept(config);
        return new HttpClient(config);
    }

    /**
     * Sends a request configured via a lambda that transforms a {@link RequestBuilder}.
     * <p>
     * Example:
     * <pre><code>
     * HttpResult result = client.request(rb ->
     *     rb.method("POST", "/upload")
     *       .binaryBody(fileBytes, "application/octet-stream")
     *       .header("X-Trace", "abc123")
     * );
     * </code></pre>
     *
     * @param builderFunction function that configures and returns a {@code RequestBuilder}
     * @return the {@link HttpResult} containing status, headers, and body
     */
    public HttpResult request(Function<RequestBuilder, RequestBuilder> builderFunction) {
        return request(builderFunction.apply(new RequestBuilder()));
    }

    /**
     * Asynchronous request using a functional RequestBuilder, executed on a virtual thread (Project Loom).
     * This allows writing asynchronous code without blocking platform threads.
     * <p>
     * Example:
     * <pre><code>
     * client.requestAsync(rb -> rb.method("GET", "/path")).thenAccept(result -> {
     *     System.out.println(result.statusCode());
     * });
     * </code></pre>
     *
     * @param builderFunction function that configures the RequestBuilder
     * @return CompletableFuture resolving to HttpResult once the request completes
     */
    public CompletableFuture<HttpResult> requestAsync(Function<RequestBuilder, RequestBuilder> builderFunction) {
        RequestBuilder rb = builderFunction.apply(new RequestBuilder());
        // Launch on a virtual thread
        CompletableFuture<HttpResult> future = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            try {
                HttpResult result = request(rb);
                future.complete(result);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Sends a request using a pre-built {@link RequestBuilder}.
     * Catches exceptions and returns an error {@code HttpResult} if needed.
     *
     * @param rb the configured {@code RequestBuilder}
     * @return the {@link HttpResult} with request/response data or error info
     */
    public HttpResult request(RequestBuilder rb) {
        Instant start = Instant.now();
        try {
            Request request = rb.build(config.baseUrl);
            // 设置 HTTP 版本
            request.version(config.httpVersion);
            // 收集并添加全局默认 headers
            Map<String, String> reqHeaders = new HashMap<>();
            config.headers.forEach((k, v) -> {
                request.setHeader(k, v);
                reqHeaders.put(k, v);
            });
            // 收集并添加请求特有 headers
            rb.getHeaders().forEach((k, v) -> {
                request.setHeader(k, v);
                reqHeaders.put(k, v);
            });

            ClassicHttpResponse response = (ClassicHttpResponse) executor.execute(request).returnResponse();
            Instant end = Instant.now();

            int status = response.getCode();
            String reason = response.getReasonPhrase();
            Map<String, String> respHeaders = new HashMap<>();
            for (Header header : response.getHeaders()) respHeaders.put(header.getName(), header.getValue());

            HttpResult.Builder builder = HttpResult
                    .builder()
                    .statusCode(status)
                    .message(reason)
                    .series(status / 100)
                    .requestHeaders(reqHeaders)
                    .responseHeaders(respHeaders)
                    .startTime(start)
                    .endTime(end)
                    .duration(Duration.between(start, end));
            if (response.getEntity() != null) {
                ContentType ct = ContentType.parse(response.getEntity().getContentType());
                if (rb.isBinary() || !isText(ct)) builder.stream(response.getEntity().getContent());
                else builder.body(new String(response.getEntity().getContent().readAllBytes(), ObjectUtils.isEmpty(ct.getCharset(), StandardCharsets.UTF_8)));
            }
            return builder.build();
        } catch (IOException e) {
            Instant end = Instant.now();
            return HttpResult
                    .builder()
                    .statusCode(-1)
                    .message(e.getMessage())
                    .series(0)
                    .requestHeaders(Collections.emptyMap())
                    .responseHeaders(Collections.emptyMap())
                    .startTime(start)
                    .endTime(end)
                    .duration(Duration.between(start, end))
                    .build();
        }
    }

    /**
     * Configuration holder for {@link HttpClient}.
     * Supports baseUrl, timeouts, default headers, and credentials.
     */
    public static class Config {
        private String baseUrl;
        private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        private CredentialsProvider credentialsProvider;
        private Timeout connectionRequestTimeout = Timeout.ofSeconds(10);
        private Timeout readTimeout = Timeout.ofSeconds(10);
        private final Map<String, String> headers = new HashMap<>();
        private String callerId = "akir";

        public Config() {
            this.headers.put(CALLER_ID_HEADER, callerId);
        }

        /**
         * Sets the base URL for all requests.
         *
         * @param baseUrl e.g. "https://api.example.com"
         * @return this {@code Config} for chaining
         */
        public Config baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Sets the http version for all requests.
         *
         * @param version http version
         * @return this {@code Config} for chaining
         */
        public Config httpVersion(HttpVersion version) {
            this.httpVersion = version;
            return this;
        }

        /**
         * Adds a default header to all requests.
         *
         * @param name  header name
         * @param value header value
         * @return this {@code Config} for chaining
         */
        public Config header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        /**
         * Sets a {@link CredentialsProvider} for HTTP authentication.
         *
         * @param provider credentials provider instance
         * @return this {@code Config} for chaining
         */
        public Config credentialsProvider(CredentialsProvider provider) {
            this.credentialsProvider = provider;
            return this;
        }

        /**
         * Sets the maximum time to wait for a connection request.
         *
         * @param timeout max wait time
         * @return this {@code Config} for chaining
         */
        public Config connectionRequestTimeout(Timeout timeout) {
            this.connectionRequestTimeout = timeout;
            return this;
        }

        /**
         * Sets the maximum time to wait for a response.
         *
         * @param timeout max wait time
         * @return this {@code Config} for chaining
         */
        public Config readTimeout(Timeout timeout) {
            this.readTimeout = timeout;
            return this;
        }

        /**
         * Overrides default X-Caller-ID value.
         *
         * @param callerId custom ID string
         * @return this {@code Config} for chaining
         */
        public Config callerId(String callerId) {
            this.callerId = callerId;
            return this;
        }
    }

}

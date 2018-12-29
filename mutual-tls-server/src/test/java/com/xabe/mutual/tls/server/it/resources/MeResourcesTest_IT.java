package com.xabe.mutual.tls.server.it.resources;

import org.junit.jupiter.api.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLParameters;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class MeResourcesTest_IT extends AbstractResourcesTest_IT {
    
    @Test
    public void shouldGetInfoHttpClientJava() throws Exception {
        //Given
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        final var httpClient = createHttpClient();
        final var request = createHttpRequest("https://localhost:8443/me");
        final var future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        //When
        final var result = future
                .thenApply(response -> {
                    System.out.println("Communication done in : " + response.version());
                    System.out.println("Communication body in : " + response.body());
                    return response;
                })
                .thenApply(HttpResponse::statusCode)
                .get(10, TimeUnit.SECONDS);
        
        //Then
        assertThat(result, is(notNullValue()));
    }

    private HttpRequest createHttpRequest(String uri) {
        return HttpRequest
                .newBuilder(URI.create(uri))
                .timeout(Duration.ofMillis(5000))
                .GET()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .setHeader(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN)
                .build();
    }

    private HttpClient createHttpClient() {
        final var sslParams = new SSLParameters();
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)  // this is the default
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .sslContext(sslContext)
                .sslParameters(sslParams)
                .build();
    }
}

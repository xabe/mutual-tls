package com.xabe.mutual.tls.server.it.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StatusResourcesTest_IT extends AbstractResourcesTest_IT {

    @Test
    public void shouldGetStatusClientJAXRS() throws Exception {
        //Given
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


        final Client client = ClientBuilder.
                newBuilder().
                connectTimeout(2, TimeUnit.SECONDS).
                readTimeout(4, TimeUnit.SECONDS).
                sslContext(sslContext).build();
        final WebTarget target = client.target( "https://localhost:8443"  ).path( "/v1" ).path( "/status" );

        //When
        final Response response = client.target(target.getUri())
                .request()
                .get();

        //Then
        final String result = response.readEntity(String.class);
        assertThat(result,is(notNullValue()));
        response.close();
    }

    @Test
    public void shouldGetStatusHttpClientCommons() throws Exception {
        final CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        final RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
        final HttpGet httpGet = new HttpGet("https://localhost:8443/v1/status");
        httpGet.setConfig(config);
        final HttpResponse response = client.execute(httpGet);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
    }
}

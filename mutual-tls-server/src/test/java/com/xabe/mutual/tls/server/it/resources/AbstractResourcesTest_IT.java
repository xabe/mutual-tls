package com.xabe.mutual.tls.server.it.resources;

import com.xabe.mutual.tls.server.config.ssl.TLSUtils;
import org.junit.jupiter.api.BeforeAll;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

public abstract class AbstractResourcesTest_IT {

    protected static SSLContext sslContext;

    protected final HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    @BeforeAll
    public static void init() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        final InputStream trust = AbstractResourcesTest_IT.class.getClassLoader().getResourceAsStream("certs-client/client-truststore.jks");
        final InputStream server =  AbstractResourcesTest_IT.class.getClassLoader().getResourceAsStream("certs-server/macbook-chabir.local.jks");
        sslContext = TLSUtils.generateSSLContext("changeit", "changeit", server, "whatever", trust);
        final Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
    }
}

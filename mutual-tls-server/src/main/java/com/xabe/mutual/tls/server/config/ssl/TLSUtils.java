package com.xabe.mutual.tls.server.config.ssl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class TLSUtils {

    public static SSLContext generateSSLContext(String keyStorePassword, String keyPassword, InputStream keyStoreInput,String trustStorePassord, InputStream trustStoreInput) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        final KeyManagerFactory keyManagerFactory = generateKeyManagerFactory(keyStorePassword, keyPassword, keyStoreInput);
        final TrustManagerFactory trustManagerFactory = generateTrustManagerFactory(trustStorePassord, trustStoreInput);
        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);
        return sslContext;
    }


    private static KeyManagerFactory generateKeyManagerFactory(String keyStorePassword, String keyPassword, InputStream keyStoreInput) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException {
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keyStoreInput,keyStorePassword.toCharArray());
        final KeyManagerFactory instance = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore,keyPassword.toCharArray());
        return instance;
    }

    public static TrustManagerFactory generateTrustManagerFactory(String trustStorePassword, InputStream trustStoreInput) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException {
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(trustStoreInput,trustStorePassword.toCharArray());
        final TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore);
        return instance;
    }

}

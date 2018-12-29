package com.xabe.mutual.tls.server;

import com.xabe.mutual.tls.server.config.CustomResourceConfig;
import com.xabe.mutual.tls.server.config.ssl.TLSUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class App {
    private static final String BIND_IP = "0.0.0.0";
    private static HttpServer server;

    public static void main(String[] args) throws InterruptedException, IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(Level.ALL);
        Runtime.getRuntime().addShutdownHook( new Thread( () ->  server.shutdownNow() ));
        final ResourceConfig rc = new CustomResourceConfig();
        final InputStream keyStoreInput = App.class.getResourceAsStream("/certs-server/macbook-chabir.local.jks");
        final InputStream trustStoreInput = App.class.getResourceAsStream("/certs-client/client-truststore.jks");
        final SSLContext sslContext = TLSUtils.generateSSLContext("changeit","changeit",keyStoreInput,"whatever",trustStoreInput);
        final SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext, false, true, true);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(getUriInfo("https" ,8443)), rc, true, sslEngineConfigurator,false);
        server.getServerConfiguration().setAllowPayloadForUndefinedHttpMethods(true);
        server.start();
        LoggerFactory.getLogger(App.class).info( "Stop the application using CTRL+C" );
        Thread.currentThread().join();
    }

    private static String getUriInfo(String protocol,int port) {
        return String.format("%s://%s:%d", protocol, BIND_IP, port);
    }
}

package com.xabe.mutual.tls.server.resource;

import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.security.cert.X509Certificate;

@Singleton
@Path("/")
public class MeResource {

    public static final String JAVAX_SERVLET_REQUEST_X_509_CERTIFICATE = "javax.servlet.request.X509Certificate";
    private final Logger logger = LoggerFactory.getLogger(MeResource.class);

    @Path("/me")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getMe(@Context final ContainerRequest request) {
        logger.info("call who me");
        final X509Certificate[] certs = (X509Certificate[]) request.getProperty(JAVAX_SERVLET_REQUEST_X_509_CERTIFICATE);
        return certs[0].getSubjectDN().getName();
    }
}

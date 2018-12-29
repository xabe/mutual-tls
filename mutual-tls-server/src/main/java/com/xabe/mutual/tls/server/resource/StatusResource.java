package com.xabe.mutual.tls.server.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/v1")
public class StatusResource {

    private final Logger logger = LoggerFactory.getLogger(StatusResource.class);

    @Path("/status")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getStatus() {
        logger.info("call status");
        return "OK";
    }
}

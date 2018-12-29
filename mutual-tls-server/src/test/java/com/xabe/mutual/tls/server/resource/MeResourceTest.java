package com.xabe.mutual.tls.server.resource;

import org.glassfish.jersey.server.ContainerRequest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.security.cert.X509Certificate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MeResourceTest {

    @Test
    public void givenAContainerRequestWhenInvokeGetMeThenReturnStringInfo() throws Exception {
        //Given
        final ContainerRequest containerRequest = mock(ContainerRequest.class);
        final  MeResource meResource = new MeResource();
        final X509Certificate x509Certificate = mock(X509Certificate.class);
        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("info");
        when(x509Certificate.getSubjectDN()).thenReturn(principal);
        when(containerRequest.getProperty(MeResource.JAVAX_SERVLET_REQUEST_X_509_CERTIFICATE)).thenReturn(new X509Certificate []{x509Certificate});

        //When
        final String result = meResource.getMe(containerRequest);

        //Then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));
    }

}
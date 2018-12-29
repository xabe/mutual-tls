package com.xabe.mutual.tls.server.resource;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class StatusResourceTest {

    @Test
    public void shouldReturnStatus() throws Exception {
        MatcherAssert.assertThat(new StatusResource().getStatus(), Matchers.is(Matchers.notNullValue()));
    }

}
/*
 * Copyright 2005-2008 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */
package org.restlet.test.jaxrs.services.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.restlet.test.jaxrs.services.tests.SimpleHouseTest;

/**
 * This class contains only data for one media type.
 * 
 * @author Stephan Koops
 * @see SimpleHouseTest
 */
@Path("/ho%20use")
public class SimpleHouse {
    /** Text of the Plain-Text-Representation. */
    public static final String RERP_PLAIN_TEXT = "  /\\ \n /  \\ \n |  | \n +--+ \n \n This is a simple text house";

    @GET
    @Path("null")
    public Object getNull() {
        return null;
    }

    @GET
    @Path("nullWithMediaType")
    @Produces("text/plain")
    public Object getNullWithMediaType() {
        return null;
    }

    /**
     * 
     * @return
     */
    @GET
    @Produces("text/plain")
    public String getPlainText() {
        return RERP_PLAIN_TEXT;
    }
}
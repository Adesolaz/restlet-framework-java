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
package org.restlet.test.jaxrs.services.tests;

import java.util.Set;

import org.restlet.data.Response;
import org.restlet.ext.jaxrs.internal.util.Util;
import org.restlet.test.jaxrs.services.providers.EntityConstructorProvider;
import org.restlet.test.jaxrs.services.providers.ParamConstructorProvider;
import org.restlet.test.jaxrs.services.resources.IllegalConstructorResource;

/**
 * Checks, if illegal things are forbidden.
 * 
 * @author Stephan Koops
 * @see IllegalConstructorResource
 */
public class IllegalConstructorTest extends JaxRsTestCase {

    @Override
    @SuppressWarnings("unchecked")
    protected Set<Class<?>> getProvClasses() {
        return Util.createSet(ParamConstructorProvider.class,
                EntityConstructorProvider.class);
    }

    @Override
    protected Class<?> getRootResourceClass() {
        return IllegalConstructorResource.class;
    }

    public void testNullSubResource() throws Exception {
        final Response response = get();
        assertTrue(response.getStatus().isError());
    }
}
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

package com.noelios.restlet.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite of unit tests for the Noelios Restlet Engine.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class NoeliosTestSuite extends TestSuite {

    /**
     * JUnit constructor.
     * 
     * @return The unit test.
     */
    public static Test suite() {
        return new NoeliosTestSuite();
    }

    /** Constructor. */
    public NoeliosTestSuite() {
        addTestSuite(AuthenticationTestCase.class);
        addTestSuite(Base64TestCase.class);
        addTestSuite(ChunkedEncodingTestCase.class);
        addTestSuite(ChunkedInputStreamTestCase.class);
        addTestSuite(ChunkedOutputStreamTestCase.class);
        addTestSuite(CookiesTestCase.class);
        addTestSuite(FormTestCase.class);
        addTestSuite(GetTestCase.class);
        addTestSuite(GetChunkedTestCase.class);
        addTestSuite(HeaderTestCase.class);
        addTestSuite(HttpCallTestCase.class);
        addTestSuite(InputEntityStreamTestCase.class);
        addTestSuite(KeepAliveInputStreamTestCase.class);
        addTestSuite(KeepAliveOutputStreamTestCase.class);
        addTestSuite(PostPutTestCase.class);
        addTestSuite(PreferencesTestCase.class);
        addTestSuite(RemoteClientAddressTestCase.class);
        // addTestSuite(SslGetTestCase.class);
        // addTestSuite(SslContextGetTestCase.class);
        addTestSuite(TunnelFilterTestCase.class);
    }
}

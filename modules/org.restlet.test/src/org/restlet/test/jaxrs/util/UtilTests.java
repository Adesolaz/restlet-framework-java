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
package org.restlet.test.jaxrs.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import junit.framework.TestCase;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.jaxrs.internal.core.MultivaluedMapImpl;
import org.restlet.ext.jaxrs.internal.util.Converter;
import org.restlet.ext.jaxrs.internal.util.PathRegExp;
import org.restlet.ext.jaxrs.internal.util.Util;

/**
 * @author Stephan Koops
 * @see PathRegExp
 */
@SuppressWarnings("all")
public class UtilTests extends TestCase {

    private MultivaluedMap<String, Object> httpHeaders;

    /**
     * @return the {@link CharacterSet} as String
     */
    private String getCss() {
        return Util.getCharsetName(this.httpHeaders, null);
    }

    /**
     * @return the {@link MediaType} without any parameter
     */
    private MediaType getMt() {
        final MediaType mediaType = Util.getMediaType(this.httpHeaders);
        if (mediaType == null) {
            return null;
        }
        return Converter.getMediaTypeWithoutParams(mediaType);
    }

    /**
     * @return the {@link MediaType} without any parameter as String
     */
    private String getMts() {
        final MediaType mediaType = getMt();
        if (mediaType == null) {
            return null;
        }
        return mediaType.toString();
    }

    private void setContentType(MediaType mediaType, CharacterSet characterSet) {
        if (characterSet != null) {
            mediaType.getParameters().add("charset", characterSet.getName());
        }
        setContentType(mediaType.toString());
    }

    private void setContentType(String contentType) {
        this.httpHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);
    }

    public void setUp() {
        this.httpHeaders = new MultivaluedMapImpl<String, Object>();
    }

    public void testGetOfContentType0() {
        assertEquals(null, getCss());
        assertEquals(null, getMts());
    }

    public void testGetOfContentType1() {
        setContentType("a/b;charset=CS");
        assertEquals("CS", getCss());
        assertEquals("a/b", getMts());
    }

    public void testGetOfContentType2() {
        setContentType(MediaType.TEXT_HTML, null);
        assertEquals(null, getCss());
        assertEquals(MediaType.TEXT_HTML, getMt());
    }

    public void testGetOfContentType3() {
        setContentType("a/b ;charset=CS");
        assertEquals("CS", getCss());
        assertEquals("a/b", getMts());
    }

    public void testGetOfContentType4() {
        setContentType("a/b;d=g;charset=CS");
        assertEquals("CS", getCss());
        assertEquals("a/b", getMts());
    }
}
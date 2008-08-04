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

package org.restlet.ext.jaxrs.internal.exceptions;

import javax.ws.rs.Path;

/**
 * This kind of exception is thrown, when an &#64{@link Path} annotation
 * contains illegal characters.
 * 
 * @author Stephan Koops
 */
public class IllegalPathException extends JaxRsException {

    private static final long serialVersionUID = 6796414811480666857L;

    private static String createMessage(IllegalArgumentException iae, Path path) {
        if (iae != null) {
            final Throwable cause = iae.getCause();
            if (cause != null) {
                final String message = cause.getMessage();
                if ((message == null) || (message.length() == 0)) {
                    return "The given path (" + path + ") is invalid";
                }
            }
        }
        return null;
    }

    private final Path path;

    /**
     * 
     * @param path
     *            the invalid path
     * @param iae
     */
    public IllegalPathException(Path path, IllegalArgumentException iae) {
        super(createMessage(iae, path), iae);
        this.path = path;
    }

    /**
     * 
     * @param path
     * @param message
     */
    public IllegalPathException(Path path, String message) {
        super(message);
        this.path = path;
    }

    /**
     * 
     * @param path
     * @param message
     * @param iae
     */
    public IllegalPathException(Path path, String message,
            IllegalArgumentException iae) {
        super(message, iae);
        this.path = path;
    }

    /**
     * Returns the cause {@link IllegalArgumentException}. If not available, it
     * is created.
     * 
     * @see java.lang.Throwable#getCause()
     */
    @Override
    public IllegalArgumentException getCause() {
        final Throwable cause = super.getCause();
        if (cause instanceof IllegalArgumentException) {
            return (IllegalArgumentException) cause;
        }
        final IllegalArgumentException iae = new IllegalArgumentException(
                getMessage());
        if (cause != null) {
            iae.setStackTrace(cause.getStackTrace());
        }
        return iae;
    }

    /**
     * Returns the Illegal Path.
     * 
     * @return the Illegal Path.
     */
    public Path getPath() {
        return this.path;
    }
}
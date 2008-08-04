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
package org.restlet.ext.jaxrs.internal.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator that iterates over exact one element.
 * 
 * @author Stephan Koops
 * @param <T>
 *            The type of the contained object.
 */
public class OneElementIterator<T> implements Iterator<T> {

    private final T element;

    private boolean hasNext = true;

    /**
     * @param element
     *            The element to iterate over. May be null.
     */
    public OneElementIterator(T element) {
        this.element = element;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return this.hasNext;
    }

    /**
     * @see java.util.Iterator#next()
     */
    public T next() throws NoSuchElementException {
        if (!this.hasNext) {
            throw new NoSuchElementException("The element was already returned");
        }
        this.hasNext = false;
        return this.element;
    }

    /**
     * @see java.util.Iterator#remove()
     * @throws UnsupportedOperationException
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "The OneElementIterator is not modifiable");
    }
}
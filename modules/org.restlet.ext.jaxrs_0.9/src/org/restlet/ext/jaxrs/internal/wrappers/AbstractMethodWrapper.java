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
package org.restlet.ext.jaxrs.internal.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.ws.rs.Encoded;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import org.restlet.ext.jaxrs.internal.core.ThreadLocalizedContext;
import org.restlet.ext.jaxrs.internal.exceptions.ConvertRepresentationException;
import org.restlet.ext.jaxrs.internal.exceptions.IllegalMethodParamTypeException;
import org.restlet.ext.jaxrs.internal.exceptions.IllegalPathOnMethodException;
import org.restlet.ext.jaxrs.internal.exceptions.IllegalPathParamTypeException;
import org.restlet.ext.jaxrs.internal.exceptions.IllegalTypeException;
import org.restlet.ext.jaxrs.internal.exceptions.MissingAnnotationException;
import org.restlet.ext.jaxrs.internal.util.PathRegExp;
import org.restlet.ext.jaxrs.internal.util.Util;
import org.restlet.ext.jaxrs.internal.wrappers.params.ParameterList;
import org.restlet.ext.jaxrs.internal.wrappers.provider.JaxRsProviders;
import org.restlet.ext.jaxrs.internal.wrappers.provider.ExtensionBackwardMapping;

/**
 * An abstract wrapper class for resource methods, sub resource methods and sub
 * resource locators.
 * 
 * @author Stephan Koops
 */
public abstract class AbstractMethodWrapper extends AbstractJaxRsWrapper {

    /**
     * the Java method that should be called. This method could be different
     * from the method containing the annotations, see section 3.6 "Annotation
     * Inheritance" of JSR-311-spec.
     * 
     * @see ResourceMethod#annotatedMethod
     */
    final Method executeMethod;

    final ParameterList parameters;

    final ResourceClass resourceClass;

    /**
     * 
     * @param executeMethod
     * @param annotatedMethod
     * @param resourceClass
     * @param tlContext
     * @param jaxRsProviders
     * @param extensionBackwardMapping
     * @param entityAllowed
     * @param logger
     * @throws IllegalPathOnMethodException
     * @throws IllegalArgumentException
     *                 if the annotated method is null
     * @throws MissingAnnotationException
     * @throws IllegalMethodParamTypeException
     *                 if one of the parameters annotated with &#64;{@link Context}
     *                 has a type that must not be annotated with &#64;
     *                 {@link Context}.
     * @throws IllegalPathParamTypeException
     */
    AbstractMethodWrapper(Method executeMethod, Method annotatedMethod,
            ResourceClass resourceClass, ThreadLocalizedContext tlContext,
            JaxRsProviders jaxRsProviders,
            ExtensionBackwardMapping extensionBackwardMapping,
            boolean entityAllowed, Logger logger)
            throws IllegalPathOnMethodException, IllegalArgumentException,
            MissingAnnotationException, IllegalMethodParamTypeException,
            IllegalPathParamTypeException {
        super(PathRegExp.createForMethod(annotatedMethod));
        this.executeMethod = executeMethod;
        this.executeMethod.setAccessible(true);
        // NICE log message, if an Exception with no exc mapper is declared.
        this.resourceClass = resourceClass;
        final boolean leaveEncoded = resourceClass.isLeaveEncoded()
                || annotatedMethod.isAnnotationPresent(Encoded.class);
        try {
            this.parameters = new ParameterList(executeMethod, annotatedMethod,
                    tlContext, leaveEncoded, jaxRsProviders,
                    extensionBackwardMapping, entityAllowed, logger);
        } catch (final IllegalTypeException e) {
            throw new IllegalMethodParamTypeException(e);
        }
    }

    /**
     * Returns the name of the method
     * 
     * @return Returns the name of the method
     */
    public String getName() {
        final Class<?>[] paramTypes = this.executeMethod.getParameterTypes();
        final StringBuilder stb = new StringBuilder();
        stb.append(this.executeMethod.getName());
        stb.append('(');
        Util.append(stb, paramTypes);
        stb.append(')');
        return stb.toString();
    }

    /**
     * @return Returns the regular expression for the URI template
     */
    @Override
    public PathRegExp getPathRegExp() {
        return super.getPathRegExp();
    }

    /**
     * @return Returns the resource class of this method.
     */
    public ResourceClass getResourceClass() {
        return this.resourceClass;
    }

    /**
     * Invokes the method and returned the created representation for the
     * response.
     * 
     * @param resourceObject
     * @return the unwrapped returned object by the wrapped method.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws WebApplicationException
     * @throws ConvertRepresentationException
     */
    Object internalInvoke(ResourceObject resourceObject)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ConvertRepresentationException,
            WebApplicationException {
        final Object[] args = this.parameters.get();
        final Object jaxRsResourceObj = resourceObject.getJaxRsResourceObject();
        return this.executeMethod.invoke(jaxRsResourceObj, args);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "["
                + this.executeMethod.getDeclaringClass().getSimpleName() + "."
                + this.executeMethod.getName() + "(__)]";
    }
}
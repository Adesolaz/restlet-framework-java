/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.ext.xstream;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

/**
 * Converter between the XML/JSON and Representation classes based on XStream.<br>
 * <br>
 * SECURITY WARNING: XStream applies various techniques under the hood to ensure
 * it is able to handle all types of objects. This includes using undocumented
 * Java features and reflection. The XML generated by XStream includes all
 * information required to build objects of almost any type. This introduces a
 * potential security problem. <br>
 * The XML provided to XStream for conversion to a Java object can be
 * manipulated to inject objects into the unmarshalled object graph, which were
 * not present at marshalling time. An attacker could exploit this to execute
 * arbitrary code or shell commands in the context of the server running the
 * XStream process. This issue has been addressed and is clearly documented
 * here: <a href="http://xstream.codehaus.org/security.html#framework">XStream
 * Security Framework</a>.<br>
 * You can configure the Xstream object used by this converter by overriding the
 * {@link #createXstream(MediaType, Class)} method and apply your own security
 * permissions, and provide this new converter to the Restlet Engine (see
 * org.restlet.engine.Engine#getRegisteredConverters method).
 * 
 * @author Jerome Louvel
 */
public class XstreamConverter extends ConverterHelper {

    private static final VariantInfo VARIANT_APPLICATION_ALL_XML = new VariantInfo(
            MediaType.APPLICATION_ALL_XML);

    private static final VariantInfo VARIANT_APPLICATION_XML = new VariantInfo(
            MediaType.APPLICATION_XML);

    private static final VariantInfo VARIANT_JSON = new VariantInfo(
            MediaType.APPLICATION_JSON);

    private static final VariantInfo VARIANT_TEXT_XML = new VariantInfo(
            MediaType.TEXT_XML);

    /** The XStream JSON driver class. */
    private Class<? extends HierarchicalStreamDriver> jsonDriverClass;

    /** The XStream XML driver class. */
    private Class<? extends HierarchicalStreamDriver> xmlDriverClass;

    /**
     * Constructor.
     */
    public XstreamConverter() {
        this.jsonDriverClass = JettisonMappedXmlDriver.class;
        this.xmlDriverClass = DomDriver.class;
    }

    /**
     * Creates the marshaling {@link XstreamRepresentation}.
     * 
     * @param <T>
     * @param mediaType
     *            The target media type.
     * @param source
     *            The source object to marshal.
     * @return The marshaling {@link XstreamRepresentation}.
     */
    protected <T> XstreamRepresentation<T> create(MediaType mediaType, T source) {
        return new XstreamRepresentation<T>(mediaType, source);
    }

    /**
     * Creates the unmarshaling {@link XstreamRepresentation}.
     * 
     * @param <T>
     * @param source
     *            The source representation to unmarshal.
     * @param target
     *            The expected class of the Java object.
     * @return The unmarshaling {@link XstreamRepresentation}.
     * @throws IOException
     */
    protected <T> XstreamRepresentation<T> create(Representation source,
            Class<T> target) throws IOException {
        XstreamRepresentation<T> representation = new XstreamRepresentation<T>(
                source, target);
        representation.setXstream(createXstream(source.getMediaType(), target));
        return representation;
    }

    /**
     * Creates an XStream object based on a media type. By default, it creates a
     * {@link HierarchicalStreamDriver} or a {@link DomDriver}. To be overriden
     * in order to customize security permissions.
     * 
     * @param <T>
     * @param mediaType
     *            The serialization media type.
     * @param target
     *            The expected class of the Java object.
     * @return The XStream object.
     * @throws IOException
     */
    public <T> XStream createXstream(MediaType mediaType, Class<T> target)
            throws IOException {
        XStream result = null;

        try {
            if (MediaType.APPLICATION_JSON.isCompatible(mediaType)) {
                result = new XStream(getJsonDriverClass().newInstance());
                result.setMode(XStream.NO_REFERENCES);
            } else {
                result = new XStream(getXmlDriverClass().newInstance());
            }

            // clear out existing permissions and set own ones
            result.addPermission(NoTypePermission.NONE);
            // allow some basics
            result.addPermission(NullPermission.NULL);
            result.addPermission(PrimitiveTypePermission.PRIMITIVES);
            result.allowTypeHierarchy(Collection.class);
            // allow any type from the same package
            result.allowTypesByWildcard(new String[] { target.getPackage()
                    .getName() + ".*" });

            result.autodetectAnnotations(true);
        } catch (Exception e) {
            IOException ioe = new IOException(
                    "Unable to create the XStream driver: " + e.getMessage());
            ioe.initCause(e);
            throw ioe;
        }

        return result;
    }

    /**
     * Returns the XStream JSON driver class.
     * 
     * @return TXStream JSON driver class.
     */
    public Class<? extends HierarchicalStreamDriver> getJsonDriverClass() {
        return jsonDriverClass;
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_JSON.isCompatible(source)
                || VARIANT_APPLICATION_ALL_XML.isCompatible(source)
                || VARIANT_APPLICATION_XML.isCompatible(source)
                || VARIANT_TEXT_XML.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, XstreamRepresentation.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (source != null) {
            result = addVariant(result, VARIANT_JSON);
            result = addVariant(result, VARIANT_APPLICATION_ALL_XML);
            result = addVariant(result, VARIANT_APPLICATION_XML);
            result = addVariant(result, VARIANT_TEXT_XML);
        }

        return result;
    }

    /**
     * Returns the XStream XML driver class.
     * 
     * @return The XStream XML driver class.
     */
    public Class<? extends HierarchicalStreamDriver> getXmlDriverClass() {
        return xmlDriverClass;
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;

        if (source instanceof XstreamRepresentation<?>) {
            result = 1.0F;
        } else {
            if (target == null) {
                result = 0.5F;
            } else if (VARIANT_JSON.isCompatible(target)) {
                result = 0.8F;
            } else if (VARIANT_APPLICATION_ALL_XML.isCompatible(target)
                    || VARIANT_APPLICATION_XML.isCompatible(target)
                    || VARIANT_TEXT_XML.isCompatible(target)) {
                result = 0.8F;
            } else {
                result = 0.5F;
            }
        }

        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target,
            Resource resource) {
        float result = -1.0F;

        if (target != null) {
            if (source instanceof XstreamRepresentation<?>) {
                result = 1.0F;
            } else if (XstreamRepresentation.class.isAssignableFrom(target)) {
                result = 1.0F;
            } else if (VARIANT_JSON.isCompatible(source)) {
                result = 0.8F;
            } else if (VARIANT_APPLICATION_ALL_XML.isCompatible(source)
                    || VARIANT_APPLICATION_XML.isCompatible(source)
                    || VARIANT_TEXT_XML.isCompatible(source)) {
                result = 0.8F;
            }
        } else {
            result = 0.5F;
        }

        return result;
    }

    /**
     * Sets the XStream JSON driver class.
     * 
     * @param jsonDriverClass
     *            The XStream JSON driver class.
     */
    public void setJsonDriverClass(
            Class<? extends HierarchicalStreamDriver> jsonDriverClass) {
        this.jsonDriverClass = jsonDriverClass;
    }

    /**
     * Sets the XStream XML driver class.
     * 
     * @param xmlDriverClass
     *            The XStream XML driver class.
     */
    public void setXmlDriverClass(
            Class<? extends HierarchicalStreamDriver> xmlDriverClass) {
        this.xmlDriverClass = xmlDriverClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target,
            Resource resource) throws IOException {
        T result = null;

        // The source for the XStream conversion
        XstreamRepresentation<?> xstreamSource = null;

        if (source instanceof XstreamRepresentation) {
            xstreamSource = (XstreamRepresentation<?>) source;
            if (target != null) {
                xstreamSource.getXstream().processAnnotations(target);
            }
        } else if (VARIANT_JSON.isCompatible(source)) {
            xstreamSource = create(source, target);
        } else if (VARIANT_APPLICATION_ALL_XML.isCompatible(source)
                || VARIANT_APPLICATION_XML.isCompatible(source)
                || VARIANT_TEXT_XML.isCompatible(source)) {
            xstreamSource = create(source, target);
        }

        if (xstreamSource != null) {
            try {
                // Handle the conversion
                if ((target != null)
                        && XstreamRepresentation.class.isAssignableFrom(target)) {
                    result = target.cast(xstreamSource);
                } else {
                    result = (T) xstreamSource.getObject();
                }
            } catch (IllegalArgumentException iae) {
                throw new IOException(
                        "SECURITY WARNING: The usage of XstreamRepresentation when "
                                + "deserializing representations from unstrusted "
                                + "sources can lead to malicious attacks. As pointed "
                                + "here (http://xstream.codehaus.org/security.html), "
                                + "it is possible to force the JVM to execute unwanted "
                                + "Java code described inside the representation."
                                + "Thus, the support of such format has been "
                                + "restricted by default to basic data types such as "
                                + "primitive types, subclasses of the java.util.Collection "
                                + "class (cf http://xstream.codehaus.org/security.html#framework). "
                                + "You can enhance this behavior by overriding the "
                                + "org.restlet.ext.xstream.XStreamConverter#createXstream(MediaType, Class<T>) method "
                                + "and provide this new converter helper to the Restlet Engine "
                                + "(see org.restlet.engine.Engine#getRegisteredConverters method).",
                        iae);
            }
        }

        return result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target,
            Resource resource) {
        Representation result = null;

        if (source instanceof XstreamRepresentation) {
            result = (XstreamRepresentation<?>) source;
        } else {
            if (target.getMediaType() == null) {
                target.setMediaType(MediaType.TEXT_XML);
            }

            if (VARIANT_JSON.isCompatible(target)) {
                XstreamRepresentation<Object> xstreamRepresentation = create(
                        target.getMediaType(), source);
                result = xstreamRepresentation;
            } else if (VARIANT_APPLICATION_ALL_XML.isCompatible(target)
                    || VARIANT_APPLICATION_XML.isCompatible(target)
                    || VARIANT_TEXT_XML.isCompatible(target)) {
                result = create(target.getMediaType(), source);
            }
        }

        return result;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
        updatePreferences(preferences, MediaType.APPLICATION_ALL_XML, 1.0F);
        updatePreferences(preferences, MediaType.APPLICATION_JSON, 1.0F);
        updatePreferences(preferences, MediaType.APPLICATION_XML, 1.0F);
        updatePreferences(preferences, MediaType.TEXT_XML, 1.0F);
    }

}

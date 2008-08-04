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

package org.restlet.ext.atom;

import static org.restlet.ext.atom.Feed.ATOM_NAMESPACE;
import static org.restlet.ext.atom.Service.APP_NAMESPACE;

import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.util.XmlWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Atom Protocol collection, part of a workspace.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class Collection {

    /**
     * The hypertext reference.
     */
    private volatile Reference href;

    /**
     * The type of members.
     */
    private volatile MemberType memberType;

    /**
     * The title.
     */
    private volatile String title;

    /**
     * The parent workspace.
     */
    private volatile Workspace workspace;

    /**
     * Constructor.
     * 
     * @param workspace
     *            The parent workspace.
     * @param title
     *            The title.
     * @param href
     *            The hypertext reference.
     */
    public Collection(Workspace workspace, String title, String href) {
        this.workspace = workspace;
        this.title = title;
        this.href = new Reference(href);
        this.memberType = null;
    }

    /**
     * Returns the feed representation.
     * 
     * @return The feed representation.
     * @throws Exception
     */
    public Feed getFeed() throws Exception {
        final Reference feedRef = getHref();

        if (feedRef.isRelative()) {
            feedRef.setBaseRef(getWorkspace().getService().getReference());
        }

        final Request request = new Request(Method.GET, feedRef.getTargetRef());
        final Response response = getWorkspace().getService()
                .getClientDispatcher().handle(request);

        if (response.getStatus().equals(Status.SUCCESS_OK)) {
            return new Feed(response.getEntity());
        } else {
            throw new Exception(
                    "Couldn't get the feed representation. Status returned: "
                            + response.getStatus().getDescription());
        }
    }

    /**
     * Returns the hypertext reference.
     * 
     * @return The hypertext reference.
     */
    public Reference getHref() {
        return this.href;
    }

    /**
     * Returns the type of members.
     * 
     * @return The type of members.
     */
    public MemberType getMemberType() {
        return this.memberType;
    }

    /**
     * Returns the title.
     * 
     * @return The title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the parent workspace.
     * 
     * @return The parent workspace.
     */
    public Workspace getWorkspace() {
        return this.workspace;
    }

    /**
     * Posts a member to the collection resulting in the creation of a new
     * resource.
     * 
     * @param member
     *            The member representation to post.
     * @return The reference of the new resource.
     * @throws Exception
     */
    public Reference postMember(Representation member) throws Exception {
        final Request request = new Request(Method.POST, getHref(), member);
        final Response response = getWorkspace().getService()
                .getClientDispatcher().handle(request);

        if (response.getStatus().equals(Status.SUCCESS_CREATED)) {
            return response.getLocationRef();
        } else {
            throw new Exception(
                    "Couldn't post the member representation. Status returned: "
                            + response.getStatus().getDescription());
        }
    }

    /**
     * Sets the hypertext reference.
     * 
     * @param href
     *            The hypertext reference.
     */
    public void setHref(Reference href) {
        this.href = href;
    }

    /**
     * Sets the type of members.
     * 
     * @param memberType
     *            The type of members.
     */
    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the parent workspace.
     * 
     * @param workspace
     *            The parent workspace.
     */
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * Writes the current object as an XML element using the given SAX writer.
     * 
     * @param writer
     *            The SAX writer.
     * @throws SAXException
     */
    public void writeElement(XmlWriter writer) throws SAXException {

        final AttributesImpl attributes = new AttributesImpl();
        if ((getHref() != null) && (getHref().toString() != null)) {
            attributes.addAttribute("", "href", null, "atomURI", getHref()
                    .toString());
        }

        writer.startElement(APP_NAMESPACE, "collection", null, attributes);

        if (getTitle() != null) {
            writer.dataElement(ATOM_NAMESPACE, "title", getTitle());
        }

        if (getMemberType() != null) {
            // TODO ??
            getMemberType().writeElement(writer, APP_NAMESPACE);
        }

        try {
            if (getFeed() != null) {
                getFeed().writeElement(writer);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        writer.endElement(APP_NAMESPACE, "collection");
    }

}

/*
 * Copyright � 2005 J�r�me LOUVEL.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.restlet;

import java.io.IOException;

import org.restlet.component.RestletContainer;
import org.restlet.component.RestletServer;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Representation;

/**
 * Factory for common restlet objects.
 */
public interface RestletFactory
{
   /**
    * Returns a new restlet server.
    * @param name The server's name.
    * @return     The new restlet server.
    */
   public RestletServer createRestletServer(String name);

   /**
    * Returns a new restlet container.
    * @param name The container's name.
    * @return     The new restlet container.
    */
   public RestletContainer createRestletContainer(String name);

   /**
    * Returns a new cookie setting.
    * @param name    The name.
    * @param value   The value.
    * @return        A new cookie setting.
    */
   public CookieSetting createCookieSetting(String name, String value);

   /**
    * Creates a new form able to process the given form content.
    * @param content The form content to process.
    * @return        A new form with the given content.
    */
   public Form createForm(Representation content) throws IOException;

   /**
    * Creates a new reference from a URI reference.
    * @param uriReference  The URI reference.
    * @return              The new URI reference.
    */
   public Reference createReference(String uriReference);

   /**
    * Creates a new uniform call.
    * @return A new uniform call.
    */
   public UniformCall createCall();
   
}

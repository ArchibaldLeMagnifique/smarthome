/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.core.picture;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.smarthome.core.auth.Role;
import org.eclipse.smarthome.io.rest.SatisfiableRESTResource;

import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * {@link PictureResource} provides access to pictures via REST.
 *
 * @author Denis Lachartre - Initial contribution
 */
@Path(PictureResource.PATH_CONFIG_DESCRIPTIONS)
@RolesAllowed({ Role.ADMIN })
@Api(value = PictureResource.PATH_CONFIG_DESCRIPTIONS)
public class PictureResource implements SatisfiableRESTResource {

    /** The URI path to this resource */
    public static final String PATH_CONFIG_DESCRIPTIONS = "pictures";

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Gets all pictures.")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response getAll() {
        Object pictures = "oui";
        return Response.ok(Lists.newArrayList(pictures)).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Stores a pictures.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Item not found") })
    public Response post() {
        Object pictures = "done";
        return Response.ok(Lists.newArrayList(pictures)).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Deletes a pictures.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Item not found") })
    public Response delete() {
        Object pictures = "done";
        return Response.ok(Lists.newArrayList(pictures)).build();
    }

    @Override
    public boolean isSatisfied() {
        return true;
    }
}

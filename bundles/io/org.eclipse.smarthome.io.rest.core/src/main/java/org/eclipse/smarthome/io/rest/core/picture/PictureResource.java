/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.core.picture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.smarthome.core.auth.Role;
import org.eclipse.smarthome.io.rest.SatisfiableRESTResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * {@link PictureResource} provides access to pictures via REST.
 *
 * @author Denis Lachartre - Initial contribution
 */
@Path(PictureResource.PATH_PICTURE)
@Api(value = PictureResource.PATH_PICTURE)
public class PictureResource implements SatisfiableRESTResource {

    /** The URI path to this resource */
    public static final String PATH_PICTURE = "pictures";

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ Role.USER, Role.ADMIN })
    @ApiOperation(value = "Gets all pictures.")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response getAll() {
        Object response = "Toutes les images";
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ Role.USER, Role.ADMIN })
    @ApiOperation(value = "Gets one specific picture.")
    @Path("/{imageName}")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Picture not found") })
    public Response getOne(@HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) @ApiParam(value = "language") String language,
            @PathParam("imageName") @ApiParam(value = "imageName") String imageName) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new java.io.File(
                    "./../../../openhab-distro/features/distro-resources/src/main/resources/pictures/" + (imageName)));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            return Response.ok(imageBytes).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("Error while loading picture").build();
        }
        // return Response.ok("Unknown error occurred.").build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Stores a pictures.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Picture not found") })
    public Response create(
            @HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) @ApiParam(value = "name", required = true) String name,
            @ApiParam(value = "data", required = true) byte[] data) {
        // create the pictures directory
        File directory;
        directory = new java.io.File("./../../../openhab-distro/features/distro-resources/src/main/resources/pictures");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // create the image
        File photo = new File(
                "./../../../openhab-distro/features/distro-resources/src/main/resources/pictures/" + name + ".jpg");

        if (photo.exists()) {
            return Response.ok("A picture with same name already exists.").build();
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(photo.getPath());

            fos.write(data);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok("Picture successfully added.").build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Deletes a pictures.")
    @Path("/{pictures: [a-zA-Z_0-9]*}")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Item not found") })
    public Response delete() {
        Object response = "done";
        return Response.ok(response).build();
    }

    @Override
    public boolean isSatisfied() {
        return true;
    }
}

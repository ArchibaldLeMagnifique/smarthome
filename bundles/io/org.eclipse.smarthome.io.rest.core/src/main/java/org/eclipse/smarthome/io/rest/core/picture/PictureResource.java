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
    @ApiOperation(value = "Gets all pictures name.")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response getAll() {
        String response = "";
        for (final File f : new java.io.File(
                "./../../../openhab-distro/features/distro-resources/src/main/resources/pictures").listFiles()) {
            // search for every jpg in the directory
            if (f != null && f.getName().toLowerCase().endsWith(".jpg")) {
                try {
                    ImageIO.read(f);
                    response += f.getName() + "\n";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (response == "") {
            return Response.status(Response.Status.NOT_FOUND).entity("No picture in directory.").build();
        }

        return Response.ok(response).build();
    }

    @GET
    @Produces("image/jpg")
    @RolesAllowed({ Role.USER, Role.ADMIN })
    @ApiOperation(value = "Gets one specific picture.")
    @Path("/{pictures}")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response getOne(@PathParam("pictures") @ApiParam(value = "name of the picture + .jpg") String pictures) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new java.io.File(
                    "./../../../openhab-distro/features/distro-resources/src/main/resources/pictures/" + (pictures)));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            return Response.ok(imageBytes).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while loading picture.")
                    .build();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ Role.USER, Role.ADMIN })
    @ApiOperation(value = "Stores a pictures.")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response create(@HeaderParam("name") @ApiParam(value = "name of the picture", required = true) String name,
            @ApiParam(value = "datas of the picture in byte array", required = true) byte[] data) {
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
            return Response.status(Response.Status.CONFLICT).entity("A picture with same name already exists.").build();
        }
        // store it
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
    @RolesAllowed({ Role.USER, Role.ADMIN })
    @ApiOperation(value = "Deletes a pictures.")
    @Path("/{pictures}")
    @ApiResponses(value = @ApiResponse(code = 200, message = "OK"))
    public Response delete(@PathParam("pictures") @ApiParam(value = "pictures") String pictures) {
        try {
            File file = new java.io.File(
                    "./../../../openhab-distro/features/distro-resources/src/main/resources/pictures/" + pictures
                            + ".jpg");
            if (!file.exists()) {
                return Response.status(Response.Status.NOT_FOUND).entity(pictures + " doesn't exist.").build();
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while deleting " + pictures)
                    .build();
        }
        return Response.ok(pictures + " has been deleted.").build();
    }

    @Override
    public boolean isSatisfied() {
        return true;
    }
}

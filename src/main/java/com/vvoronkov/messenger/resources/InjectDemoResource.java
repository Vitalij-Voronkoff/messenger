package com.vvoronkov.messenger.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {

    @GET
    @Path("/annotations")
    public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
                                            @HeaderParam("headerParam") String headerParam,
                                            @CookieParam("name") String cookie){
        return "Martix param: " + matrixParam + ", header param: " + headerParam + ", cookie: " + cookie;
    }

    @GET
    @Path("context")
    public String getParamsUsingContext(@Context UriInfo uriInfo,
                                        @Context HttpHeaders httpHeaders){
        String cookies = httpHeaders.getCookies().toString();
        String path = uriInfo.getAbsolutePath().toASCIIString();
        return "Path: " + path + ", cookies : " + cookies;
    }
}

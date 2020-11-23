package com.wanhive.iot.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wanhive.iot.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/test")
@Api(value = "/test")
@SwaggerDefinition(tags = {
		@Tag(name = "Application test", description = "REST endpoint for testing and validation of the web application.") })
public class TestResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloWorld() {
		return Response.ok(Constants.getInfo()).build();
	}

	@GET
	@Path("exception")
	@Produces(MediaType.APPLICATION_JSON)
	public Response testException() throws Exception {
		throw new Exception("An exception occurred");
	}
}

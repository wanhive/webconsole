package com.wanhive.iot.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wanhive.iot.Constants;
import com.wanhive.iot.bean.ApplicationInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/info")
@Api(value = "/info")
@SwaggerDefinition(tags = { @Tag(name = "Application information", description = "REST endpoint for application information.") })
public class AppResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns application info", notes = "Prefetch these values to display on your web interface", response = ApplicationInfo.class)
	public Response info() {
		return Response.ok(Constants.getInfo()).build();
	}
}

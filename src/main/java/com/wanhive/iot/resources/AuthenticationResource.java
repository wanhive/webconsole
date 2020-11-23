package com.wanhive.iot.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wanhive.iot.IotApplication;
import com.wanhive.iot.bean.Status;
import com.wanhive.iot.bean.User;
import com.wanhive.iot.dao.UserDao;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/authenticate")
@Api(value = "/authenticate")
@SwaggerDefinition(tags = {
		@Tag(name = "User authentication", description = "REST endpoint for authentication of registered users.") })
public class AuthenticationResource {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(@FormParam("email") String email, @FormParam("password") String password,
			@FormParam("captcha") String captcha) {
		try {
			if (IotApplication.verifyCaptcha(captcha)) {
				return Response.ok(UserDao.verifyUser(email, password)).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity(new Status("error", "request denied"))
						.build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(new Status("error", "invalid request")).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(User user) {
		try {
			if (IotApplication.verifyCaptcha(user.getCaptcha())) {
				return Response.ok(UserDao.verifyUser(user.getEmail(), user.getPassword())).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity(new Status("error", "request denied"))
						.build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(new Status("error", "invalid request")).build();
		}
	}
}

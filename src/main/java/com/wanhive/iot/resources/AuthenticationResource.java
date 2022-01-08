package com.wanhive.iot.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wanhive.iot.WebConsole;
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
			@FormParam("captcha") String captcha) throws Exception {
		if (WebConsole.verifyCaptcha(captcha)) {
			return Response.ok(UserDao.verifyUser(email, password)).build();
		} else {
			throw new ForbiddenException();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(User user) throws Exception {
		if (WebConsole.verifyCaptcha(user.getCaptcha())) {
			return Response.ok(UserDao.verifyUser(user.getEmail(), user.getPassword())).build();
		} else {
			throw new ForbiddenException();
		}
	}
}

package com.wanhive.iot.resources;

import java.security.Principal;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.wanhive.iot.Constants;
import com.wanhive.iot.IotApplication;
import com.wanhive.iot.bean.Challenge;
import com.wanhive.iot.bean.User;
import com.wanhive.iot.dao.UserDao;
import com.wanhive.iot.util.Secured;
import com.wanhive.iot.util.StatusMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/user")
@Api(value = "/user")
@SwaggerDefinition(tags = {
		@Tag(name = "User management", description = "REST endpoint for the management of user accounts.") })
public class UserResource {
	@Context
	private SecurityContext securityContext;

	private long getUserUid() {
		Principal principal = securityContext.getUserPrincipal();
		return Long.parseLong(principal.getName());
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@FormParam("alias") String alias, @FormParam("email") String email,
			@FormParam("captcha") String captcha) throws Exception {
		if (Constants.isSignUpAllowed() && IotApplication.verifyCaptcha(captcha)) {
			UserDao.create(alias, email);
			return Response.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User user) throws Exception {
		if (Constants.isSignUpAllowed() && IotApplication.verifyCaptcha(user.getCaptcha())) {
			UserDao.create(user.getAlias(), user.getEmail());
			return Response.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@POST
	@Path("activate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activate(@FormParam("context") String context, @FormParam("challenge") String challenge,
			@FormParam("secret") String secret, @FormParam("captcha") String captcha) throws Exception {
		if (IotApplication.verifyCaptcha(captcha)) {
			UserDao.activate(context, challenge, secret);
			return Response.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@POST
	@Path("activate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activate(Challenge challenge) throws Exception {
		if (IotApplication.verifyCaptcha(challenge.getCaptcha())) {
			UserDao.activate(challenge.getContext(), challenge.getChallenge(), challenge.getSecret());
			return Response.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@GET
	@Path("challenge")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChallenge(@QueryParam("email") String email, @QueryParam("captcha") String captcha)
			throws Exception {
		if (IotApplication.verifyCaptcha(captcha)) {
			UserDao.generateChallenge(email);
			return Response.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@POST
	@Secured
	@Path("changepassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@FormParam("oldPassword") String oldPassword, @FormParam("password") String password)
			throws Exception {
		UserDao.changePassword(getUserUid(), oldPassword, password);
		return Response.ok().build();
	}

	@POST
	@Secured
	@Path("changepassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(User user) throws Exception {
		UserDao.changePassword(getUserUid(), user.getOldPassword(), user.getPassword());
		return Response.ok().build();
	}

	@DELETE
	@Path("token")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteToken() throws Exception {
		UserDao.removeToken(getUserUid());
		return Response.ok(StatusMessage.DELETED).build();
	}
}

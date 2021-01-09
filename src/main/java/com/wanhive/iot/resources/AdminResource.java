package com.wanhive.iot.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wanhive.iot.bean.User;
import com.wanhive.iot.dao.DomainDao;
import com.wanhive.iot.dao.ThingDao;
import com.wanhive.iot.dao.UserDao;
import com.wanhive.iot.util.Role;
import com.wanhive.iot.util.Secured;
import com.wanhive.iot.util.StatusMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/admin")
@Api(value = "/admin")
@SwaggerDefinition(tags = {
		@Tag(name = "Application administration", description = "REST endpoint for administration of application and users.") })
public class AdminResource {
	@GET
	@Path("user")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@DefaultValue("256") @QueryParam("limit") long limit,
			@DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("uid") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("type") int type, @DefaultValue("-1") @QueryParam("status") int status)
			throws Exception {
		return Response.ok(UserDao.list(limit, offset, order, orderBy, type, status)).build();
	}

	@GET
	@Path("user/search")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsers(@QueryParam("keyword") String keyword,
			@DefaultValue("256") @QueryParam("limit") long limit, @DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("uid") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("type") int type, @DefaultValue("-1") @QueryParam("status") int status)
			throws Exception {
		return Response.ok(UserDao.search(keyword, limit, offset, order, orderBy, type, status)).build();
	}

	@GET
	@Path("user/count")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response countUsers() throws Exception {
		return Response.ok(UserDao.count()).build();
	}

	@GET
	@Path("user/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserInfo(@PathParam("uid") long uid) throws Exception {
		return Response.ok(UserDao.info(uid)).build();
	}

	@POST
	@Path("user")
	@Secured({ Role.ADMINISTRATOR })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(@FormParam("alias") String alias, @FormParam("email") String email) throws Exception {
		return Response.ok(UserDao.create(alias, email)).build();
	}

	@POST
	@Path("user")
	@Secured({ Role.ADMINISTRATOR })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user) throws Exception {
		return Response.ok(UserDao.create(user.getAlias(), user.getEmail())).build();
	}

	@PUT
	@Path("user/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("uid") long uid, @FormParam("alias") String alias,
			@FormParam("password") String password, @DefaultValue("-1") @FormParam("type") int type,
			@DefaultValue("-1") @FormParam("status") int status, @DefaultValue("-1") @FormParam("flag") int flag)
			throws Exception {
		UserDao.update(uid, alias, password, type, status, flag);
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@PUT
	@Path("user/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("uid") long uid, User user) throws Exception {
		UserDao.update(uid, user.getAlias(), user.getPassword(), user.getType(), user.getStatus(), user.getFlag());
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@DELETE
	@Path("user/token/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteToken(@PathParam("uid") long uid) throws Exception {
		UserDao.removeToken(uid);
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Path("user/tokens")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response purgeTokens() throws Exception {
		UserDao.purgeTokens();
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Path("user/domains/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response purgeDomains(@PathParam("uid") long uid) throws Exception {
		DomainDao.purge(uid);
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Path("user/things/{uid}")
	@Secured({ Role.ADMINISTRATOR })
	@Produces(MediaType.APPLICATION_JSON)
	public Response purgeThings(@PathParam("uid") long uid) throws Exception {
		ThingDao.purge(uid);
		return Response.ok(StatusMessage.DELETED).build();
	}
}

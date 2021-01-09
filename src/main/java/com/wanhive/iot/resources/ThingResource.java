package com.wanhive.iot.resources;

import java.security.Principal;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.wanhive.iot.bean.Thing;
import com.wanhive.iot.dao.ThingDao;
import com.wanhive.iot.util.Secured;
import com.wanhive.iot.util.StatusMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import com.wanhive.iot.bean.Password;

@Path("/thing")
@Api(value = "/thing")
@SwaggerDefinition(tags = {
		@Tag(name = "Endpoint management", description = "REST endpoint for management of IoT endpoints.") })
public class ThingResource {
	@Context
	private SecurityContext securityContext;

	private long getUserUid() {
		Principal principal = securityContext.getUserPrincipal();
		return Long.parseLong(principal.getName());
	}

	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("domainUid") long domainUid, @DefaultValue("256") @QueryParam("limit") long limit,
			@DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("createdon") @QueryParam("orderBy") String orderBy) throws Exception {
		return Response.ok(ThingDao.list(getUserUid(), domainUid, limit, offset, order, orderBy)).build();
	}

	@GET
	@Path("search")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("domainUid") long domainUid, @QueryParam("keyword") String keyword,
			@DefaultValue("256") @QueryParam("limit") long limit, @DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("uid") @QueryParam("orderBy") String orderBy) throws Exception {
		return Response.ok(ThingDao.search(getUserUid(), domainUid, keyword, limit, offset, order, orderBy)).build();
	}

	@GET
	@Path("count")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response count() throws Exception {
		return Response.ok(ThingDao.count(getUserUid())).build();
	}

	@GET
	@Path("count/{domainUid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response count(@PathParam("domainUid") long domainUid) throws Exception {
		return Response.ok(ThingDao.count(getUserUid(), domainUid)).build();
	}

	@GET
	@Path("{uid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uid") long uid) throws Exception {
		return Response.ok(ThingDao.info(getUserUid(), uid)).build();
	}

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@FormParam("domainUid") long domainUid, @FormParam("name") String name,
			@DefaultValue("-1") @FormParam("type") int type) throws Exception {
		return Response.ok(ThingDao.create(getUserUid(), domainUid, name, type)).build();
	}

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Thing thing) throws Exception {
		return Response.ok(ThingDao.create(getUserUid(), thing.getDomainUid(), thing.getName(), thing.getType()))
				.build();
	}

	@PUT
	@Path("{uid}")
	@Secured
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uid") long uid, @FormParam("name") String name,
			@DefaultValue("-1") @FormParam("type") int type, @FormParam("salt") String salt,
			@FormParam("verifier") String verifier) throws Exception {
		ThingDao.update(getUserUid(), uid, name, type, salt, verifier);
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@PUT
	@Path("{uid}")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uid") long uid, Thing thing) throws Exception {
		ThingDao.update(getUserUid(), uid, thing.getName(), thing.getType(), thing.getSalt(), thing.getVerifier());
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@PUT
	@Path("{uid}/verifier")
	@Secured
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response computeVerifier(@PathParam("uid") String uid, @FormParam("rounds") int rounds,
			@FormParam("password") String password) throws Exception {
		ThingDao.updateVerifier(getUserUid(), uid, rounds, password);
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@PUT
	@Path("{uid}/verifier")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response computeVerifier(@PathParam("uid") String uid, Password password) throws Exception {
		ThingDao.updateVerifier(getUserUid(), uid, password.getRounds(), password.getPassword());
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@DELETE
	@Path("{uid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uid") long uid) throws Exception {
		ThingDao.delete(getUserUid(), uid);
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response purge() throws Exception {
		ThingDao.purge(getUserUid());
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Path("domain/{domainUid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response purge(@PathParam("domainUid") long domainUid) throws Exception {
		ThingDao.purge(getUserUid(), domainUid);
		return Response.ok(StatusMessage.DELETED).build();
	}
}

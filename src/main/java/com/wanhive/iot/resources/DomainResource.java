package com.wanhive.iot.resources;

import java.security.Principal;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.wanhive.iot.bean.Domain;
import com.wanhive.iot.dao.DomainDao;
import com.wanhive.iot.util.Secured;
import com.wanhive.iot.util.StatusMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/domain")
@Api(value = "/domain")
@SwaggerDefinition(tags = { @Tag(name = "Domain registry", description = "REST endpoint for management of domains.") })
public class DomainResource {
	@Context
	private SecurityContext securityContext;

	private long getUserUid() {
		Principal principal = securityContext.getUserPrincipal();
		return Long.parseLong(principal.getName());
	}

	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@DefaultValue("256") @QueryParam("limit") long limit,
			@DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("uid") @QueryParam("orderBy") String orderBy) throws Exception {
		return Response.ok(DomainDao.list(getUserUid(), limit, offset, order, orderBy)).build();
	}

	@GET
	@Path("search")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("keyword") String keyword, @DefaultValue("256") @QueryParam("limit") long limit,
			@DefaultValue("0") @QueryParam("offset") long offset,
			@DefaultValue("desc") @QueryParam("order") String order,
			@DefaultValue("uid") @QueryParam("orderBy") String orderBy) throws Exception {
		return Response.ok(DomainDao.search(getUserUid(), keyword, limit, offset, order, orderBy)).build();
	}

	@GET
	@Path("count")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response count() throws Exception {
		return Response.ok(DomainDao.count(getUserUid())).build();
	}

	@GET
	@Path("{uid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uid") long uid) throws Exception {
		return Response.ok(DomainDao.info(getUserUid(), uid)).build();
	}

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@FormParam("name") String name, @FormParam("description") String description)
			throws Exception {
		return Response.ok(DomainDao.create(getUserUid(), name, description)).build();
	}

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Domain domain) throws Exception {
		return Response.ok(DomainDao.create(getUserUid(), domain.getName(), domain.getDescription())).build();
	}

	@PUT
	@Path("{uid}")
	@Secured
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uid") long uid, @FormParam("name") String name,
			@FormParam("description") String description) throws Exception {
		DomainDao.update(getUserUid(), uid, name, description);
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@PUT
	@Path("{uid}")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uid") long uid, Domain domain) throws Exception {
		DomainDao.update(getUserUid(), uid, domain.getName(), domain.getDescription());
		return Response.ok(StatusMessage.UPDATED).build();
	}

	@DELETE
	@Path("{uid}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uid") long uid) throws Exception {
		DomainDao.delete(getUserUid(), uid);
		return Response.ok(StatusMessage.DELETED).build();
	}

	@DELETE
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response purge() throws Exception {
		DomainDao.purge(getUserUid());
		return Response.ok(StatusMessage.DELETED).build();
	}
}

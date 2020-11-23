package com.wanhive.iot;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wanhive.iot.bean.Status;

@Provider
public class UncaughtException extends Throwable implements ExceptionMapper<Throwable> {
	private static final long serialVersionUID = 1L;

	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public Response toResponse(Throwable error) {
		return Response.status(500).entity(new Status("error", "Something bad happened")).build();
	}
}

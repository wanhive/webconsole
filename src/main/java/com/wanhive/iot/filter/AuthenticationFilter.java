package com.wanhive.iot.filter;

import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.wanhive.iot.bean.Status;
import com.wanhive.iot.bean.User;
import com.wanhive.iot.dao.UserDao;
import com.wanhive.iot.util.Role;
import com.wanhive.iot.util.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	@Context
	private UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		try {
			// Retrieve the HTTP Authorization header from the request
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				throw new NotAuthorizedException("Authorization header missing");
			}

			// Extract and validate the token
			String token = authorizationHeader.substring("Bearer".length()).trim();
			final User subject = UserDao.verifyToken(token);
			requestContext.setSecurityContext(new SecurityContext() {
				@Override
				public Principal getUserPrincipal() {

					return new Principal() {

						@Override
						public String getName() {
							return Long.toString(subject.getUid());
						}
					};
				}

				@Override
				public boolean isUserInRole(String role) {
					switch (Role.valueOf(role)) {
					case ADMINISTRATOR:
						return subject.getType() == Role.ADMINISTRATOR.getValue();
					case MAINTAINER:
						return subject.getType() == Role.MAINTAINER.getValue();
					case REPORTER:
						return subject.getType() == Role.REPORTER.getValue();
					case USER:
						return subject.getType() == Role.USER.getValue();
					default:
						return false;
					}
				}

				@Override
				public boolean isSecure() {
					return uriInfo.getAbsolutePath().toString().startsWith("https");
				}

				@Override
				public String getAuthenticationScheme() {
					return null;
				}
			});

		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity(new Status("error", "request denied")).build());
		}
	}
}
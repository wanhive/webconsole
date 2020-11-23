package com.wanhive.iot.filter;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.wanhive.iot.util.Role;
import com.wanhive.iot.util.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
	@Context
	private ResourceInfo resourceInfo;

	private List<Role> getRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return null;
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return null;
			} else {
				Role[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

	private void verify(SecurityContext ctx, List<Role> allowed) throws Exception {
		if (allowed == null || allowed.size() == 0) {
			return;
		} else {
			for (Role role : allowed) {
				if (ctx.isUserInRole(role.name())) {
					return;
				}
			}

			throw new Exception("Forbidden");
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		try {
			List<Role> roles = getRoles(resourceInfo.getResourceMethod());
			if (roles != null && !roles.isEmpty()) {
				verify(requestContext.getSecurityContext(), roles);
			} else {
				roles = getRoles(resourceInfo.getResourceClass());
				verify(requestContext.getSecurityContext(), roles);
			}
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		}
	}

}

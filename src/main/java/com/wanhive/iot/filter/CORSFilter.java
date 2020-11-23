package com.wanhive.iot.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import com.wanhive.iot.Constants;

public class CORSFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if(Constants.allowCORS()) {
			response.getHeaders().add("Access-Control-Allow-Origin", "*");
	        response.getHeaders().add("Access-Control-Allow-Headers",
	                "x-requested-with, origin, content-type, accept, authorization");
	        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
	        response.getHeaders().add("Access-Control-Allow-Methods",
	                "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD");
		} else {
			//CORS has been disabled
		}
		
	}

}

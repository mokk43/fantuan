package com.ozhou.fantuan.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ozhou.fantuan.resource.Dto.AuthenticationResponseDto;
import com.ozhou.fantuan.resource.Dto.LoginDto;
import com.ozhou.filter.AuthenticationFilter;

@Path("/auth")
public class AuthenticationResource {

  @Context
  private HttpServletRequest httpRequest;

  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  public AuthenticationResponseDto login(LoginDto loginDto) {
    
    String user = loginDto.getUsername();
    String password = loginDto.getPassword();

    //boolean validLogin = processEngine.getIdentityService().checkPassword(user, password);
    boolean validLogin = true;
    if (validLogin) {
      httpRequest.getSession(true).setAttribute(AuthenticationFilter.AUTH_USER, user);
    }

    return new AuthenticationResponseDto(validLogin, user);
  }

  @GET
  @Path("logout")
  public String logout() {
    httpRequest.getSession(true).setAttribute(AuthenticationFilter.AUTH_USER, null);

    return "logged out";
  }

  @GET
  @Path("user")
  @Produces(MediaType.APPLICATION_JSON)
  public String getCurrentUser() {
    return (String) httpRequest.getSession().getAttribute(AuthenticationFilter.AUTH_USER);
  }

}

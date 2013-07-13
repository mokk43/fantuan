package com.ozhou.fantuan.resource;

import java.util.List;

import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.ozhou.fantuan.model.Account;
import com.ozhou.fantuan.model.AccountEntry;
import com.ozhou.fantuan.model.dao.AccountDao;
import com.ozhou.fantuan.resource.dto.AccountDto;
import com.ozhou.fantuan.resource.dto.AccountEntryDto;
import com.ozhou.utils.DtoBoConverter;

@Path("/accounts")
public class AccountResource {

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private DtoBoConverter converter;
	
	@GET
	@Path("/")
	@Produces({"application/json"})
	public Response getAccounts() {
		List<Account> accounts = accountDao.getAll();
		List<AccountDto> dtos = converter.getMappingFacade().mapAsList(accounts, AccountDto.class);
		return Response.status(200).entity(dtos).build();
	}
	
	@GET
	@Path("/{user}")
	@Produces({"application/json"})
	public Response getAccount(@PathParam("user") @Min(3) String user) {
		Account account = accountDao.get(user);
		if (account == null)
			return Response.status(404).build();
		
		AccountDto dto = converter.getMappingFacade().map(account, AccountDto.class);
		return Response.status(200).entity(dto).build();
	}
	
	@GET
	@Path("/{user}/entry")
	@Produces({"application/json"})
	public Response getAccountEntries(@PathParam("user") String user, 
			@QueryParam("start") int start, @QueryParam("pageSize") int pageSize) {
		List<AccountEntry> entries = accountDao.getAccountEntryByUser(user, start, pageSize);
		List<AccountEntryDto> dtos = converter.getMappingFacade().mapAsList(entries, AccountEntryDto.class);
		return Response.status(200).entity(dtos).build();
	}
	
	@GET
	@Path("/{user}/entry/count")
	@Produces({"application/json"})
	public Response getAccountEntriesCount(@PathParam("user") String user) {
		Long count = accountDao.getAccountEntryByUserCount(user);
		return Response.status(200).entity("{\"count\": " + count + "}").build();
	}
	
	@POST
	@Path("/clear-cache")
	@Produces({"application/json"})
	public Response clearCache() {
		accountDao.clearCache();
		return Response.ok().build();
	}
}

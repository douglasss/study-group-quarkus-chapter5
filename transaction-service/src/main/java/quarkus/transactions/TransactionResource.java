package quarkus.transactions;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/transactions")
public class TransactionResource {

    @Inject
    @RestClient
    AccountService accountService;

    @POST
    @Path("/{accountNumber}")
    public Response newTransaction(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        accountService.transact(accountNumber, amount);
        return Response.ok().build();
    }

}

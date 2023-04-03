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

    @Inject //  To inject the CDI bean for the interface, it is necessary to explicitly use @Inject.
    @RestClient // CDI qualifier telling Quarkus to inject a type-safe REST client bean matching the interface
    AccountService accountService; // The REST client interface representing the external service

    @POST
    @Path("/{accountNumber}")
    public Response newTransaction(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        accountService.transact(accountNumber, amount); // Calls the external service method
        return Response.ok().build();
    }

}

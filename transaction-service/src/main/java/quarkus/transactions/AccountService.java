package quarkus.transactions;

import java.math.BigDecimal;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/accounts") // Defines the path of the service, excluding the base URL portion
@RegisterRestClient(configKey = "account-service") // Indicates that the interface should have a CDI bean created that can be injected into classes
@Produces(MediaType.APPLICATION_JSON) // Sets all methods of the service to return JSON
public interface AccountService {

    // Method for retrieving the account balance, with HTTP method and Path annotations
    @GET
    @Path("/{accountNumber}/balance")
    BigDecimal getBalance(
            @PathParam("accountNumber")
            Long accountNumber);

    // Method for transacting on an account, with HTTP method and Path annotations
    @POST
    @Path("/{accountNumber}/transaction")
    void transact(@PathParam("accountNumber") Long accountNumber, BigDecimal amount);

    @POST
    @Path("{accountNumber}/transaction")
    CompletionStage<Void> transactAsync(
            @PathParam("accountNumber")
            Long accountNumber,
            BigDecimal amount);

}

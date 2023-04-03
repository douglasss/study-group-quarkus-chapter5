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

// Same as 'AccountService', only removing the @RegisterRestClient annotation.
// The same interface could be used for CDI and programmatic API
// But it’s important to show that @RegisterRestClient is not required for programmatic API usage.
@Path("/accounts")
@RegisterRestClient(configKey = "account-service")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountServiceProgrammatic {

    @GET
    @Path("/{accountNumber}/balance")
    BigDecimal getBalance(
            @PathParam("accountNumber")
            Long accountNumber);

    @POST
    @Path("/{accountNumber}/transaction")
    void transact(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount);

    @POST
    @Path("{accountNumber}/transaction")
    CompletionStage<Void> transactAsync(
            @PathParam("accountNumber")
            Long
                    accountNumber, BigDecimal amount);

}

package quarkus.transactions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/accounts") // Defines the path of the service, excluding the base URL portion
@RegisterRestClient(configKey = "account-service") // Indicates that the interface should have a CDI bean created that can be injected into classes
@ClientHeaderParam(name = "class-level-param", value = "AccountService interface") // Adds class-level-param to the outgoing HTTP request header. Adding it on the interface means all methods will have the header added.
//Indicates the default ClientHeadersFactory should be used. The default factory will propagate any
//headers from an inbound JAX-RS request onto the outbound client request, where the headers are
//added as a comma-separated list into the configuration key named
//org.eclipse.microprofile.rest.client.propagateHeaders
@RegisterClientHeaders
@Produces(MediaType.APPLICATION_JSON) // Sets all methods of the service to return JSON
@RegisterProvider(AccountRequestFilter.class)
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
    Map<String, List<String>> transact(@PathParam("accountNumber") Long accountNumber, BigDecimal amount);

    @POST
    @Path("{accountNumber}/transaction")
    @ClientHeaderParam(name = "method-level-param", value="{generateValue}")
    CompletionStage<Map<String, List<String>>> transactAsync(
            @PathParam("accountNumber")
            Long accountNumber,
            BigDecimal amount);

    default String generateValue() {
        return "Value generated in method for async call";
    }

}

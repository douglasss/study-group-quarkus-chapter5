package quarkus.transactions;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/transactions")
public class TransactionResource {

    // Injects a value for the configuration key account.service, defaulting it to http://localhost:8080 if it’s not found
    @ConfigProperty(name = "account.service", defaultValue = "http://localhost:8080")
    String accountServiceUrl;

    @Inject //  To inject the CDI bean for the interface, it is necessary to explicitly use @Inject.
    @RestClient // CDI qualifier telling Quarkus to inject a type-safe REST client bean matching the interface
    AccountService accountService; // The REST client interface representing the external service

    @POST
    @Path("/{accountNumber}")
    public Map<String, List<String>> newTransaction(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        try {
            return accountService.transact(accountNumber, amount); // Calls the external service method
        } catch (Exception t) {
            t.printStackTrace();
            Map<String, List<String>> response = new HashMap<>();
            response.put("EXCEPTION - " + t.getClass(),
                    Collections.singletonList(t.getMessage()));
            return response;
        }
    }

    @POST
    @Path("/api/{accountNumber}") // Adds the new programmatic API to the /transactions/api/ URL path
    public Response newTransactionWithApi(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) throws MalformedURLException {

        AccountServiceProgrammatic accountServiceProgrammatic = RestClientBuilder.newBuilder() // Uses RestClientBuilder to create a builder instance for setting features programmatically
                .baseUrl(new URL(accountServiceUrl)) // Sets the URL for any requests with the REST client, which is equivalent to baseUrl on @RegisterRestClient. Uses the configuration value of account.service to create a new URL
                .connectTimeout(500, TimeUnit.MILLISECONDS) // The maximum amount of wait time allowed when connecting to an external service
                .readTimeout(1200, TimeUnit.MILLISECONDS) // How long to wait for a response before triggering an exception
                .build(AccountServiceProgrammatic.class); // Builds a proxy of the AccountServiceProgrammatic interface for calling the external service

        accountServiceProgrammatic.transact(accountNumber, amount); // Calls the service in the same way as done previously with a CDI bean
        return Response.ok().build();
    }

    @POST
    @Path("/async/{accountNumber}") // Uses a different URL path for the asynchronous version. Return type is now CompletionStage<Void> instead of Response.
    public CompletionStage<Map<String, List<String>>> newTransactionAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        return accountService.transactAsync(accountNumber, amount); // Method body modified to return the result of REST client call
    }

    @POST
    @Path("/api/async/{accountNumber}")
    public CompletionStage<Void> newTransactionWithApiAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) throws MalformedURLException {

        AccountServiceProgrammatic accountServiceProgrammatic =
                RestClientBuilder.newBuilder()
                        .baseUrl(new URL(accountServiceUrl))
                        .build(AccountServiceProgrammatic.class);

        return accountServiceProgrammatic.transactAsync(accountNumber,
                amount); // As with the newTransactionAsync method, instead of returning a Response to indicate everything is OK, returns the CompletionStage returned from the REST client call instead
    }

}

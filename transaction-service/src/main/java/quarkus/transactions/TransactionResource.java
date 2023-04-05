package quarkus.transactions;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/transactions")
public class TransactionResource {

    @Inject
    @RestClient
    AccountService accountService;

    @POST
    @Path("/{accountNumber}")
    public Map<String, List<String>> newTransaction(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        try {
            return accountService.transact(accountNumber, amount);
        } catch (Exception e) {
            return Collections.singletonMap(
                    "Exception - " + e.getClass(),
                    Collections.singletonList(e.getMessage())
            );
        }
    }

    @POST
    @Path("/api/{accountNumber}")
    public Response newTransactionApi(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) throws MalformedURLException {

        AccountServiceProgrammatic service = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://localhost:8080"))
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(1200, TimeUnit.MILLISECONDS)
                .build(AccountServiceProgrammatic.class);

        service.transact(accountNumber, amount);

        return Response.ok().build();
    }

    @POST
    @Path("/async/{accountNumber}")
    public CompletionStage<Map<String, List<String>>> newTransactionAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        return accountService.transactAsync(accountNumber, amount);
    }

    @POST
    @Path("/api/async/{accountNumber}")
    public CompletionStage<Void> newTransactionApiAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) throws MalformedURLException {

        AccountServiceProgrammatic service = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://localhost:8080"))
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(1200, TimeUnit.MILLISECONDS)
                .build(AccountServiceProgrammatic.class);

        return service.transactAsync(accountNumber, amount);
    }

}

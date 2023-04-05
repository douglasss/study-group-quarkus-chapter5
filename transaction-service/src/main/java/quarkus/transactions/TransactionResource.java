package quarkus.transactions;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
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
    public Response newTransaction(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount) {
        accountService.transact(accountNumber, amount);
        return Response.ok().build();
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

}

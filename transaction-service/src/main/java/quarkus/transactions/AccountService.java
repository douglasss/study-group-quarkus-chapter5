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

@Path("/accounts")
@RegisterRestClient(configKey = "account-service")
@Produces(MediaType.APPLICATION_JSON)
@ClientHeaderParam(name = "class-level-param", value = "AccountService interface")
@RegisterClientHeaders
@RegisterProvider(AccountRequestFilter.class)
public interface AccountService {

    @GET
    @Path("/{accountNumber}/balance")
    BigDecimal getBalance(
            @PathParam("accountNumber")
            Long accountNumber);

    @POST
    @Path("/{accountNumber}/transaction")
    Map<String, List<String>> transact(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount);

    @POST
    @Path("/{accountNumber}/transaction")
    @ClientHeaderParam(name = "method-level-param", value = "{generateValue}")
    CompletionStage<Map<String, List<String>>> transactAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount);

    default String generateValue() {
        return "Generated value";
    }
}

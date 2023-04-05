package quarkus.transactions;

import java.math.BigDecimal;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
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
    @Path("/{accountNumber}/transaction")
    CompletionStage<Void> transactAsync(
            @PathParam("accountNumber")
            Long accountNumber, BigDecimal amount);

}
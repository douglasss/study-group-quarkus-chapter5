package quarkus.transactions;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

// Implements QuarkusTestResourceLifecycleManager to respond to the start and stop events of the test
public class WiremockAccountService implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer; // Stores the WireMockServer instance to enable stopping it during test shutdow

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(); // Creates the WireMockServer, and starts it
        wireMockServer.start();

        // Provides a stub for responding to the HTTP GET method for retrieving an account balance.
        // Because it’s a mock server, the account number the server responds to needs to be hardcoded
        // and used in the request from a test.
        stubFor(get(urlEqualTo("/accounts/121212/balance"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("435.76")));

        // Creates another stub for responding to the HTTP POST method to create a transaction
        stubFor(post(urlEqualTo("/accounts/121212/transaction"))
                .willReturn(noContent()));

        // Sets an environment variable named account-service/mp-rest/url to the URL
        // of the WireMock server.
        // The variable name matches the expected name of the configuration key for
        // defining the URL.
        return Collections.singletonMap(
                "account-service/mp-rest/url",
                wireMockServer.baseUrl()
        );
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}

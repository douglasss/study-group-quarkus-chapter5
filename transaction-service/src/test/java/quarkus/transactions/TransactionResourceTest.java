package quarkus.transactions;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(WiremockAccountService.class) // Adds the life cycle manager for WireMock to the test
class TransactionResourceTest {

    @Test
    void testTransaction() {
        RestAssured.given()
                .body("142.12")
                .contentType(ContentType.JSON)
                .when()
                .post("/transactions/{accountNumber}", 121212) // Issues an HTTP POST request using the account number defined in the WireMock stub
                .then()
                .statusCode(200); // Verifies a response code of 200 is returned
    }

}

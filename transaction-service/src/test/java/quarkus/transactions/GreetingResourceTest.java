package quarkus.transactions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void test() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello RESTEasy"));
    }

}
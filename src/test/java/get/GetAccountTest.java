package get;

import baseMethod.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import ru.gb.backend.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Slf4j
public class GetAccountTest extends BaseTest {

    @Test
    public void getAccountInfoPositiveTest() {
        given()
                .spec(reqSpec)
                .when()
                .get(Endpoints.GET_ACCOUNT_REQUEST, BaseTest.username)
                .prettyPeek()
                .then()
                .spec(respSpec);
    }

    @Test
    public void getAccountInfoNegativeTest() {
        given()
                .spec(reqSpec)
                .when()
                .get(Endpoints.GET_ACCOUNT_REQUEST, "")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @Test
    public void getAccountInfoPositiveWithManyChecksTest() {
        given()
                .spec(reqSpec)
                .expect()
                .body(CoreMatchers.containsString(BaseTest.username))
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .get("/account/{id}", BaseTest.username)
                .then()
                .statusCode(200);
    }
}

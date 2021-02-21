package Get;

import BaseMethod.BaseTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class GetAccountTest extends BaseTest {
    @Test
    public void getAccountInfoPositiveTest() {
        given()
                .headers("Authorization", BaseTest.token)
                .when()
                .get("/account/{id}", BaseTest.username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    public void getImageInfoBrokenEndpointTest() {
        given()
                .headers("Authorization", BaseTest.token)
                .expect()
                .body("success", is(false))
                .when()
                .get("/image")
                .then()
                .statusCode(400);
    }

    @Test
    public void getImageParameterAnimationTest() {
        given()
                .headers("Authorization", BaseTest.token)
                .expect()
                .body("data.animated", is(true))
                .when()
                .get("/image/wymWu3D")
                .then()
                .statusCode(200);
    }

    @Test
    public void getImageParameterFavoriteTest() {
        given()
                .headers("Authorization", BaseTest.token)
                .expect()
                .body("data.favorite", is(true))
                .when()
                .get("/image/3a94fYN")
                .then()
                .statusCode(200);
    }

    @Test
    public void getAccountInfoPositiveWithManyChecksTest() {
        given()
                .headers("Authorization", BaseTest.token)
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

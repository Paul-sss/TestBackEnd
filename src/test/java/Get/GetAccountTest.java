package Get;

import BaseMethod.BaseTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gb.backend.dto.GetAccountResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Slf4j
public class GetAccountTest extends BaseTest {



    @Test
    public void getAccountInfoPositiveTest() {
        GetAccountResponse response = given()
                .when()
                .get("/account/{id}", BaseTest.username)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(GetAccountResponse.class);
        System.out.println(response.getStatus().toString());
        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getData().getUrl(),equalTo(username));


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

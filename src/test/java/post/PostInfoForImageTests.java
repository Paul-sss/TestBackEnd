package post;

import baseMethod.BaseTest;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gb.backend.Endpoints;

import static baseMethod.BaseTest.reqSpec;
import static baseMethod.BaseTest.respSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostInfoForImageTests extends BaseTest{

    private String uploadedImageId;
    private String deleteHashImg;
    private String URLImage = "http://saint-nectaire-fromage.fr/wp-content/uploads/2012/11/fruit-decoration.jpg";

    @BeforeEach
    void setUp () {
    uploadedImageId = given()
                .spec(reqSpec)
                .multiPart("image" ,URLImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(Endpoints.GET_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
}

    @Test
    void addTitleAndDescriptionTest() {
        given()
                .spec(reqSpec)
                .multiPart("title", "NEW TITLE")
                .multiPart("description", "NEW DESCRIPTION")
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is(true))
                .when()
                .post(Endpoints.GET_IMAGE_REQUEST + uploadedImageId)
                .prettyPeek()
                .then()
                .spec(respSpec);
    }

    @AfterEach
    @Step("Delete file after test")
    void tearDown() {
        getDeleteHashImage();
        given()
                .spec(reqSpec)
                .when()
                .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, deleteHashImg)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private void getDeleteHashImage() {
        deleteHashImg = given()
                .spec(reqSpec)
                .when()
                .get(Endpoints.GET_IMAGE_REQUEST + uploadedImageId)
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
}

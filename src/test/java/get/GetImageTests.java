package get;

import baseMethod.BaseTest;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gb.backend.Endpoints;

import java.io.File;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class GetImageTests extends BaseTest {
    private String uploadedImageId;
    private String animatedImgURL = "https://i.gifer.com/Czje.gif";

    @BeforeEach
     void setUp() {
        uploadedImageId = given()
                .spec(reqSpec)
                .multiPart("image" ,animatedImgURL)
                .when()
                .post(Endpoints.GET_IMAGE_REQUEST)
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    public void getImageInfoTest() {
        given()
                .spec(reqSpec)
                .expect()
                .body("success", is(true))
                .when()
                .get(Endpoints.GET_IMAGE_REQUEST+uploadedImageId)
                .then()
                .spec(respSpec);
    }

    @Test
    public void getImageInfoBrokenEndpointTest() {
        given()
                .spec(reqSpec)
                .expect()
                .body("success", is(false))
                .when()
                .get(Endpoints.GET_IMAGE_REQUEST)
                .then()
                .statusCode(400);
    }

    @Test
    public void getImageParameterAnimationTest() {
        given()
                .spec(reqSpec)
                .expect()
                .body("data.animated", is(true))
                .when()
                .get(Endpoints.GET_IMAGE_REQUEST+uploadedImageId)
                .then()
                .spec(respSpec);
    }

    @AfterEach
    @Step("Delete file after test")
    void tearDown() {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, uploadedImageId)
                    .then()
                    .spec(respSpec);
    }
}

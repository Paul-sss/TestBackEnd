package delete;

import baseMethod.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gb.backend.Endpoints;

import static io.restassured.RestAssured.given;


public class DeleteImageTests extends BaseTest {
    private String uploadedImageId;
    private String animatedImgURL = "https://i.gifer.com/Czje.gif";


    @BeforeEach
    void setUp () {
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
    void deleteImageWithAuthTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, uploadedImageId)
                .then()
                .spec(respSpec);
    }

    @Test
    void deleteImageWithoutAuthTest() {
        given()
                .spec(reqSpecWithoutAuth)
                .when()
                .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, uploadedImageId)
                .then()
                .statusCode(403)
                .statusLine("HTTP/1.1 403 Permission Denied");
    }

    @Test
    void deleteImageBrokenEndpointTest() {
        String ErrorLine = given()
                .spec(reqSpec)
                .when()
                .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, "")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        Assertions.assertEquals(ErrorLine, "An ID is required.");
    }
}

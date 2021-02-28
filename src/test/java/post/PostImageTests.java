package post;

import baseMethod.BaseTest;
import io.qameta.allure.Step;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gb.backend.Endpoints;
import ru.gb.backend.utils.FileEncodingUtils;

import java.io.File;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostImageTests extends BaseTest {

    String encodedImage;
    private String uploadedImageId;
    private String URLImage = "http://saint-nectaire-fromage.fr/wp-content/uploads/2012/11/fruit-decoration.jpg";

    @BeforeEach
    void setUp() {
        byte[] fileContent = FileEncodingUtils.getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    void uploadFileContentInBase64Test() {
        uploadedImageId = given()
                .spec(reqSpec)
                .multiPart("image" ,encodedImage)
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
                .getString("data.deletehash");
    }

    @Test
    void uploadFileUsingUrlTest() {
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
                .getString("data.deletehash");
    }

    @Test
    void uploadFileTest() {
        File inputFile = getFile("pineapple.jpg");
        uploadedImageId = given()
                .spec(reqSpec)
                .multiPart("image" ,inputFile)
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
                .getString("data.deletehash");
    }

    @Test
    void uploadTooLargeFileTest() {
        File inputFile = getFile("Heavy.jpg");
        given()
                .spec(reqSpec)
                .multiPart("image" ,inputFile)
                .expect()
                .body("success", is(false))
                .when()
                .post(Endpoints.GET_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    private File getFile(String s) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(s)).getFile());
    }

    @AfterEach
    @Step("Delete file after test")
    void tearDown() {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(Endpoints.DELETE_IMAGE_REQUEST, BaseTest.username, uploadedImageId)
                    .prettyPeek()
                    .then()
                    .spec(respSpec);
    }
}

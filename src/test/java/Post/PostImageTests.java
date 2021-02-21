package Post;

import BaseMethod.BaseTest;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostImageTests extends BaseTest {
    Logger LOGGER;

    String encodedImage;
    private String uploadedImageId;
    private String URLImage = "http://saint-nectaire-fromage.fr/wp-content/uploads/2012/11/fruit-decoration.jpg";

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    void uploadFileContentInBase64Test() {
        uploadedImageId = given()
                .headers("Authorization", BaseTest.token)
                .multiPart("image" ,encodedImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
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
                .headers("Authorization", BaseTest.token)
                .multiPart("image" ,URLImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void uploadFileTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("pineapple.jpg")).getFile());
        uploadedImageId = given()
                .headers("Authorization", BaseTest.token)
                .multiPart("image" ,inputFile)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void uploadTooLargeFileTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("Heavy.jpg")).getFile());
        given()
                .headers("Authorization", BaseTest.token)
                .multiPart("image" ,inputFile)
                .expect()
                .body("success", is(false))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @Test
    void addTitleAndDescriptionTest() {
        given()
                .headers("Authorization", BaseTest.token)
                .multiPart("title" ,"NEW TITLE")
                .multiPart("description", "NEW DESCRIPTION")
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is(true))
                .when()
                .post("/image/pr0V4oQ")
                .prettyPeek()
                .then()
                .statusCode(200);
    }




    @AfterEach
    @Step("Delete file after test")
    void tearDown() {
        try {
            given()
                    .headers("Authorization", BaseTest.token)
                    .when()
                    .delete("account/{username}/image/{deleteHash}", BaseTest.username, uploadedImageId)
                    .prettyPeek()
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getFileContentInBase64() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("pineapple.jpg")).getFile());
        byte[] fileContent = new byte[0];
        try {
            fileContent =   FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}

package baseMethod;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    static Properties prop = new Properties();
    public static String token;
    public static String username;
    static private Map<String, String> headers = new HashMap<>();
    public static ResponseSpecification respSpec;
    public static RequestSpecification reqSpec;
    public static RequestSpecification reqSpecWithoutAuth;

    @BeforeAll
    static void beforeAll() {
        loadProperties();

        username = prop.getProperty("username");
        token = prop.getProperty("token");


        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = prop.getProperty("base.url");
        RestAssured.filters(new AllureRestAssured());

        respSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.ANY)
                .build();

        reqSpecWithoutAuth = new RequestSpecBuilder()
                .addHeader("Authorization", "")
                .setAccept(ContentType.ANY)
                .build();
    }

    static void loadProperties() {
        try {
            InputStream properties = new FileInputStream("src\\test\\resources\\application.properties");
            prop.load(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

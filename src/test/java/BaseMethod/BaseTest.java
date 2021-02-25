package BaseMethod;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import ru.gb.backend.dto.GetAccountResponse;

import javax.naming.spi.ResolveResult;
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
    public static ResponseSpecification responseSpecification;
    public static RequestSpecification reqSpec;

    @BeforeAll
    static void beforeAll() {
        loadProperties();

        username = prop.getProperty("username");
        token = prop.getProperty("token");


        headers.put("Authorization", token);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = prop.getProperty("base.url");
        RestAssured.filters(new AllureRestAssured());

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Access-Control-Allow-Credentials", "true")
                .build();

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.ANY)
                .setContentType(ContentType.JSON)
                .build();

        //RestAssured.responseSpecification = responseSpecification;
        //RestAssured.requestSpecification = reqSpec;

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

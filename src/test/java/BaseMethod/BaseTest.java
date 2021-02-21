package BaseMethod;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
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

    @BeforeAll
    static void beforeAll() {
        loadProperties();

        username = prop.getProperty("username");
        token = prop.getProperty("token");


        headers.put("Authorization", token);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = prop.getProperty("base.url");
        RestAssured.filters(new AllureRestAssured());
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

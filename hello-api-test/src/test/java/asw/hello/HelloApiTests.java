package asw.hello;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class HelloApiTests {

	@Value("${hello.api.baseuri}") 
	private String apiBaseUri; 

	@Value("${hello.api.port}") 
	private int apiPort; 

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = apiBaseUri;
        RestAssured.port = apiPort;
//		RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
	}

	@Test 
	public void helloWorldApiTest() {
		when().
            get("/").
		then().
            statusCode(200).
            body(containsString("Hello")).
            body(containsString("world"));
	}

	@Test 
	public void helloLucaApiTest() {
		when().
            get("/hello/{name}", "Luca").
		then().
            statusCode(200).
            body(equalTo("Hello, Luca!"));
	}

}

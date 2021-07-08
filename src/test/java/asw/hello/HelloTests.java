package asw.hello;

import asw.hello.domain.HelloService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HelloServiceApplication.class)
public class HelloTests {

	@Autowired
	private HelloService helloService;

	@BeforeEach
	public void setupFixture() {
	}

	@Test 
	public void helloTest() {
    	assertEquals( "Hello, world!", helloService.hello() );
	}


}

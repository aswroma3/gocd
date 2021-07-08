package asw.hello.domain;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    /* Calcola il saluto Hello, world! */
	public String hello() {
		return "Hello, world!"; 
	}
	
    /* Calcola il saluto Hello, name! */
	public String hello(String name) {
		return "Hello, " + name + "!"; 
	}

}


package pl.java.scalatech;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EipBootApplication implements CommandLineRunner{
    
    
	public static void main(String[] args) throws IOException {	  
		SpringApplication.run(EipBootApplication.class, args);
		
	}
	@Autowired
    CamelContext camelContext;
	
	@Autowired
	ConsumerTemplate consumerTemplate;
	
	@Autowired
    ProducerTemplate producerTemplate;
	
	
    @Override
    public void run(String... args) throws Exception {
       // camelContext.findEips().entrySet().stream().forEach(entry -> log.info("key : {} , value : {}",entry.getKey(),entry.getValue()));
        
        producerTemplate.sendBody("direct:marshal", "przodownik");
        producerTemplate.sendBody("direct:unmarshal", "przodownik");
    }
}

package pl.java.scalatech;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EipBootApplication implements CommandLineRunner{
    
    
	public static void main(String[] args) {	  
		SpringApplication.run(EipBootApplication.class, args);
		
	}
	@Autowired
    CamelContext camelContext;
	
	@Autowired
	ConsumerTemplate consumerTemplate;
	
	@Autowired
    ProducerTemplate producerTemplate;
	
	@Bean
	@Profile("camelProfile")
    PropertiesComponent properties(){
       return new PropertiesComponent("classpath:camel.properties");
   }
	@Bean
	@Profile("camelProfile")
    CamelContextConfiguration contextConfiguration(PropertiesComponent properties) {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                log.info("before context : {}",context);
                
                context.addComponent("properties", properties);
                
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                log.info("after context : {}",camelContext);
                
            }
        };
    }
	
	
	
    @Override
    public void run(String... args) throws Exception {
       // camelContext.findEips().entrySet().stream().forEach(entry -> log.info("key : {} , value : {}",entry.getKey(),entry.getValue()));
        
    }
}

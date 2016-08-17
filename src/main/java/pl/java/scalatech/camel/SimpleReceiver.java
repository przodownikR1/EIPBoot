package pl.java.scalatech.camel;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleReceiver {

    
    public void receive(String body){
      log.info("+++++  {}",body);  
    }
}

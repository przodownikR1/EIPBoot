package pl.java.scalatech.camel.simpleFile;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
           from("file://Inbox/simple?move=processed").transform(simple("Approved: ${body}")).to("file://Outbox/simple");
        
    }

}

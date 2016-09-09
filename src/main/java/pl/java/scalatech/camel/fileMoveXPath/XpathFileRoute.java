package pl.java.scalatech.camel.fileMoveXPath;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("XpathAppend")
public class XpathFileRoute extends RouteBuilder {

    @Value("${end}")
    private String end;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        log.info("+++++++++++++++++  end : {}",end);
        log.info("+++++++++++++++++  start :");
        from("{{inbox.processed}}")
        .log(LoggingLevel.INFO, "xpath", "${file:name}")
        .setHeader(Exchange.FILE_NAME).constant("simple_artefactId.txt")
        .split(xpath("*/dependency")).streaming()
        .transform(xpath("*/artifactId/text()"))
        .setBody(simple("${body}"+"\\n"))
        
        .log(LoggingLevel.INFO,"xpath","${body}")
         .to("file://Outbox?fileExist=Append");

        // @formatter:on
    }

}
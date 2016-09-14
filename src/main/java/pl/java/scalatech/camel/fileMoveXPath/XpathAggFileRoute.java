package pl.java.scalatech.camel.fileMoveXPath;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class XpathAggFileRoute extends RouteBuilder {
    @Value("${end}")
    private String end;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        log.info("+++++++++++++++++  end : {}",end);
        from("{{inbox.processed}}")
        //.log(LoggingLevel.INFO, "xpath", "${file:name}")
        .setHeader(Exchange.FILE_NAME).constant("simple_artefactId.txt")
        .split(xpath("*/dependency"))
        .transform(xpath("*/artifactId/text()"))
        .aggregate(body(),new DependencyAgg()).completionTimeout(1000).eagerCheckCompletion()                
         .to("file://Outbox");

        // @formatter:on

    }

    class DependencyAgg implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            if(oldExchange.getIn().getBody()!= null){
             log.info(" ****  old  {}",oldExchange.getIn().getBody());
            }
            log.info(" ******  new  {} ",newExchange.getIn().getBody());
            oldExchange.getIn().setBody(newExchange.getIn().getBody() + " + " + newExchange.getIn().getBody());
            return oldExchange;
         
        }
    }

}

package pl.java.scalatech.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyDataFormatRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        log.info("+++++ MyDataFormatRouter");
        MyDataFormat dataFormat = new MyDataFormat("slawek_123");
        from("direct:marshal").marshal(dataFormat).process(exchange -> log.info("+++ masrhal : {}",exchange.getIn().getBody()));
        from("direct:unmarshal").unmarshal(dataFormat).process(exchange -> log.info("unmarshal {}",exchange.getIn().getBody()));
    }
}
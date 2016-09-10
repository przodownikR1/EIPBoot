package pl.java.scalatech.camel.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Component
@Profile("simple")
public class MyRouteBuilder extends RouteBuilder {
    
    @Override
    public void configure() {

        restConfiguration()
        .component("netty4-http")
        .bindingMode(RestBindingMode.off)
        .host("0.0.0.0")
        .contextPath("/")
        .port("10000");
        
        rest("/foo")
        .produces("text/plain")
        .get("/bar").route().transform().constant("GET /foo/bar").endRest()
        .post("/bar").route().transform().constant("POST /foo/bar").endRest()
        .put("/bar").route().transform().constant("PUT /foo/bar").endRest()
        .delete("/bar").route().transform().constant("DELETE /foo/bar").endRest();
    }

}
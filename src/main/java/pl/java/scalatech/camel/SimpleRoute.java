package pl.java.scalatech.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends RouteBuilder{
    
    @Autowired
    TimerProcessor timerProcessor;

    @Override
    public void configure() throws Exception {
        
        tracer();
        
        from("timer://foo?fixedRate=true&period=15000").routeId("simpleRoute").setBody(simple("fired at ${header.firedTime} ")).process(timerProcessor).log(">>> ${body}").bean(SimpleReceiver.class);
        //simple("Helloworld timer fired at ${header.firedTime}"))
    }

    private void tracer() {
        Tracer tracer = new Tracer();
        tracer.getDefaultTraceFormatter().setShowBreadCrumb(false);
        tracer.getDefaultTraceFormatter().setShowNode(true);
        tracer.getDefaultTraceFormatter().setShowBody(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(true);
        tracer.getDefaultTraceFormatter().setShowProperties(true);

        getContext().addInterceptStrategy(tracer);
    }

}

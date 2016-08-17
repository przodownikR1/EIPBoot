package pl.java.scalatech.camel;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
@Component
public class TimerProcessor implements Processor {

    @Override
    public void process(Exchange msg) throws Exception {

        Date firedTime = msg.getProperty(Exchange.TIMER_FIRED_TIME, java.util.Date.class);
        int eventCount = msg.getProperty(Exchange.TIMER_COUNTER, Integer.class);
        String timerName = msg.getProperty(Exchange.TIMER_NAME, String.class);
        int period = msg.getProperty(Exchange.TIMER_PERIOD, Integer.class);
        Date time = msg.getProperty(Exchange.TIMER_TIME, Date.class);

        msg.getOut().setBody(
                "Exchange Properties: name: " + timerName + " time: " + time + " period: " + period + " firedAt: " + firedTime + " counter: " + eventCount);

    }
}

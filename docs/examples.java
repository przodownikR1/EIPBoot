


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event implements Serializable {

    private String hostname;
    private LocalDateTime eventTime;
    private Long id;
}

@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class EventSimulator {

    private @NonNull JmsTemplate jmsTemplate;
    private @NonNull String destination;

    @Scheduled(fixedRate = 1000)
    public void simulate() {
        Random random = new Random();

        String hostname;
        switch (random.nextInt(3)) {
        case 0:
            hostname = "local1";
            break;
        case 1:
            hostname = "local2";
            break;
        default:
            hostname = "local3";
            break;
        }

        Event event = new Event(hostname, LocalDateTime.now(),random.nextInt(200));
        log.info("+++  send message {}", event);
        jmsTemplate.convertAndSend(destination, event);

    }
}
=====


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Body;
import org.apache.camel.Consume;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.beans.Event;
import pl.java.scalatech.beans.Event.EventBuilder;
import pl.java.scalatech.jms.EventSimulator;
import pl.java.scalatech.simple.bean.Consumer;
import pl.java.scalatech.simple.model.person.Person;
import pl.java.scalatech.spring_camel.eip.RecipientListBean;
import pl.java.scalatech.spring_camel.nbp.Pozycja;
import pl.java.scalatech.spring_camel.nbp.Tabela_kursow;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Pozycja {
    @XmlElement
    private String nazwa_waluty;
    @XmlElement
    private String przelicznik;
    @XmlElement
    private String kod_waluty;
    @XmlElement
    private String kurs_sredni;

}

@Data
@XmlRootElement(name = "tabela_kursow")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tabela_kursow {
    @XmlAttribute
    private String typ;
    @XmlAttribute
    private String uid;

    @XmlElement
    private String numer_tabeli;

    @XmlElement
    private Date data_publikacji;

    @XmlElement(name = "pozycja")
    private List<Pozycja> pozycje = Lists.newArrayList();

    public String getTyp() {
        return typ;
    }

}

----
JaxbDataFormat jxb = new JaxbDataFormat("pl.java.scalatech.nbp");

.split().tokenizeXML("pozycja").streaming().convertBodyTo(String.class)
----


public void customLoad(@Body InputStream inputStream, @Headers Map<String, Object> header) throws IOException {
    String text = null;
    try (final Reader reader = new InputStreamReader(inputStream)) {
        text = CharStreams.toString(reader);
    }
    log.info("{}", text);
}

from("stream:url?url=http://www.nbp.pl/kursy/xml/LastA.xml").to("seda:xmlProc");
from("seda:xmlProc").beanRef("fileContentReader").setId("READ_STREAM_ROUTE");


RecipientListBean rl = new RecipientListBean("file://outbox?fileName=${date:now:yyyyMMddHHmmss}.xml", "stream:out");
from("timer://nbp?fixedRate=true&delay=0&period=1800000").routeId("httpProcessingRoute").to("http://www.nbp.pl/kursy/xml/LastA.xml")
        .convertBodyTo(String.class).recipientList(method(rl, "route")).setId("HttpRoute");

public class RecipientListBean {

    private  List<String> uris;

    public RecipientListBean(String... uris) {
        this.uris = Arrays.asList(uris);
    }
    @Handler
    public List<String> route(Exchange exchange) {
        return uris;
    }

}

=========
@Component
@Slf4j
public class Consumer {
    static final String URI = "direct:test";
    @Consume(uri = URI)
    public void consume(String name) {
      log.info("consume message ---->  {}  ",name);
    }
}

===========
//transform(simple("Hello ${body}"))
//setBody(constant("Hello World, from the Department of Redundancy Department!"))
//transform(new SimpleExpression("${in.body}, Hello World !"+ LocalDate.now()))


====
.`idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(200))

===
.marshal().json(JsonLibrary.Jackson).

unmarshal().json(JsonLibrary.Jackson, Person.class).

===
from("file:src/data?charset=utf-8&doneFileName=${file:name}.done&move=.done&moveFailed=.error")



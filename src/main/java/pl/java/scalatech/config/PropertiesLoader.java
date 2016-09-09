package pl.java.scalatech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PropertiesLoader {

    @Configuration
    @PropertySources({ @PropertySource(value = { "classpath:camel.properties" })})
    static class PropertiesLoaderForDev {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            log.info("+++                         propertySource camel -> prop profile launch");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}

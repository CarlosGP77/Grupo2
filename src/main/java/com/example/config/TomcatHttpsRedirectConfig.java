package com.example.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.tomcat.TomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatHttpsRedirectConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatWebServerFactory> servletContainer() {
        return factory -> {
            factory.addAdditionalConnectors(httpConnector(80));
            factory.addAdditionalConnectors(httpConnector(8080));
        };
    }

    private Connector httpConnector(int port) {
        Connector connector = new Connector(TomcatWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(port);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }
}


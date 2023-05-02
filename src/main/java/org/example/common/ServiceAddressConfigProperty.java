package org.example.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "service.address")
public record ServiceAddressConfigProperty(String auth, String bill, String delivery) {

}

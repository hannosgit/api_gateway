package org.example.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "service")
public record ServiceAddressConfigProperty(String address) {
}

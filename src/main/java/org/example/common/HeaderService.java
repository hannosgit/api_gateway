package org.example.common;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class HeaderService {

    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public ApiCredentials parseAuthorizationHeader(String authorization) {
        final String basic = authorization.substring("Basic ".length()).trim();
        final byte[] credDecoded = DECODER.decode(basic);
        final String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        final String[] split = credentials.split(":");
        final String username = split[0];
        final String password = split[1];

        return new ApiCredentials(username, password);
    }
}

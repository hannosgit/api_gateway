package org.example.config;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class JettyConfiguration implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Override
    public void customize(JettyServletWebServerFactory factory) {
        // customize your thread pool here
        QueuedThreadPool qtp = new QueuedThreadPool(1_000_000, 1);
        qtp.setName("jettyThreadPool");
        qtp.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());

        factory.setThreadPool(qtp);
    }
}

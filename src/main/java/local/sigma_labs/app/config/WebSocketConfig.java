package local.sigma_labs.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.List;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${allowed.origins}")
    private String theAllowedOrigins;
    private static final int SIZE_LIMIT = 16384 * 16384;
    private final String[] brokerDestinationPrefixes = {"/topic", "/queue"};
    private final String[] applicationDestinationPrefixes = {"/app", "/api"};
    private final String[] StompRegistryEndpoints = {"/private-channel-text",
                                                    "/private-channel-image",
                                                    "/private-channel-audio",
                                                    "/public-channel"};
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(brokerDestinationPrefixes);
        config.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(StompRegistryEndpoints)
                .setAllowedOrigins(theAllowedOrigins)
                .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new ByteArrayMessageConverter());
        return false;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(SIZE_LIMIT)
                .setSendBufferSizeLimit(SIZE_LIMIT)
                .setSendTimeLimit(SIZE_LIMIT);
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(SIZE_LIMIT);
        container.setMaxBinaryMessageBufferSize(SIZE_LIMIT);
        container.setMaxSessionIdleTimeout((long) SIZE_LIMIT);
        container.setAsyncSendTimeout((long) SIZE_LIMIT);
        return container;
    }
}

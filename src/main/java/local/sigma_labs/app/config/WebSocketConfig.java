package local.sigma_labs.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${allowed.origins}")
    private String theAllowedOrigins;
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
}

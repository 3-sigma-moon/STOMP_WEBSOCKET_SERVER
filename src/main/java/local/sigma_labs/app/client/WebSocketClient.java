package local.sigma_labs.app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@EnableScheduling
public class WebSocketClient {

    @Value("${stomp.websocket.private.destination}")
    private static String PRIVATE_DESTINATION_ENDPOINT;
    @Value("${stomp.websocket.server.address}")
    private static String WEBSOCKET_ADDRESS;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        StompSessionHandler sessionHandler = new CustomStompSessionHandler();
        stompClient.connectAsync("ws://localhost:9999/private_channel", sessionHandler);
        logger.info("connection succeed");
        new Scanner(System.in).nextLine();
    }


}

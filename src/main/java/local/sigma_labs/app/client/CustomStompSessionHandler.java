package local.sigma_labs.app.client;

import local.sigma_labs.app.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.Type;

@Configuration
@EnableScheduling
class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private static  final Logger logger = LoggerFactory.getLogger(CustomStompSessionHandler.class);


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/queue/private_channel", this);
        logger.info("New session established on 3-sigma-labs : " + session.getSessionId());
        session.send("/app/private_channel", "{\"name\":\"Client\"}".getBytes());
        logger.info("New session: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        logger.info("Received : "  +msg.getBody()+  "from "+msg.getUser()+ " at : " +
                msg.getSentAt());
        System.out.println("received " + payload);
    }
}


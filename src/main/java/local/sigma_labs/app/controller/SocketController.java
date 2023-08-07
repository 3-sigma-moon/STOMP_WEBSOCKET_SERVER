package local.sigma_labs.app.controller;

import local.sigma_labs.app.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class SocketController {
    @Value("${stomp.websocket.private.destination}")
    private  String PRIVATE_DESTINATION_ENDPOINT;

    @Value("${stomp.websocket.public.destination}")
    private  String PUBLIC_DESTINATION_ENDPOINT;


    @Value("${spring.kafka.input-topic}")
    private String kafkaInputTopic;
    private final static Logger logger = LoggerFactory.getLogger(SocketController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public SocketController(SimpMessagingTemplate simpMessagingTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @MessageMapping("/private-channel")
    public void respondHelloPrivate(String message) {
        String messageKey="sigma-keys"+ UUID.randomUUID();
        this.kafkaTemplate.send(kafkaInputTopic,messageKey,message);
        //this.simpMessagingTemplate.convertAndSend(PRIVATE_DESTINATION_ENDPOINT, message);
        logger.info("Message Received  {}", message);
    }

    @MessageMapping("/public-channel")
    public void sayHelloPublic(@Payload Message message) {
        this.simpMessagingTemplate.convertAndSend(PUBLIC_DESTINATION_ENDPOINT, message);
        logger.info("Message Received {}", message);

    }

}

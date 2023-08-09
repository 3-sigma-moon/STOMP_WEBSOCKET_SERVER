package local.sigma_labs.app.controller;

import local.sigma_labs.app.entity.*;
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

    @Value("${spring.kafka.text.input-topic}")
    private String kafkaTextInputTopic;
    @Value("${spring.kafka.image.input-topic}")
    private String kafkaImageInputTopic;
    @Value("${spring.kafka.audio.input-topic}")
    private String kafkaAudioInputTopic;

    @Value("${stomp.websocket.private.destination.text}")
    private String PRIVATE_TEXT_DESTINATION_ENDPOINT;
    @Value("${stomp.websocket.private.destination.image}")
    private String PRIVATE_IMAGE_DESTINATION_ENDPOINT;
    @Value("${stomp.websocket.private.destination.audio}")
    private String PRIVATE_AUDIO_DESTINATION_ENDPOINT;

    @Value("${stomp.websocket.public.destination}")
    private String PUBLIC_DESTINATION_ENDPOINT;

    private final static Logger logger = LoggerFactory.getLogger(SocketController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateT;
    private final KafkaTemplate<String, byte[]> kafkaTemplateI;
    private final KafkaTemplate<String, String> kafkaTemplateA;

    @Autowired
    public SocketController(SimpMessagingTemplate simpMessagingTemplate,
                            KafkaTemplate<String, String> kafkaTemplateT,
                            KafkaTemplate<String, byte[]> kafkaTemplateI,
                            KafkaTemplate<String, String> kafkaTemplateA
    ) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.kafkaTemplateT = kafkaTemplateT;
        this.kafkaTemplateI = kafkaTemplateI;
        this.kafkaTemplateA = kafkaTemplateA;
    }

    @MessageMapping("/private-channel-text")
    public void textReception(String message) {
        String messageKey = "sigma-keys" + UUID.randomUUID();
        this.kafkaTemplateT.send(kafkaTextInputTopic, messageKey, message);
        logger.info("Message Received  {}", message);
    }

    //STRING PAYLOAD WILL BE REPLACED WITH BINARY DATA PAYLOAD
    // FOR BLOB IMAGE FILE SENT
    //THEN WILL GET BYTES FORM THIS
    // AND TRANSFORM IT INTO STRING TO BE PASSED TO KAFKA TEMPLATE
    // AND STORE IN MONGO ...
    @MessageMapping("/private-channel-image")
    public void imageReception(@Payload byte[] imageFile) {
        logger.info("Message Received  {}", imageFile.clone());
        String messageKey = "sigma-keys" + UUID.randomUUID();
        this.kafkaTemplateI.send(kafkaImageInputTopic, messageKey, imageFile.clone());
    }

    //STRING PAYLOAD WILL BE REPLACED WITH BINARY DATA PAYLOAD
    // FOR BLOB AUDIO FILE SENT
    //THEN WILL GET BYTES FORM THIS
    // AND TRANSFORM IT INTO STRING TO BE PASSED TO KAFKA TEMPLATE
    // AND STORE IN MONGO ...
    @MessageMapping("/private-channel-audio")
    public void audioReception(String message) {
        String messageKey = "sigma-keys" + UUID.randomUUID();
        this.kafkaTemplateA.send(kafkaAudioInputTopic, messageKey, message);
        logger.info("Message Received  {}", message);
    }


    @MessageMapping("/public-channel")
    public void sayHelloPublic(@Payload Message message) {
        this.simpMessagingTemplate.convertAndSend(PUBLIC_DESTINATION_ENDPOINT, message);
        logger.info("Message Received {}", message);

    }

}

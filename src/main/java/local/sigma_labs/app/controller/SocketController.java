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
import org.springframework.stereotype.Component;
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
    @Value("${stomp.websocket.public.destination}")
    private String PUBLIC_DESTINATION_ENDPOINT;
    private final static Logger logger = LoggerFactory.getLogger(SocketController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final KafkaTemplate<String, String> kafkaTemplateT;
    private final KafkaTemplate<String, byte[]> kafkaTemplateI;
    private final KafkaTemplate<String, byte[]> kafkaTemplateA;

    @Autowired
    public SocketController(SimpMessagingTemplate simpMessagingTemplate,
                            KafkaTemplate<String, String> kafkaTemplateT,
                            KafkaTemplate<String, byte[]> kafkaTemplateI,
                            KafkaTemplate<String, byte[]> kafkaTemplateA) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.kafkaTemplateT = kafkaTemplateT;
        this.kafkaTemplateI = kafkaTemplateI;
        this.kafkaTemplateA = kafkaTemplateA;
    }

    @MessageMapping("/private-channel-text")
    public void textReception(String message) {
        this.sendPayloadToKafka("text", message);
    }

    @MessageMapping("/private-channel-image")
    public void imageReception(@Payload byte[] imageFile) {
        this.sendPayloadToKafka("image", imageFile);
    }

    @MessageMapping("/private-channel-audio")
    public void audioReception(@Payload byte[] audioFile) {
        this.sendPayloadToKafka("audio", audioFile);
    }

    public void sendPayloadToKafka(String channel, Object payload) {
        String messageKey = "sigma-keys" + UUID.randomUUID();
        String info = "";
        switch (channel) {
            case "text" -> {
                this.kafkaTemplateT.send(kafkaTextInputTopic, messageKey, (String) payload);
                info += "Text Message Received  {}";
                logger.info(info,payload);
            }
            case "image" -> {
                this.kafkaTemplateI.send(kafkaImageInputTopic, messageKey, (byte[]) payload);
                info += "Image File  Received As Byte Array  {}";
                logger.info(info,payload);
            }
            case "audio" -> {
                this.kafkaTemplateA.send(kafkaAudioInputTopic, messageKey, (byte[]) payload);
                info += "Audio File  Received As Byte Array  {}";
                logger.info(info,payload);
            }
            default -> throw new IllegalStateException("Unexpected value: " + channel);
        }
    }

    @MessageMapping("/public-channel")
    public void sayHelloPublic(@Payload Message message) {
        this.simpMessagingTemplate.convertAndSend(PUBLIC_DESTINATION_ENDPOINT, message);
        logger.info("Message Received {}", message);

    }

}

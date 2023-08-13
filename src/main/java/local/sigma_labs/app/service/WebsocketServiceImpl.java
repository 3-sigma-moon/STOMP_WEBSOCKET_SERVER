package local.sigma_labs.app.service;

import local.sigma_labs.app.controller.SocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebsocketServiceImpl implements WebsocketService {
    @Value("${spring.kafka.text.input-topic}")
    private String kafkaTextInputTopic;
    @Value("${spring.kafka.image.input-topic}")
    private String kafkaImageInputTopic;
    @Value("${spring.kafka.audio.input-topic}")
    private String kafkaAudioInputTopic;

    private final static Logger logger = LoggerFactory.getLogger(SocketController.class);
    private final KafkaTemplate<String, String> kafkaTemplateT;
    private final KafkaTemplate<String, byte[]> kafkaTemplateI;
    private final KafkaTemplate<String, byte[]> kafkaTemplateA;


    public WebsocketServiceImpl(KafkaTemplate<String, String> kafkaTemplateT,
                                KafkaTemplate<String, byte[]> kafkaTemplateI,
                                KafkaTemplate<String, byte[]> kafkaTemplateA) {
        this.kafkaTemplateT = kafkaTemplateT;
        this.kafkaTemplateI = kafkaTemplateI;
        this.kafkaTemplateA = kafkaTemplateA;
    }

    @Override
    public void sendPayloadToKafka(String channel, Object payload) {
        String messageKey = "sigma-keys" + UUID.randomUUID();
        switch (channel) {
            case "text" -> {
                this.kafkaTemplateT.send(kafkaTextInputTopic, messageKey, (String) payload);
                logger.info("Text Message Received  {}", payload);
            }
            case "image" -> {
                this.kafkaTemplateI.send(kafkaImageInputTopic, messageKey, (byte[]) payload);
                logger.info("Image File  Received As Byte Array  {}", payload);
            }
            case "audio" -> {
                this.kafkaTemplateA.send(kafkaAudioInputTopic, messageKey, (byte[]) payload);
                logger.info("Audio File  Received As Byte Array  {}", payload);
            }
            default -> throw new IllegalStateException("Unexpected value: " + channel);
        }
    }
}

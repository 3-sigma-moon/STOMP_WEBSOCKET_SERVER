package local.sigma_labs.app.client;

import local.sigma_labs.app.entity.Message;
import local.sigma_labs.app.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@EnableScheduling
@Component
public class MessageSender {
    @Value("${stomp.websocket.private.destination}")
    private  String PRIVATE_DESTINATION_ENDPOINT;
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${spring.kafka.input-topic}")
    private String kafkaInputTopic;
    private final KafkaTemplate<String,String> kafkaTemplate;
    public MessageSender(SimpMessagingTemplate messagingTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 100)
    private void sendMessageOnPrivateChannel() {
        Message message = new Message(UUID.randomUUID(),
                "SENDING MESSAGE FROM SERVER ON PRIVATE CHANNEL",
                new User(UUID.randomUUID(), "SERVER_USER", "PREMIUM"),
                new Date());
        //logger.info("Message sent {}",message);
        String messageKey="sigma-keys"+UUID.randomUUID();
        //this.kafkaTemplate.send(kafkaInputTopic,messageKey,message.toString());
        //this.messagingTemplate.convertAndSend(PRIVATE_DESTINATION_ENDPOINT, message);
    }
}

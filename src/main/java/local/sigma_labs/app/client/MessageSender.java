package local.sigma_labs.app.client;

import local.sigma_labs.app.entity.Message;
import local.sigma_labs.app.entity.User;
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
    /*
    @Value("${spring.kafka.text.input-topic}")
    private String kafkaTextInputTopic;
    @Value("${stomp.websocket.private.destination.text}")
    private  String PRIVATE_TEXT_DESTINATION_ENDPOINT;

    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);

     */
    SimpMessagingTemplate messagingTemplate;

    KafkaTemplate<String, String> kafkaTemplate;

    public MessageSender(final SimpMessagingTemplate messagingTemplate, final KafkaTemplate<String, String> kafkaTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    private void sendMessageOnPrivateChannel() {
        Message message = new Message(UUID.randomUUID(),
                "SENDING MESSAGE FROM SERVER ON PRIVATE CHANNEL",
                new User(UUID.randomUUID(), "SERVER_USER", "PREMIUM"),
                new Date());
        //
        // log.info("Message sent {}",message);
        //String messageKey = "sigma-keys" + UUID.randomUUID();
        //this.kafkaTemplate.send(kafkaTextInputTopic,messageKey,message.toString());
        //this.messagingTemplate.convertAndSend(PRIVATE_TEXT_DESTINATION_ENDPOINT, message);
    }
}

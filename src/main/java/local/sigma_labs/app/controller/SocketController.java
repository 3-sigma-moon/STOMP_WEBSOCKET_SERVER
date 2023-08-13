package local.sigma_labs.app.controller;

import local.sigma_labs.app.entity.*;
import local.sigma_labs.app.service.WebsocketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class SocketController {
    private final WebsocketServiceImpl websocketService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${stomp.websocket.public.destination}")
    private String PUBLIC_DESTINATION_ENDPOINT;

    @Autowired
    public SocketController(SimpMessagingTemplate simpMessagingTemplate,
                           WebsocketServiceImpl websocketService) {
        this.websocketService = websocketService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        
    }

    @MessageMapping("/private-channel-text")
    public void textReception(String message) {
        this.websocketService.sendPayloadToKafka("text", message);
    }

    @MessageMapping("/private-channel-image")
    public void imageReception(@Payload byte[] imageFile) {
        this.websocketService.sendPayloadToKafka("image", imageFile);
    }

    @MessageMapping("/private-channel-audio")
    public void audioReception(@Payload byte[] audioFile) {
        this.websocketService.sendPayloadToKafka("audio", audioFile);
    }

    @MessageMapping("/public-channel")
    public void sayHelloPublic(@Payload Message message) {
        this.simpMessagingTemplate.convertAndSend(PUBLIC_DESTINATION_ENDPOINT, message);
        log.info("Message Received {}", message);
    }

}

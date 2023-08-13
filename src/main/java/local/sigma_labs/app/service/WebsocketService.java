package local.sigma_labs.app.service;

public interface WebsocketService {
    void sendPayloadToKafka(String channel, Object payload);
}

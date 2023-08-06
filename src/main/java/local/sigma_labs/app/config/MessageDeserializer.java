package local.sigma_labs.app.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import local.sigma_labs.app.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import java.util.Map;

@Slf4j
public class MessageDeserializer implements Deserializer<Message>{

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Message deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.info("Null received at deserializing");
                return null;
            }
            log.info("Deserializing...");

            return objectMapper.readValue(new String(data, "UTF-8"), Message.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to Message");
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}

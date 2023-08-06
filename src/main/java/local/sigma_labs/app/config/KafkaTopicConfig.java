package local.sigma_labs.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.input-topic}")
    private String kafkaInputTopic;

    @Value("${spring.kafka.output-topic}")
    private String kafkaOutputTopic;

    @Bean
    public NewTopic createInputTopic() {
        return new NewTopic(kafkaInputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createOutputTopic() {
        return new NewTopic(kafkaOutputTopic, 1, (short) 1);
    }

}

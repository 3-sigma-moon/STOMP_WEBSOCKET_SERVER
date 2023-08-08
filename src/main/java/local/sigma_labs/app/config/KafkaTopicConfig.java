package local.sigma_labs.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.text.input-topic}")
    private String kafkaTextInputTopic;
    @Value("${spring.kafka.image.input-topic}")
    private String kafkaImageInputTopic;
    @Value("${spring.kafka.audio.input-topic}")
    private String kafkaAudioInputTopic;

    @Value("${spring.kafka.text.output-topic}")
    private String kafkaTextOutputTopic;
    @Value("${spring.kafka.image.output-topic}")
    private String kafkaImageOutputTopic;
    @Value("${spring.kafka.audio.output-topic}")
    private String kafkaAudioOutputTopic;

    @Bean
    public NewTopic createInputTopicT() {
        return new NewTopic(kafkaTextInputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createInputTopicI() {
        return new NewTopic(kafkaImageInputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createInputTopicA() {
        return new NewTopic(kafkaAudioInputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createOutputTopicT() {
        return new NewTopic(kafkaTextOutputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createOutputTopicI() {
        return new NewTopic(kafkaImageOutputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic createOutputTopicA() {
        return new NewTopic(kafkaAudioOutputTopic, 1, (short) 1);
    }

}

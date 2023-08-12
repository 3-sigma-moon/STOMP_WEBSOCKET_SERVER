package local.sigma_labs.app.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class kafkaConfig {

    private final Map<String, Object> consumerConfigs;

    private Map<String, Object> setProducerConfigs(Class<?> ValueSerializerClass) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ValueSerializerClass);
        config.put("message.max.bytes", 1048576000);
        config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,1048576000);
        return config;
    }

    public kafkaConfig() {
        this.consumerConfigs = new HashMap<>();
        this.consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        this.consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, "sigma-labs-brokers");
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactoryTexte() {
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConsumerFactory<String, byte[]> consumerFactoryBinary() {
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs, new StringDeserializer(), new ByteArrayDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaTemplateListenerContainerFactoryTexte() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryTexte());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaTemplateListenerContainerFactoryBinary() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryBinary());
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactoryTexte() {
        return new DefaultKafkaProducerFactory<>(this.setProducerConfigs(StringSerializer.class));
    }

    @Bean
    public ProducerFactory<String, byte[]> producerFactoryBinary() {
        return new DefaultKafkaProducerFactory<>(this.setProducerConfigs(ByteArraySerializer.class));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateTexte() {
        return new KafkaTemplate<>(producerFactoryTexte());
    }

    @Bean
    public KafkaTemplate<String, byte[]> kafkaTemplateBinary() {
        return new KafkaTemplate<>(producerFactoryBinary());
    }
}

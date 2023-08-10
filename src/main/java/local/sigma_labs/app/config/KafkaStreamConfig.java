package local.sigma_labs.app.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
public class KafkaStreamConfig {

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

    private final Properties propsStringString;
    private final Properties propsStringByteArray;


    public KafkaStreamConfig() {
        this.propsStringString = new Properties();
        this.propsStringByteArray = new Properties();
        this.setProperties(this.propsStringString, Serdes.String().getClass(), Serdes.String().getClass());
        this.setProperties(this.propsStringByteArray, Serdes.String().getClass(), Serdes.ByteArray().getClass());
    }

    public void setProperties(Properties properties, Class<?> KeySerdesClass, Class<?> ValueSerdesClass) {
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "sigma-labs-brokers");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, KeySerdesClass);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, ValueSerdesClass);
    }

    public KStream<String, String> buildKafkaStreamStringString(String inputTopic, String outputTopic) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> stream = streamsBuilder.stream(inputTopic,
                Consumed.with(Serdes.String(), Serdes.String()));
        stream.map(KeyValue::pair).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), this.propsStringString);
        streams.start();
        return stream;
    }

    public KStream<String, byte[]> buildKafkaStreamStringByteArray(String inputTopic, String outputTopic) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, byte[]> stream = streamsBuilder.stream(inputTopic,
                Consumed.with(Serdes.String(), Serdes.ByteArray()));
        stream.map(KeyValue::pair).to(outputTopic, Produced.with(Serdes.String(), Serdes.ByteArray()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), this.propsStringByteArray);
        streams.start();
        return stream;
    }

    @Bean
    public KStream<String, String> kStreamText() {
        return buildKafkaStreamStringString(kafkaTextInputTopic, kafkaTextOutputTopic);
    }

    @Bean
    public KStream<String, byte[]> kStreamImage() {
        return buildKafkaStreamStringByteArray(kafkaImageInputTopic, kafkaImageOutputTopic);
    }

    @Bean
    public KStream<String, String> kStreamAudio() {
        return buildKafkaStreamStringString(kafkaAudioInputTopic, kafkaAudioOutputTopic);
    }
}

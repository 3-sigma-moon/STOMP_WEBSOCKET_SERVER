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



    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServerAddress;

    //@Value("${spring.kafka.producer.key-serializer}")
    //private String STRING_SERIALIZER;


    @Bean
    public KStream<String, String> kStreamText() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "sigma-labs-texts");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServerAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "local.sigma_labs.app.config.MessageDeserializer");
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> stream = streamsBuilder.stream(kafkaTextInputTopic,
                Consumed.with(Serdes.String(), Serdes.String()));
        stream.map(KeyValue::pair).to(kafkaTextOutputTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();
        return stream;
    }

    @Bean
    public KStream<String, String> kStreamImage() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "sigma-labs-images");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServerAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "local.sigma_labs.app.config.MessageDeserializer");
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> stream = streamsBuilder.stream(kafkaImageInputTopic,
                Consumed.with(Serdes.String(), Serdes.String()));
        stream.map(KeyValue::pair).to(kafkaImageOutputTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();
        return stream;
    }

    @Bean
    public KStream<String, String> kStreamAudio() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "sigma-labs-audios");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServerAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "local.sigma_labs.app.config.MessageDeserializer");
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> stream = streamsBuilder.stream(kafkaAudioInputTopic,
                Consumed.with(Serdes.String(), Serdes.String()));
        stream.map(KeyValue::pair).to(kafkaAudioOutputTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();
        return stream;
    }
}

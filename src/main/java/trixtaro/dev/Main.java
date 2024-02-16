package trixtaro.dev;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fruits-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        KStream<String, String> textLines = builder.stream("fruits", Consumed.with(stringSerde, stringSerde));

        textLines.foreach(
                (key, value) -> System.out.println(value)
        );

        KStream<String, String> flatFruits = textLines
                .flatMapValues((textLine) -> Arrays.asList(textLine.toLowerCase().split("\\W+")));

        flatFruits.foreach(
                (key, value) -> System.out.println("flat fruits: " + value)
        );

        KTable<String, Long> fruitNumbers = flatFruits
                .groupBy(
                        (key, word) -> word
                )
                .count();

        fruitNumbers.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
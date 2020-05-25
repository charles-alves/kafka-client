package br;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProdutorKafka {

    private String brokers;
    private List topicos;
    KafkaProducer kafkaProducer;
    Consumidor consumidor;

    public ProdutorKafka(Consumidor consumidor) {
        this.consumidor = consumidor;
    }

    public void start() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", consumidor.brokers());
        config.put("key.serializer", "io.vertx.kafka.client.serialization.BufferSerializer");
        config.put("value.serializer", "io.vertx.kafka.client.serialization.BufferSerializer");
        config.put("acks", "1");

        Vertx vertx = Vertx.vertx();
        kafkaProducer = KafkaProducer.create(vertx, config, String.class, String.class);
    }

    public void send(String topic, String message) {
        if (kafkaProducer != null) {
            KafkaProducerRecord record = KafkaProducerRecord.create(topic, message);
            kafkaProducer.send(record, result -> {
                System.out.println(result);
            });
        }
    }

}
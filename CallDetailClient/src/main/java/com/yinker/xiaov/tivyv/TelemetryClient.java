package com.yinker.xiaov.tivyv;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TelemetryClient {
    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final Boolean isAsync;

    public TelemetryClient(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", Configs.getConfig("KAFKA_SERVER_URL_LIST"));
        props.put("client.id", "TelemetryClient");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("max.request.size", "20971520");
        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void SendMsg(String keyStr, String messageStr) throws KafkaException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        if (isAsync) { // Send asynchronously
            producer.send(new ProducerRecord<>(topic, keyStr, messageStr), new DemoCallBack(startTime, keyStr, messageStr));
        } else { // Send synchronously
        	producer.send(new ProducerRecord<>(topic, keyStr, messageStr)).get();
//            System.out.println("Sent message: (" + keyStr + ", " + messageStr + ")");
        }
    }
    
    public void close() {
    	producer.close();
    }
}

class DemoCallBack implements Callback {

    private final long startTime;
    private final String key;
    private final String message;

    public DemoCallBack(long startTime, String key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                    "), " +
                    "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}

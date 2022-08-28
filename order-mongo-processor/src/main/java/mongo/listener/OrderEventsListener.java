package mongo.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mongo.dto.inbound.OrderEventDto;
import mongo.processor.MongoProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class OrderEventsListener {

    @Autowired
    MongoProcessor mongoProcessor;

    @KafkaListener(topics = "#{kafkaProperties.getConsumer().getProperties().get(\"topic\")}")
    public void listen(List<Message<OrderEventDto>> messageList, Acknowledgment ack) {
        log.warn("received batch={}", messageList.size());

        Flux.fromIterable(messageList)
                .flatMap(message -> mongoProcessor.process(message))
                .subscribeOn(Schedulers.boundedElastic())
                .blockLast();
        ack.acknowledge();
    }
}

package com.projects.order_create.QueueReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.order_create.dto.OrderCompletedDto;
import com.projects.order_create.dto.OrderRequest;
import com.projects.order_create.queue.OrderRequestsBlockingQueue;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@NoArgsConstructor
@Component
public class OrderEventConsumer implements Runnable {

  @Autowired
  OrderRequestsBlockingQueue queue;

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  KafkaProperties kafkaProperties;

  private static final Integer THREAD_SLEEP_TIME = 2000;

  @Override
  public void run() {

    while (true) {
      try {
        OrderRequest orderRequest = queue.getMessage();
        if (orderRequest != null) {
          log.info(Thread.currentThread().getName());
          produceToKafkaTopic(new ObjectMapper().writeValueAsString(OrderCompletedDto.builder()
                  .associateId(Thread.currentThread().getName())
                  .orderCompleteTime(System.currentTimeMillis())
                  .orderId(orderRequest.getOrderId())
                  .price(orderRequest.getPrice())
                  .orderCreateTime(orderRequest.getOrderCreateTime())
                  .status(((new Random().nextInt(10))<=9) ? "DELIVERED" : "ORDER_DELIVERY_ISSUE")
                  .build()), orderRequest.getOrderId());
        }
        Thread.sleep(THREAD_SLEEP_TIME);
      } catch (InterruptedException e) {
        e.printStackTrace();  // TODO impl
      } catch (JsonProcessingException e) {
        e.printStackTrace();  // TODO impl
      }


    }
  }

  public void produceToKafkaTopic(String payload, String orderId) {
    String outboundTopicName =
            kafkaProperties.getProducer().getProperties().get(KafkaHeaders.TOPIC);
    Message<?> message =
            MessageBuilder.withPayload(payload)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, orderId)
                    .setHeader(KafkaHeaders.TOPIC, outboundTopicName)
                    .build();

    try {
      Mono.fromFuture(kafkaTemplate.send(message).completable())
              .subscribe(
                      result ->
                              log.info(
                                      "Successfully published partition={} offset={} key={} value={}",
                                      result.getRecordMetadata().partition(),
                                      result.getRecordMetadata().offset(),
                                      result.getProducerRecord().key(),
                                      result.getProducerRecord().value()));
    } catch (Exception exp) {
      log.error(
              "Exception occurred while publishing the message with key : {} to the topic : {} Exception is : {}",
              orderId,
              outboundTopicName,
              exp.toString());
    }
  }

}
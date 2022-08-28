package mongo.config;

import lombok.extern.slf4j.Slf4j;
import mongo.listener.OrderEventsListener;
import mongo.processor.MongoProcessor;
import mongo.util.YamlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Configuration
public class AppBeanConfig {
  @Autowired Environment environment;


  @Bean
  @Primary
  @Qualifier("kafkaProperties")
  public KafkaProperties kafkaProperties() throws IOException {
    String kafkaConfig = "{\"producer\":{\"bootstrap-servers\":[\"localhost:9092\"],\"key-serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"value-serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"properties\":{\"kafka_topic\":\"quickstart\"}},\"consumer\":{\"bootstrap-servers\":[\"localhost:9092\"],\"key-deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"value-deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"auto-offset-reset\":\"earliest\",\"max-poll-records\":500,\"group-id\":\"order-events-mongo-listener\",\"properties\":{\"topic\":\"quickstart\"}},\"listener\":{\"concurrency\":1,\"type\":\"BATCH\",\"ack-mode\":\"MANUAL\"}}";
    KafkaProperties kafkaProperties =
            YamlUtil.getObjectWithKebabCase(kafkaConfig, KafkaProperties.class);
    return kafkaProperties;
  }

  @Bean
  @Primary
  public ProducerFactory<String, String> producerFactory(
          @Qualifier("kafkaProperties") KafkaProperties kafkaProperties) {
    return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
  }

  @Bean
  @Primary
  public KafkaTemplate<String, String> kafkaTemplate(
          @Qualifier("kafkaProperties") KafkaProperties kafkaProperties) {
    String topic = kafkaProperties.getProducer().getProperties().get(KafkaHeaders.TOPIC);
    KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory(kafkaProperties));
    if (!StringUtils.isBlank(topic)) template.setDefaultTopic(topic);
    return template;
  }

  @Bean
  public OrderEventsListener orderEventsListener(
          MongoProcessor mongoProcessor) {
    return new OrderEventsListener(mongoProcessor);
  }

}

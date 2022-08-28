package mongo.processor;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mongo.dto.inbound.OrderEventDto;
import mongo.model.OrderInfo;
import mongo.repository.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class MongoProcessorImpl implements MongoProcessor {

    @Autowired
    OrderEventRepository orderEventRepository;

    private final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public Mono<String> process(Message message) {
        try {
            OrderEventDto orderEventDto = OBJECT_MAPPER.readValue((String) message.getPayload(), OrderEventDto.class);
            orderEventRepository.save(OrderInfo.fromDto(orderEventDto));
            log.warn("Json to Java Object Created Successfully for message : {}", message.getPayload());
        } catch (Exception e) {
            log.error("Exception while processing message : {}", e.getMessage());
        }
        return Mono.just("Done");

    }
}

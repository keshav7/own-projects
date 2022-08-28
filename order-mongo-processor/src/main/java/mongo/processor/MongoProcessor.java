package mongo.processor;

import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

public interface MongoProcessor {

  Mono<String> process(Message message);
}

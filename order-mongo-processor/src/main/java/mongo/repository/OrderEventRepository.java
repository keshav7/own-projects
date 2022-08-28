package mongo.repository;

import mongo.model.OrderInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends MongoRepository<OrderInfo, String>{

}

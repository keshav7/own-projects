package mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mongo.dto.inbound.OrderEventDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "OrderInfo")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderInfo {

    @Id
    private String id;
    private String associateId;
    private Long orderCreateTime;
    private Long orderCompleteTime;
    private String status;
    private Integer price;

    public static OrderInfo fromDto(OrderEventDto orderEventDto) {
        return OrderInfo.builder()
                .id(orderEventDto.getOrderId())
                .associateId(orderEventDto.getAssociateId())
                .orderCreateTime(orderEventDto.getOrderCreateTime())
                .orderCompleteTime(orderEventDto.getOrderCompleteTime())
                .status(orderEventDto.getStatus())
                .price(orderEventDto.getPrice())
                .build();
    }
}

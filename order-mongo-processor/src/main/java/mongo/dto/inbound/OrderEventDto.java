package mongo.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderEventDto {
    String orderId;
    String associateId;
    Long orderCreateTime;
    Long orderCompleteTime;
    String status;
    Integer price;
}

package com.projects.order_create.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCompletedDto {

    String orderId;
    String associateId;
    Long orderCreateTime;
    Long orderCompleteTime;
    String status;
    Integer price;
}

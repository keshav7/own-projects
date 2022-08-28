package com.projects.order_create.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {
    String orderId;
    Long orderCreateTime;
    Integer price;
}

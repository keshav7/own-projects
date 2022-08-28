package com.projects.order_create.queue;

import com.projects.order_create.dto.OrderRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class OrderRequestsBlockingQueue {
    int capacity = 5;
    BlockingQueue<OrderRequest> queue = new ArrayBlockingQueue<OrderRequest>(500);

//    public OrderRequestsBlockingQueue(int capacity) {
//        this.capacity = capacity;
//        this.queue = new ArrayBlockingQueue<OrderRequest>(capacity);
//    }

    public boolean isFull() {
        return queue.size() == capacity;
    }

    public void putMessage(OrderRequest s) {
        queue.add(s);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public OrderRequest getMessage() throws InterruptedException {
        return queue.take();
    }
}

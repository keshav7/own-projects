package com.projects.order_create.dataGen;

import com.projects.order_create.QueueReader.OrderEventConsumer;
import com.projects.order_create.dto.OrderRequest;
import com.projects.order_create.queue.OrderRequestsBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class DummyOrderRequestsGenerator {

    @Autowired
    private OrderRequestsBlockingQueue orderRequestsBlockingQueue;

    @Autowired
    private OrderEventConsumer orderEventConsumer;

    private static final Integer threadCount = 10;
    private static final Integer RANDOM_PRICE_LOW_BOUND = 100;
    private static final Integer RANDOM_PRICE_UPPER_BOUND = 2000;

    @PostConstruct
    public void initialise() {

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("%d")
                .priority(Thread.MAX_PRIORITY)
                .build();

        ExecutorService executorServicePool = Executors.newFixedThreadPool(5, factory);

        for(int i = 0;  i < threadCount; i++) {
            executorServicePool.submit(() -> {
                try {
                    orderEventConsumer.run();
                } catch (Exception e) {
                    log.error("Error in executor thread pool", e.getMessage());
                }
            });
        }
//        System.out.println("hello");
    }


    @Scheduled(cron = "#{fixedDelayTime}")
    public void generateOrderRequests() {
        try {

//            System.out.println("hello");
            orderRequestsBlockingQueue.putMessage(createOrderRequestObject(String.valueOf(System.currentTimeMillis())));
        } catch(Exception e) {
            System.out.println("Exception thrown" + e.getMessage());
        }

    }

    private OrderRequest createOrderRequestObject(String orderId) {
        Integer randomPrice = (int) (Math.random() * (RANDOM_PRICE_UPPER_BOUND - RANDOM_PRICE_LOW_BOUND)) + RANDOM_PRICE_LOW_BOUND;
        return new OrderRequest(orderId, System.currentTimeMillis(), randomPrice);
    }




}

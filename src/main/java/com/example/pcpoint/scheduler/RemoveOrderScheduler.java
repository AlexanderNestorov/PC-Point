package com.example.pcpoint.scheduler;

import com.example.pcpoint.security.jwt.AuthEntryPointJwt;
import com.example.pcpoint.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemoveOrderScheduler {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(RemoveOrderScheduler.class);

    public RemoveOrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedDelay = 10800000)
    public void removeExpiredOrders() {
        int count = orderService.removeExpiredOrders();
        logger.info("Removed expired " + count + " orders");
    }
}

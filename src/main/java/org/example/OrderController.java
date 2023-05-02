package org.example;

import org.example.common.ApiCredentials;
import org.example.common.OrderDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final HeaderService headerService;

    private final OrderService orderService;

    public OrderController(HeaderService headerService, OrderService orderService) {
        this.headerService = headerService;
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public OrderDetails getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final ApiCredentials credentials = headerService.parseAuthorizationHeader(authorization);

        return orderService.fetchOrderDetails(orderId, credentials);
    }

}

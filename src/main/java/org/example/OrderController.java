package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @ExceptionHandler(FetchException.class)
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public ErrorResponse fetchException(FetchException fetchException){
//        System.err.println(fetchException);
//        return new ErrorResponse(fetchException.getMessage());
//    }

    @GetMapping("/{orderId}")
    public OrderDetails getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final String basic = authorization.substring(authorization.indexOf("Basic "));
        final String[] split = basic.split(":");
        final String username = split[0];
        final String password = split[1];

        return orderService.fetchOrderDetails(orderId, username, password);
    }

}

package org.example;

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

//    @ExceptionHandler(FetchException.class)
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public ErrorResponse fetchException(FetchException fetchException){
//        System.err.println(fetchException);
//        return new ErrorResponse(fetchException.getMessage());
//    }

    @GetMapping("/{orderId}")
    public OrderDetails getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final ApiCredentials credentials = headerService.parseAuthorizationHeader(authorization);

        return orderService.fetchOrderDetails(orderId, credentials);
    }

}

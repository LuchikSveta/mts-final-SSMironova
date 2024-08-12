package ru.mts.siebel.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.siebel.order.entity.Order;
import ru.mts.siebel.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public void getOrders() {
        orderService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<String> orderParams(@RequestBody Order order) {
        orderService.addOrder(order);
        return ResponseEntity.ok("Order is received");
    }

}

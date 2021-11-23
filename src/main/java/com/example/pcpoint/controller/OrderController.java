package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.request.order.OrderAddRequest;
import com.example.pcpoint.model.request.order.OrderUpdateRequest;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.order.OrderAddServiceModel;
import com.example.pcpoint.model.service.order.OrderUpdateServiceModel;
import com.example.pcpoint.service.order.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntity> orders = this.orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id) {
        OrderEntity order = this.orderService.findOrderById(id);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderAddRequest orderAddRequest,
                                       BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid order request data!"));
        }

        OrderAddServiceModel orderAddServiceModel =
                modelMapper.map(orderAddRequest, OrderAddServiceModel.class);


        orderService.addOrder(orderAddServiceModel);


        return ResponseEntity.ok(new MessageResponse("Order added successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest,
                                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid order request data!"));
        }

        OrderUpdateServiceModel orderUpdateServiceModel =
                modelMapper.map(orderUpdateRequest, OrderUpdateServiceModel.class);


        OrderEntity updated = orderService.updateOrder(orderUpdateServiceModel);


        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {

        Long deletionId = orderService.findOrderById(id).getId();

        if(deletionId == null){
            return ResponseEntity.notFound().build();
        }

        this.orderService.deleteOrder(id);

        return ResponseEntity.ok(new MessageResponse("Order deleted successfully!"));
    }

    @GetMapping("/by_user/{id}")
    public ResponseEntity<?> getAllOrdersByUser(@PathVariable("id") Long id) {
        List<OrderEntity> orders = this.orderService.findAllOrdersByBuyer(id);
        return ResponseEntity.ok(orders);
    }

}

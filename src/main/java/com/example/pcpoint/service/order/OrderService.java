package com.example.pcpoint.service.order;

import com.example.pcpoint.model.entity.order.OrderEntity;
import com.example.pcpoint.model.service.order.OrderAddServiceModel;
import com.example.pcpoint.model.service.order.OrderUpdateServiceModel;

import java.util.List;

public interface OrderService {

    void addOrder(OrderAddServiceModel orderAddServiceModel);

    List<OrderEntity> findAllOrders();

    OrderEntity findOrderById(Long id);

    OrderEntity updateOrder(OrderUpdateServiceModel orderUpdateServiceModel);

    void deleteOrder(Long id);

    int removeExpiredOrders();

    List<OrderEntity> findAllOrdersByBuyer(Long id);
}

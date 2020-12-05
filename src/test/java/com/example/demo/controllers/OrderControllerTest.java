package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderControllerTest {

    private OrderController orderController;

    private final OrderRepository orderRepo = mock(OrderRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);

    @BeforeEach
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
    }

    @Test
    public void submit_order_happy_path() {
        when(userRepo.findByUsername(getUserCart().getUsername())).thenReturn(getUserCart());

        final ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(getUserCart().getUsername());

        assertNotNull(userOrderResponseEntity.getBody());
        assertEquals("test", userOrderResponseEntity.getBody().getUser().getUsername());
        assertEquals(new BigDecimal(2.99), userOrderResponseEntity.getBody().getTotal());
    }

    @Test
    public void get_order_for_user() {
        when(userRepo.findByUsername("test")).thenReturn(getUserCart());

        when(orderRepo.findByUser(any())).thenReturn(getUserOrder(getUserCart().getCart()));
        final ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");

        assertEquals(1,responseEntity.getBody().size());

    }


    private User getUserCart() {
        User user = new User();
        user.setUsername("test");
        user.setId(1L);
//        user.setCart(getCart());

        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = new ArrayList<>();
        Item item = getItem();
        items.add(item);
        cart.setItems(items);

        cart.setUser(user);
        cart.setTotal(new BigDecimal(2.99));

        user.setCart(cart);

        return user;
    }

    private User getUser() {
        User user = new User();
        user.setUsername("test");
        user.setId(1L);
        user.setCart(new Cart());

        return user;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(new BigDecimal(2.99));
        item.setDescription("A widget that is round");

        return item;
    }

    private List<UserOrder> getUserOrder(Cart cart) {
        List<UserOrder> userOrderList = new ArrayList<>();
        UserOrder order = new UserOrder();
        order.setItems(cart.getItems().stream().collect(Collectors.toList()));
        order.setTotal(cart.getTotal());
        User user = getUser();
        order.setUser(user);
        userOrderList.add(order);

        return userOrderList;
    }

}

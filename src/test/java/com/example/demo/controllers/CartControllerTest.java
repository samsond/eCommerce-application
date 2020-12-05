package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @BeforeEach
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path() {

        User user = getUser();
        Item item = getItem();
        Cart cart = getCart(item, user);

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("test");


//        when(cartRepo.getOne(1L)).thenReturn(cart);
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepo.findByUser(user)).thenReturn(cart);

        final ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(cartRequest);

        assertNotNull(cartResponseEntity.getBody());
        assertEquals("A widget that is round", cartResponseEntity.getBody().getItems().get(0).getDescription());
    }

    @Test
    public void remove_from_cart_happy_path() {
        User user = getUser();
        Item item = getItem();
        Cart cart = getCart(item, user);

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("test");

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepo.findByUser(user)).thenReturn(cart);

        final ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(cartRequest);

        assertNotNull(cartResponseEntity.getBody());



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

    private Cart getCart(Item item, User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(new BigDecimal(2.99));

        return cart;
    }
}

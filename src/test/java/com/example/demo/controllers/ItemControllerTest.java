package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @BeforeEach
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void get_items_happy_path() {
        List<Item> items = new ArrayList<>();
        items.add(getItem());
        when(itemRepo.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> itemResponseEntity = itemController.getItems();

        assertNotNull(itemResponseEntity.getBody().get(0));
        assertEquals(1L, itemResponseEntity.getBody().get(0).getId());
        assertEquals("Round Widget", itemResponseEntity.getBody().get(0).getName());
        assertEquals(new BigDecimal(2.99), itemResponseEntity.getBody().get(0).getPrice());
    }

    @Test
    public void get_item_by_id_happy_path() {
        when(itemRepo.findById(1L)).thenReturn(Optional.of(getItem()));
        final ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);

        assertNotNull(itemResponseEntity.getBody());
        assertEquals(1L, itemResponseEntity.getBody().getId());
    }

    @Test
    public void get_item_by_name() {
        List<Item> items = new ArrayList<>();
        items.add(getItem());
        when(itemRepo.findByName(getItem().getName())).thenReturn(items);

        final ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName(getItem().getName());

        assertNotNull(itemResponseEntity.getBody().get(0));
        assertEquals("Round Widget", itemResponseEntity.getBody().get(0).getName());

    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(new BigDecimal(2.99));
        item.setDescription("A widget that is round");

        return item;
    }

}

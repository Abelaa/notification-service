package com.sa.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String userEmail;
    private Integer totalQuantity;
    private Double totalPrice;
    private String dateCreated;
    private List<CartItem> shoppingCart = new ArrayList<>();
}

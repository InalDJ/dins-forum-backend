package com.java.springportfolio.service;

import com.java.springportfolio.exception.ItemNotFoundException;
import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum OrderType {
    NEW,
    POPULAR;

    public static OrderType findOrderType(String orderType) {
        for (var value : EnumSet.allOf(OrderType.class)) {
            if (value.name().equalsIgnoreCase(orderType))
                return value;
        }
        throw new ItemNotFoundException("Order type has not been found!");
    }
}

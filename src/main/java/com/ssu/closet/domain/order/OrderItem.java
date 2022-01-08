package com.ssu.closet.domain.order;

import com.ssu.closet.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int orderPrice;
    private int count;
}

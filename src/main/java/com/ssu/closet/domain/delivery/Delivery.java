package com.ssu.closet.domain.delivery;

import com.ssu.closet.domain.order.Orders;
import com.ssu.closet.domain.shipping.ShippingInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ShippingInfo shippingInfo;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne
    private Orders order;
}
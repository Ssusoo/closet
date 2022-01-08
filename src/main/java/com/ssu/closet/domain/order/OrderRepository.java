package com.ssu.closet.domain.order;

import com.ssu.closet.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByMemberAndOrderStatus(Member member, OrderStatus orderStatus);
}


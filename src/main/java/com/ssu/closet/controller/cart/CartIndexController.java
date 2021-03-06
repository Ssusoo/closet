package com.ssu.closet.controller.cart;

import com.ssu.closet.domain.member.CurrentUser;
import com.ssu.closet.domain.member.Member;
import com.ssu.closet.domain.order.OrderItem;
import com.ssu.closet.service.cart.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartIndexController {
    private final OrderService orderService;

    @PostMapping("/cart/list")
    public String addCart(@RequestParam("item_id") String[] itemId,
                          @CurrentUser Member member, Model model) {
        Long[] idArr = Arrays.stream(itemId)
                .map(Long::parseLong).toArray(Long[]::new);
        List<Long> itemIdList = List.of(idArr);

        if (member == null) {

            return "redirect:/";
        }
        orderService.addCart(member, itemIdList);

        return cartList(member, model);
    }

    @GetMapping("/cart/list")
    public String cartList(@CurrentUser Member member, Model model) {
        try {
            List<OrderItem> cartList = orderService.getCart(member);
            model.addAttribute("cartList", cartList);
            model.addAttribute("totalPrice", orderService.getTotalPrice(cartList));

        } catch (IllegalArgumentException e) {
            model.addAttribute("error_message", e.getMessage());
        }
        return "/view/cart";
    }

    @GetMapping("/cart/minus")
    public String cartMinus(@RequestParam("id") String itemId, @CurrentUser Member member, Model model){
        Long deleteItemId = Long.parseLong(itemId);

        orderService.minusCart(member, deleteItemId);

        return cartList(member, model);
    }
}

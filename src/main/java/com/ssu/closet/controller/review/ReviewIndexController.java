package com.ssu.closet.controller.review;

import com.ssu.closet.domain.item.Item;
import com.ssu.closet.domain.member.CurrentUser;
import com.ssu.closet.domain.member.Member;
import com.ssu.closet.domain.review.Review;
import com.ssu.closet.dto.review.ReviewForm;
import com.ssu.closet.service.item.ItemService;
import com.ssu.closet.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReviewIndexController {
    private final ItemService itemService;
    private final ReviewService reviewService;

    @PostMapping("/review")
    public String create(@CurrentUser Member member, @Valid ReviewForm reviewForm) throws Exception {
        log.info("POST /review : " + reviewForm.toString());

        Item item = itemService.findItem(reviewForm.getItemId());

        if (reviewForm.getReviewId()!=null) {
            Optional<Review> optional = reviewService.findById(reviewForm.getReviewId());
            if (optional!=null) {
                Review review = optional.get();

                reviewService.updateReview(review,reviewForm);

                return "redirect:/store/detail/" + reviewForm.getItemId();
            }
        }

        reviewService.createReview(member,item,reviewForm);

        return "redirect:/store/detail/" + reviewForm.getItemId();
    }
//
//    // 리뷰 수정
//    @PutMapping("/review")
//    public void modify(Review review, @Valid ReviewForm reviewForm) throws Exception{
//        log.info("PUT data : " + reviewForm.toString());
//        reviewService.update(review, reviewForm);
//    }

    // 리뷰 삭제
    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId, @RequestParam("itemId") Long itemId ) {
        log.info("DELETE no : " + reviewId);

        reviewService.deleteReview(reviewId);

        return "redirect:/store/detail/" + itemId;
    }
}

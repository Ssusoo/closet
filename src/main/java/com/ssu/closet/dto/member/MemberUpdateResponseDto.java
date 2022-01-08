package com.ssu.closet.dto.member;

import com.ssu.closet.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberUpdateResponseDto {
     private Long id;
     private String email;
     private String passoword;

     public MemberUpdateResponseDto(Member entity) {
         this.id = entity.getId();
         this.email = entity.getEmail();
         this.passoword = entity.getPassword();
     }
}

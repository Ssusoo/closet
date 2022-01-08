package com.ssu.closet.dto.social;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {
    String token;

    @Builder
    public Token(String token) {
        this.token = token;
    }
}

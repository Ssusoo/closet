package com.ssu.closet.domain.member;

import org.springframework.security.core.GrantedAuthority;

public enum MemberStatus implements GrantedAuthority {
    USER, ADMIN;

    @Override public String getAuthority() {
        return name();
    }
}
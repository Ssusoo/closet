package com.ssu.closet.domain.social;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Long> {
    public Social findBySocialId(String socialId);
}

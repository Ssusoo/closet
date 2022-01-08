package com.ssu.closet.domain.social.google;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleSocialRepository extends JpaRepository<GoogleSocial, Long> {
    public GoogleSocial findByGoogleSocialId(String googleSocialId);
}

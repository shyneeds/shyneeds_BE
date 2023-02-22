package com.example.shyneeds_be.domain.member.repository;

import com.example.shyneeds_be.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository <Member, Long> {
    Optional<Member> findByEmail(String email);

    Member findByKakaoId(Long kakaoId);

}

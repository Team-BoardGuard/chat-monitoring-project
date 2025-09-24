package com.guard.communityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guard.communityservice.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// 닉네임으로 사용자가 존재하는지 확인
    boolean existsByNickname(String nickname);
}

package com.guard.communityservice.domain;



import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
	
	@Column(name = "username", nullable = false, length = 50)
    private String name;


    @Column(nullable = false, length = 255) // 해시된 비밀번호 저장을 위해 길이 넉넉하게 설정
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER; // 기본값 'USER'로 설정

	/*
	 * @Enumerated(EnumType.STRING) // Enum 이름을 DB에 문자열로 저장
	 * 
	 * @Column(name = "role",nullable = false, length = 20) private UserStatus
	 * status = UserStatus.ACTIVE; // 기본값 'ACTIVE'로 설정
	 */

    @CreationTimestamp // 엔티티 생성 시각을 자동으로 기록
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "warn_count", nullable = false)
    private int cumulativeCount = 0; // 기본값 0으로 설정
  //== 빌더 패턴을 사용한 생성자 ==//
    @Builder
    public User(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        //this.status = UserStatus.ACTIVE; // 생성 시 항상 활성 상태
        this.role = UserRole.USER;       // 생성 시 항상 일반 사용자
        this.cumulativeCount = 0;        // 생성 시 누적 횟수는 0
    }
}

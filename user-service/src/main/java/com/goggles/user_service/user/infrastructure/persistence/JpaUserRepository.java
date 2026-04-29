package com.goggles.user_service.user.infrastructure.persistence;

import com.goggles.user_service.user.domain.User;
import com.goggles.user_service.user.domain.UserId;
import com.goggles.user_service.user.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, UserId> {
}

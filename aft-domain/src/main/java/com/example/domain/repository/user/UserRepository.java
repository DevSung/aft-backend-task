package com.example.domain.repository.user;

import com.example.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "userRoles")
    Optional<User> findByIdx(Long idx);

    Optional<User> findFirstByUserId(String userId);
}

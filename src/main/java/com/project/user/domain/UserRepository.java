package com.project.user.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "userId")
    Optional<User> findByUserId(String userId);

    Boolean existsByUserId(String userId);
}

package com.project.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, UserAuthPk> {
}

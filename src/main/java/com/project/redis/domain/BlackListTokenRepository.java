package com.project.redis.domain;

import org.springframework.data.repository.CrudRepository;

public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {
}

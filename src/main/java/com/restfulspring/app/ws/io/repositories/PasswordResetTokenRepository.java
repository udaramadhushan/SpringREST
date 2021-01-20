package com.restfulspring.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;

import com.restfulspring.app.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

}

package com.java.springportfolio.dao;

import com.java.springportfolio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByEmail (String username);

    Optional<User> findByVerificationToken(String verificationToken);
}

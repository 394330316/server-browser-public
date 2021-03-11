package com.browser.repository;

import com.browser.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByPhoneNumber(String phoneNumber);
}

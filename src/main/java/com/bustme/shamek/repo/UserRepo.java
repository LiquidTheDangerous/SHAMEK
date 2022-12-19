package com.bustme.shamek.repo;
import com.bustme.shamek.domain.Role;
import com.bustme.shamek.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
//    User findById(Long id);
}
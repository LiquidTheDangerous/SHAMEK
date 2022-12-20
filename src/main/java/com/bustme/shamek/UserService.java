package com.bustme.shamek;

import com.bustme.shamek.domain.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    List<Object> isUserPresent(User user);
}
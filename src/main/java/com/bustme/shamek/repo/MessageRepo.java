package com.bustme.shamek.repo;

import com.bustme.shamek.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MessageRepo extends JpaRepository<Message,Long> {
    Set<Message> findMessagesByTag(String tag);
    Message findMessageById(Integer id);
}

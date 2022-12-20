package com.bustme.shamek.repo;

import com.bustme.shamek.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MessageRepo extends JpaRepository<Message,Long> {
//    Set<Message> findMessagesByTag(String tag);
    Message findMessageById(Integer id);

    Set<Message> findMessagesByUserUsername(String UserName);

    Set<Message> findMessagesByApproved(Boolean approved);

    void removeById(Integer id);
}

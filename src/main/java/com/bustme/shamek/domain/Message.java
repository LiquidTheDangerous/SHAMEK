package com.bustme.shamek.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="msg")
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String text;

    /*
    * К таблице msg будут присоединятся message_tags по Message.id = message_tags.message_id
    * One to many
    * */
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "message_tags", joinColumns = @JoinColumn(name = "message_id"))
    private Set<String> tag;
}

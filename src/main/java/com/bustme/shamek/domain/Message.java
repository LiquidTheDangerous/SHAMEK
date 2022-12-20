package com.bustme.shamek.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="msg")
public class Message implements Cloneable{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String text;
    private String title;
    /*
    * К таблице msg будут присоединятся message_tags по Message.id = message_tags.message_id
    * One to many
    * */
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "message_tags", joinColumns = @JoinColumn(name = "message_id"))
    private Set<String> tag;

    @ManyToOne
    private User user;

    private Boolean approved;
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public boolean isAuthor(User user){
      return this.user.getId().equals(user.getId());
    }

    public boolean isAuthor(String userName){
      return this.user.getUsername().equals(userName);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTag() {
        return tag;
    }

    public void setTag(Set<String> tag) {
        this.tag = tag;
    }

    public String getTags() {
        StringBuilder result = new StringBuilder(this.tag.size()*2);
        for(String t: tag) {
            result.append("#");
            result.append(t);
        }
        return result.toString();
    }
}

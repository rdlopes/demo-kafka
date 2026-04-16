package io.github.rdlopes.kafka.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "accounts")
public class AccountEntity implements Serializable {

  @Id
  private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private UserEntity user;

  private long timestamp;

  public AccountEntity() {
  }

  public AccountEntity(String id, UserEntity user, long timestamp) {
    this.id = id;
    this.user = user;
    this.timestamp = timestamp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}

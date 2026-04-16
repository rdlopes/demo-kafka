package io.github.rdlopes.kafka.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String email;

    private long timestamp;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<AccountEntity> accounts = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(String id, String name, String email, long timestamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }
}

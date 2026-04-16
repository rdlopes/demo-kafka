package io.github.rdlopes.kafka.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByUser_Id(String userId);
}

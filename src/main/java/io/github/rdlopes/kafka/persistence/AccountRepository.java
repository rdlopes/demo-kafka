package io.github.rdlopes.kafka.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
  List<AccountEntity> findByUser_Id(String userId);
}

package io.github.rdlopes.kafka.service;

import io.github.rdlopes.kafka.avro.AccountEvent;
import io.github.rdlopes.kafka.avro.UserEvent;
import io.github.rdlopes.kafka.persistence.AccountEntity;
import io.github.rdlopes.kafka.persistence.AccountRepository;
import io.github.rdlopes.kafka.persistence.UserEntity;
import io.github.rdlopes.kafka.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public void processUserEvent(UserEvent event) {
        log.info("Processing UserEvent: {}", event);
        UserEntity entity = new UserEntity(
                event.getId(),
                event.getName(),
                event.getEmail(),
                event.getTimestamp()
        );
        userRepository.save(entity);
        log.info("Saved UserEntity: {}", entity.getId());
    }

    public void processAccountEvent(AccountEvent event) {
        log.info("Processing AccountEvent: {}", event);
        UserEntity user = userRepository.findById(event.getUserId())
                                        .orElseThrow(() -> new RuntimeException("User not found: " + event.getUserId()));
        AccountEntity entity = new AccountEntity(
                event.getId(),
                user,
                event.getTimestamp()
        );
        accountRepository.save(entity);
        log.info("Saved AccountEntity: {}", entity.getId());
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findUserById(String id) {
        return userRepository.findById(id);
    }
}

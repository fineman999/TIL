package io.chan.springcoresecurity.repository;


import io.chan.springcoresecurity.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}

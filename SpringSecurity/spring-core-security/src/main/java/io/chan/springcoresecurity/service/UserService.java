package io.chan.springcoresecurity.service;


import io.chan.springcoresecurity.domain.dto.AccountDto;
import io.chan.springcoresecurity.domain.entity.Account;

import java.util.List;

public interface UserService {

    void createUser(Account account);

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long idx);
}

package io.chan.accountservice.service;

import com.popbill.api.EasyFinBankService;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.easyfin.EasyFinBankAccountForm;
import io.chan.accountservice.domain.BankCode;
import io.chan.accountservice.util.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AccountServiceTest {
    @Autowired
    private EasyFinBankService easyFinBankService;


    @Test
    void registerAccount() throws PopbillException {
        EasyFinBankAccountForm accountRequest = new EasyFinBankAccountForm();
        accountRequest.setBankCode(BankCode.getCodeByName("농협"));
        accountRequest.setAccountNumber("3021834303711");
        accountRequest.setAccountPWD("5989");
        accountRequest.setAccountType("개인");
        accountRequest.setIdentityNumber("940221");

        Response response = easyFinBankService.registBankAccount(
                AccountUtils.corpNum,
                accountRequest,
                AccountUtils.userId
        );

        assertNotNull(response);
        log.info("response: {}", response);

    }
    @Test
    void registerAccountOur() throws PopbillException {
        EasyFinBankAccountForm accountRequest = new EasyFinBankAccountForm();
        accountRequest.setBankCode(BankCode.getCodeByName("우리은행"));
        accountRequest.setAccountNumber("1002253548314");
        accountRequest.setAccountPWD("7985");
        accountRequest.setAccountType("개인");
        accountRequest.setIdentityNumber("980325");

        Response response = easyFinBankService.registBankAccount(
                AccountUtils.corpNum,
                accountRequest,
                AccountUtils.userId
        );

        assertNotNull(response);
        log.info("response: {}", response);

    }
}
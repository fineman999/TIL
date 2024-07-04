package io.chan.accountservice.service;

import com.popbill.api.EasyFinBankService;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import io.chan.accountservice.controller.request.AccountRequest;
import io.chan.accountservice.controller.request.SaveAccountRequest;
import io.chan.accountservice.util.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final EasyFinBankService easyFinBankService;


    public String getJobId(AccountRequest accountRequest) {
        try {
            return easyFinBankService.requestJob(
                    accountRequest.testCorpNum(),
                    accountRequest.backCode(),
                    accountRequest.accountNumber(),
                    accountRequest.sDate(),
                    accountRequest.eDate()
            );
        } catch (PopbillException e) {
            throw new RuntimeException(e);
        }
    }

    public Response registerAccount(SaveAccountRequest accountRequest) {
        try {
            return easyFinBankService.registBankAccount(
                    AccountUtils.corpNum,
                    accountRequest.easyFinBankAccountForm(),
                    AccountUtils.userId
            );
        } catch (PopbillException e) {
            throw new RuntimeException(e);
        }
    }
}

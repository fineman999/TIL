package io.chan.accountservice.controller.request;

import org.springframework.util.Assert;

public record AccountRequest(
        String testCorpNum,
        String backCode,
        String accountNumber,
        String sDate,
        String eDate
) {
    public AccountRequest {
        Assert.hasText(testCorpNum, "testCorpNum must not be empty");
        Assert.hasText(backCode, "backCode must not be empty");
        Assert.hasText(accountNumber, "accountNumber must not be empty");
        Assert.hasText(sDate, "sDate must not be empty");
        Assert.hasText(eDate, "eDate must not be empty");
    }
}

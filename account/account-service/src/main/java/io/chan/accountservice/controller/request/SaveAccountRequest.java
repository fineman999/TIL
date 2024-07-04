package io.chan.accountservice.controller.request;

import com.popbill.api.easyfin.EasyFinBankAccountForm;

public record SaveAccountRequest(
        EasyFinBankAccountForm easyFinBankAccountForm
) {
}

package io.chan.accountservice.controller;

import com.popbill.api.Response;
import io.chan.accountservice.controller.request.AccountRequest;
import io.chan.accountservice.controller.request.SaveAccountRequest;
import io.chan.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/job-id")
    public ResponseEntity<String> getJobId(
            @RequestBody AccountRequest accountRequest
    ) {
        String jobId = accountService.getJobId(accountRequest);
        return ResponseEntity.ok(jobId);
    }

    @PostMapping
    public ResponseEntity<Response> saveAccount(
            @RequestBody SaveAccountRequest accountRequest
            ) {
        Response response = accountService.registerAccount(accountRequest);
        return ResponseEntity.ok(response);
    }
}

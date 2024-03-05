package io.chan.springngrindertest.controller;

import io.chan.springngrindertest.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class SendController {
    private final SendService sendService;


    @GetMapping("/logs/async")
    public ResponseEntity<Object> sendAll() {
        return new ResponseEntity<>(sendService.sendAllAsync(), HttpStatus.OK);
    }

    @GetMapping("/logs/sync")
    public ResponseEntity<Object> sendAllSync() {
        return new ResponseEntity<>(sendService.sendAllSync(), HttpStatus.OK);
    }
}

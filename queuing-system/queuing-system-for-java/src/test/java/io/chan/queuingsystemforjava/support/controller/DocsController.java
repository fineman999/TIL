package io.chan.queuingsystemforjava.support.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test/docs")
public class DocsController {

    public record HelloRequest(String name) {}

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(@RequestParam("name") String name) {
        Map<String, String> map = new HashMap<>();
        map.put("hello", name);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/hello/{test}")
    public ResponseEntity<Map<String, String>> hello2(
            @PathVariable("test") Long testVariable, @RequestBody HelloRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("hello", request.name);
        map.put("pathVariable", testVariable.toString());
        return ResponseEntity.ok(map);
    }
}

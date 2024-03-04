package io.chan.springngrindertest.controller;


import io.chan.springngrindertest.domain.Notice;
import io.chan.springngrindertest.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeService noticeService;


    @GetMapping
    public ResponseEntity<Object> findAll() {
        List<Notice> notices = noticeService.getAllNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Object> findByPage(
            Pageable pageable
            ) {

        final Page<Notice> notices = noticeService.findByPage(pageable);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/dates")
    public ResponseEntity<Object> findNoticesByDates(@RequestParam("startDate") String startDate,
                                                     @RequestParam("endDate") String endDate) {
        List<Notice> notices = noticeService.findNoticesByDates(
                new NoticesByDatesDto(startDate, endDate)
        );
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

}

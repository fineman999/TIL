package io.chan.springngrindertest.service;

import io.chan.springngrindertest.domain.Notice;
import io.chan.springngrindertest.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
}

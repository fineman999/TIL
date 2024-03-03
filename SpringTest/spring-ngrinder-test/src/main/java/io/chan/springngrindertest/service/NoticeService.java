package io.chan.springngrindertest.service;

import io.chan.springngrindertest.config.CacheUtils;
import io.chan.springngrindertest.domain.Notice;
import io.chan.springngrindertest.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Cacheable(value = CacheUtils.NOTICE_FIND_ALL)
    @Transactional(readOnly = true)
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Cacheable(value = CacheUtils.NOTICE_FIND_BY_PAGE, condition = "#pageable.pageNumber() <= 5")
    @Transactional(readOnly = true)
    public Page<Notice> findByPage(final Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }
}

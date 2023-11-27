package io.chan.springcoresecurity.aopsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AopLiveMethodService {
    public void liveMethodSecured() {
        log.info("liveMethodSecured");
    }
}

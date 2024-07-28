package io.chan.popbillservice;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PopbillConfig {

    @Value("${popbill.dev.userId}")
    private String userId;

    @Value("${popbill.dev.corpNum}")
    private String corpNum;

}

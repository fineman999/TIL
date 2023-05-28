package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoratorV2 extends AbstractDecorator{


    public MessageDecoratorV2(Component component) {
        super(component);
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String operation = super.operation();
        String decoResult = "*****" + operation + "*****";
        log.info("MessageDecorator 꾸미기 적용 전 ={}, 적용 후 ={}", operation, decoResult);
        return decoResult;
    }
}

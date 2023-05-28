package hello.proxy.pureproxy.decorator.code;

public abstract class AbstractDecorator implements Component{
    protected final Component component;

    protected AbstractDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        return component.operation();
    }
}

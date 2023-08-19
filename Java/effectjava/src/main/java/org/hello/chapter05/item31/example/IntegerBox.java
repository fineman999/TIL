package org.hello.chapter05.item31.example;

public class IntegerBox extends Box<Integer>{
    private final String message;
    public IntegerBox(Integer value, String message) {
        super(value);
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IntegerBox{");
        sb.append("message='").append(message).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}

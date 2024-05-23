package io.chan.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MarshallerUtil {
  public static String toJson(Object value) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(value);
  }

  // marshallerFor 메소드 구현
  public static <T> MethodDescriptor.Marshaller<T> marshallerFor(Class<T> clz) {
    Gson gson = new Gson();
    return new MethodDescriptor.Marshaller<T>() {
      @Override
      public InputStream stream(T value) {
        // 객체를 JSON 문자열로 변환하고, 이를 바이트 배열로 변환한 후 ByteArrayInputStream으로 감쌈
        return new ByteArrayInputStream(gson.toJson(value, clz).getBytes(StandardCharsets.UTF_8));
      }

      @Override
      public T parse(InputStream stream) {
        // InputStream으로부터 JSON 문자열을 읽어 객체로 변환
        return gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), clz);
      }

      public String toJson(T value) {
        return gson.toJson(value, clz);
      }
    };
  }
}

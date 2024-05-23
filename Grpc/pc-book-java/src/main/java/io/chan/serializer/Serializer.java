package io.chan.serializer;

import com.google.protobuf.DescriptorProtos;
import io.chan.Laptop;
import io.grpc.MethodDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Serializer {
  public void WriteBinaryFile(Laptop laptop, String filename) {
    try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
      laptop.writeTo(fileOutputStream);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Laptop ReadBinaryFile(String filename) {
    try (FileInputStream fileInputStream = new FileInputStream(filename)) {
      return Laptop.parseFrom(fileInputStream);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void WriteJSONFile(Laptop laptop, String filename) {
    final String json = MarshallerUtil.toJson(laptop);

    try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
      fileOutputStream.write(json.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    Serializer serializer = new Serializer();
    Laptop laptop = serializer.ReadBinaryFile("laptop.bin");
    serializer.WriteJSONFile(laptop, "laptop.json");
  }
}

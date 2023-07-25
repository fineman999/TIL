package org.hello.item03.serialization;

import java.io.*;

public class SerializationExample {
    public void serialize(Book book) {
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("book.obj"))) {
            out.writeObject(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Book deserialize() {
        try (ObjectInput in = new ObjectInputStream(new FileInputStream("book.obj"))){
            return (Book) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

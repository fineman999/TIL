package io.chan.service;

public class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(final String laptopWithIdAlreadyExists) {
    super(laptopWithIdAlreadyExists);
  }
}

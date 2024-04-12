package com.practice.kafka.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutionException;

public class FileEventSource implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(FileEventSource.class.getName());
  private boolean keepRunning = true;
  private long filePointer = 0;
  private File file;
  private int updateInterval;
  private EventHandler eventHandler;

  public FileEventSource(File file, int updateInterval, EventHandler eventHandler) {
    this.file = file;
    this.updateInterval = updateInterval;
    this.eventHandler = eventHandler;
  }

  @Override
  public void run() {

    try {
      while (this.keepRunning) {
        Thread.sleep(this.updateInterval);
        // file의 변경을 감지하고, 변경이 있을 경우 이벤트를 발생시킨다.
        long length = this.file.length();
        if (length < this.filePointer) {
          logger.info("File was reset as filePointer is larger than file length");
          this.filePointer = length;
        } else if (length > this.filePointer) {
          // 파일이 커졌을 경우
          readAppendAndSend();
        } else {
          // 파일이 변화가 없을 경우
        }
      }
    } catch (InterruptedException | ExecutionException | IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void readAppendAndSend() throws IOException, ExecutionException, InterruptedException {
    // 파일을 읽어서 이벤트를 발생시킨다.
    RandomAccessFile raf = new RandomAccessFile(this.file, "r");
    raf.seek(this.filePointer);
    String line;
    while ((line = raf.readLine()) != null) {
      sendMessage(line);
    }
    // file이 변경되었으므로 file의 filePointer를 현재 file의 마지막으로 재 설정함
    this.filePointer = raf.getFilePointer();
    raf.close();
  }

  private void sendMessage(String line) throws ExecutionException, InterruptedException {
    String[] tokens = line.split(",");
    String key = tokens[0];
    StringBuffer value = new StringBuffer();
    for (int i = 1; i < tokens.length; i++) {
      value.append(tokens[i]);
      if (i < tokens.length - 1) {
        value.append(",");
      }
    }
    MessageEvent messageEvent = new MessageEvent(key, value.toString());

    this.eventHandler.onMessage(messageEvent);
  }
}

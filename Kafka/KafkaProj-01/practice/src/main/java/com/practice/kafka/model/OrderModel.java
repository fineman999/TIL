package com.practice.kafka.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderModel implements Serializable {
  private String orderId;
  private String shopId;
  private String menuName;
  private String userName;
  private String phoneNumber;
  private String address;
  private LocalDateTime orderTime;

  public OrderModel(
      final String orderId,
      final String shopId,
      final String menuName,
      final String userName,
      final String phoneNumber,
      final String address,
      final LocalDateTime orderTime) {
    this.orderId = orderId;
    this.shopId = shopId;
    this.menuName = menuName;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.orderTime = orderTime;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getShopId() {
    return shopId;
  }

  public String getMenuName() {
    return menuName;
  }

  public String getUserName() {
    return userName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public LocalDateTime getOrderTime() {
    return orderTime;
  }

  public void setOrderId(final String orderId) {
    this.orderId = orderId;
  }

  public void setShopId(final String shopId) {
    this.shopId = shopId;
  }

  public void setMenuName(final String menuName) {
    this.menuName = menuName;
  }

  public void setUserName(final String userName) {
    this.userName = userName;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public void setOrderTime(final LocalDateTime orderTime) {
    this.orderTime = orderTime;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderModel{");
    sb.append("orderId='").append(orderId).append('\'');
    sb.append(", shopId='").append(shopId).append('\'');
    sb.append(", menuName='").append(menuName).append('\'');
    sb.append(", userName='").append(userName).append('\'');
    sb.append(", phoneNumber='").append(phoneNumber).append('\'');
    sb.append(", address='").append(address).append('\'');
    sb.append(", orderTime=").append(orderTime);
    sb.append('}');
    return sb.toString();
  }
}

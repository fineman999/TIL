package io.chan.apiservice;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductVO implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private Long id;
  private String name;
  private int price;
  private String type;
}
package br.com.gui.aws_app02.model;

import br.com.gui.aws_app02.enums.EventType;
import lombok.Getter;

@Getter
public class ProductEventLogDto {

  private final EventType eventType;
  private final String code;
  private final long productId;
  private final String username;
  private final long timestamp;

  public ProductEventLogDto(ProductEventLog productEventLog) {
    this.eventType = productEventLog.getEventType();
    this.code = productEventLog.getPk();
    this.productId = productEventLog.getProductId();
    this.username = productEventLog.getUsername();
    this.timestamp = productEventLog.getTimestamp();
  }
}

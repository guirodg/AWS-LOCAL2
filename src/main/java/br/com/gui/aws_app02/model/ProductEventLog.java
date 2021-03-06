package br.com.gui.aws_app02.model;

import br.com.gui.aws_app02.enums.EventType;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "product-events")
public class ProductEventLog {
  public ProductEventLog() {
  }

  @Id
  private ProductEventKey productEventKey;

  @Getter
  @Setter
  @DynamoDBTypeConvertedEnum
  @DynamoDBAttribute(attributeName = "eventType")
  private EventType eventType;

  @Getter
  @Setter
  @DynamoDBAttribute(attributeName = "productId")
  private long productId;

  @Getter
  @Setter
  @DynamoDBAttribute(attributeName = "username")
  private String username;

  @Getter
  @Setter
  @DynamoDBAttribute(attributeName = "timestamp")
  private long timestamp;

  @Getter
  @Setter
  @DynamoDBAttribute(attributeName = "ttl")
  private long ttl;

  @DynamoDBHashKey(attributeName = "pk")
  public String getPk() {
    return this.productEventKey != null ? this.productEventKey.getPk() : null;
  }

  public void setPk(String pk){
    if(this.productEventKey == null) this.productEventKey = new ProductEventKey();
    this.productEventKey.setPk(pk);
  }

  @DynamoDBRangeKey(attributeName = "sk")
  public String getSk() {
    return this.productEventKey != null ? this.productEventKey.getSk() : null;
  }

  public void setSk(String sk) {
    if(this.productEventKey == null) this.productEventKey = new ProductEventKey();
    this.productEventKey.setSk(sk);
  }
}

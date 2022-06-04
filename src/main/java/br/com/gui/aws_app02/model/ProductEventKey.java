package br.com.gui.aws_app02.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductEventKey {
  public ProductEventKey() {
  }

  @DynamoDBHashKey(attributeName = "pk")
  private String pk;

  @DynamoDBRangeKey(attributeName = "sk")
  private String sk;
}

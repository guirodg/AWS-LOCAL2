package br.com.gui.aws_app02.config.local;

import br.com.gui.aws_app02.repository.ProductEventLogRepository;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@Profile("local")
@EnableDynamoDBRepositories(basePackageClasses = ProductEventLogRepository.class)
public class DynamoDBConfigLocal {

  private final AmazonDynamoDB amazonDynamoDB;

  public DynamoDBConfigLocal() {
    amazonDynamoDB = AmazonDynamoDBClient.builder()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:4566",
            Regions.US_EAST_1.getName()))
        .withCredentials(new DefaultAWSCredentialsProviderChain()).build();

    final var dynamoDB = new DynamoDB(amazonDynamoDB);
    List<AttributeDefinition> attributeDefinitionList = new ArrayList<>();
    attributeDefinitionList.add(new AttributeDefinition().withAttributeName("pk").withAttributeType(ScalarAttributeType.S));
    attributeDefinitionList.add(new AttributeDefinition().withAttributeName("sk").withAttributeType(ScalarAttributeType.S));

    List<KeySchemaElement> keySchemaElementList = new ArrayList<>();
    keySchemaElementList.add(new KeySchemaElement().withAttributeName("pk").withKeyType(KeyType.HASH));
    keySchemaElementList.add(new KeySchemaElement().withAttributeName("sk").withKeyType(KeyType.RANGE));

    final var request = new CreateTableRequest()
        .withTableName("product-events")
        .withKeySchema(keySchemaElementList)
        .withAttributeDefinitions(attributeDefinitionList)
        .withBillingMode(BillingMode.PAY_PER_REQUEST);

    final var table = dynamoDB.createTable(request);

    try {
      table.waitForActive();
    } catch (InterruptedException e) {
      log.error("Erro ao criar table message: {} ", e.getMessage());
    }

  }

  @Bean
  @Primary
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Bean
  @Primary
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    return new DynamoDBMapper(amazonDynamoDB, config);
  }

  @Bean
  @Primary
  public AmazonDynamoDB amazonDynamoDB() {
    return this.amazonDynamoDB;
  }
}

package br.com.gui.aws_app02.service;

import br.com.gui.aws_app02.model.Envelop;
import br.com.gui.aws_app02.model.ProductEvent;
import br.com.gui.aws_app02.model.ProductEventLog;
import br.com.gui.aws_app02.model.SnsMessage;
import br.com.gui.aws_app02.repository.ProductEventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class ProductEventConsumer {

  private final ObjectMapper objectMapper;

  @Autowired
  private final ProductEventLogRepository productEventLogRepository;

  public ProductEventConsumer(ObjectMapper objectMapper, ProductEventLogRepository productEventLogRepository) {
    this.objectMapper = objectMapper;
    this.productEventLogRepository = productEventLogRepository;
  }

  @JmsListener(destination = "${aws.sns.queue.product.events.name}")
  public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

    final var snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);
    final var envelop = objectMapper.readValue(snsMessage.getMessage(), Envelop.class);
    final var productEvent = objectMapper.readValue(envelop.getData(), ProductEvent.class);

    log.info("Product event received - Event: {} - Product: {} -", envelop.getEventType(), productEvent.getProductId());

    final var productEventLog = builProductEventLog(envelop, productEvent);

    productEventLogRepository.save(productEventLog);

  }

  private ProductEventLog builProductEventLog(Envelop envelop, ProductEvent productEvent) {
    final var timestamp = Instant.now().toEpochMilli();

    final var productEventLog = new ProductEventLog();
    productEventLog.setPk(productEvent.getCode());
    productEventLog.setSk(envelop.getEventType() + "_" + timestamp);
    productEventLog.setEventType(envelop.getEventType());
    productEventLog.setProductId(productEvent.getProductId());
    productEventLog.setUsername(productEvent.getUsername());
    productEventLog.setTimestamp(timestamp);
    productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());
    return productEventLog;
  }
}

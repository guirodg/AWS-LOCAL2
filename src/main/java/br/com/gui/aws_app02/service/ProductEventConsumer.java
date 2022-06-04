package br.com.gui.aws_app02.service;

import br.com.gui.aws_app02.model.Envelop;
import br.com.gui.aws_app02.model.ProductEvent;
import br.com.gui.aws_app02.model.SnsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Slf4j
@Service
public class ProductEventConsumer {

  private final ObjectMapper objectMapper;

  public ProductEventConsumer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @JmsListener(destination = "@{aws.sns.queue.product.events.name}")
  public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

    final var snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);
    final var envelop = objectMapper.readValue(snsMessage.getMessage(), Envelop.class);
    final var productEvent = objectMapper.readValue(envelop.getData(), ProductEvent.class);

    log.info("Product event received - Event: {} - Product: {} -", envelop.getEventType(), productEvent.getProductId());
  }
}

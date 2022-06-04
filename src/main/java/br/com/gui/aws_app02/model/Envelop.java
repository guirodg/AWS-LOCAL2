package br.com.gui.aws_app02.model;

import br.com.gui.aws_app02.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelop {
  private EventType eventType;
  private String data;
}

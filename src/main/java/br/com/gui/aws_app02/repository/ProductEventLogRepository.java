package br.com.gui.aws_app02.repository;

import br.com.gui.aws_app02.model.ProductEventKey;
import br.com.gui.aws_app02.model.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductEventLogRepository extends CrudRepository<ProductEventLog, ProductEventKey> {

  List<ProductEventLog> findAllByPk(String code);

  List<ProductEventLog> findAllByPkAndSkStartsWith(String code, String eventType);
}

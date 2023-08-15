package com.prasanna.products.consumer;

import com.prasanna.products.Repository.ApprovalRepository;
import com.prasanna.products.config.MessagingConfig;
import com.prasanna.products.Entity.PendingProduct;
import com.prasanna.products.dto.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApprovalConsumer {


  private ApprovalRepository approvalRepository;
  @Autowired
  public ApprovalConsumer(ApprovalRepository approvalRepository){
    this.approvalRepository = approvalRepository;
  }
  @RabbitListener(queues = MessagingConfig.QUEUE)
  public void consumeMessageFromQueue(ProductStatus productStatus) {
    log.info("Message Consumed in approval queue - [{}]", productStatus);
    PendingProduct pendingProduct = PendingProduct.builder()
        .productName(productStatus.getProduct().getProductName())
        .price(productStatus.getProduct().getPrice())
        .productArrivalDate(productStatus.getProduct().getProductArrivalDate())
        .productAvailability(productStatus.getProduct().getProductAvailability())
        .build();
    approvalRepository.save(pendingProduct);
  }
}

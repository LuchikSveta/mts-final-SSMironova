package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

@Log4j2
@Component
public class CheckAvailableDelegate implements JavaDelegate {

    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        String productCode = execution.getVariable(VariablesConstant.PRODUCT_CODE).toString();
        Integer quantity = (Integer) execution.getVariable(VariablesConstant.QUANTITY);
        String status = execution.getVariable(VariablesConstant.STATUS).toString();
        String fio = execution.getVariable(VariablesConstant.FIO).toString();
        String phone = execution.getVariable(VariablesConstant.PHONE).toString();
        String address = execution.getVariable(VariablesConstant.ADDRESS).toString();
        OrderMessage orderMessage = new OrderMessage(orderId, productCode, quantity, status, fio, phone, address);
        log.info("Проверка наличия товара {} на складе по заказу Id = {}", productCode, orderId);
        kafkaTemplate.send("check_available_request", orderId, orderMessage);
    }

}

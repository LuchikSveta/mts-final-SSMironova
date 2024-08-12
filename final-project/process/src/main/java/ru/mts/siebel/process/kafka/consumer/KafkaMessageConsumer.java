package ru.mts.siebel.process.kafka.consumer;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.model.StartMessage;
import ru.mts.siebel.model.StatusMessage;
import ru.mts.siebel.process.bpm.constant.CorrelateMessageConstants;
import ru.mts.siebel.process.bpm.constant.StatusConstant;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

import java.util.Map;

@Log4j2
@Service
public class KafkaMessageConsumer {

    @Autowired
    private RuntimeService runtimeService;

    @KafkaListener(topics = "start_process", containerFactory = "startMessageKafkaListenerContainerFactory")
    public void startProcessListener(final StartMessage startMessage) {
        log.info("Запуск процесса Camunda для заказа Id = {}", startMessage.getId());
        Map<String, Object> variables = Map.of(
                VariablesConstant.ORDER_ID, startMessage.getId(),
                VariablesConstant.PRODUCT_CODE, startMessage.getProductCode(),
                VariablesConstant.QUANTITY, startMessage.getQuantity(),
                VariablesConstant.STATUS, startMessage.getStatus(),
                VariablesConstant.FIO, startMessage.getFio(),
                VariablesConstant.PHONE, startMessage.getPhone(),
                VariablesConstant.ADDRESS, startMessage.getAddress());
        runtimeService.startProcessInstanceByKey("final", startMessage.getId(), variables);
    }

    @KafkaListener(topics = "check_available_response", containerFactory = "orderMessageKafkaListenerContainerFactory")
    public void checkAvailableResponseListener(final OrderMessage orderMessage) {
        String orderId = orderMessage.getId();
        String productCode = orderMessage.getProductCode();
        String status = orderMessage.getStatus();
        log.info("Получен ответ для заказа Id = {} по наличию товара {} на складе", orderId, productCode);
        if (status.equals(StatusConstant.RESERVED)) {
            log.info("По заказу Id = {} все в наличии, товар зарезервирован", orderId);
        } else {
            log.info("Недостаточно товаров на складе для заказа Id = {}", orderId);
        }
        runtimeService.createMessageCorrelation(CorrelateMessageConstants.ORDER_RESERVED)
                .processInstanceBusinessKey(orderMessage.getId())
                .setVariable(VariablesConstant.STATUS, orderMessage.getStatus())
                .correlate();
    }

    @KafkaListener(topics = "assembly_order_response", containerFactory = "statusMessageKafkaListenerContainerFactory")
    public void orderAssemblyListener(final StatusMessage statusMessage) {
        String orderId = statusMessage.getOrderId();
        String status = statusMessage.getStatus();
        log.info("Получен ответ по сборке заказа Id = {}", orderId);
        if (status.equals(StatusConstant.ASSEMBLED)) {
            log.info("Сборка заказа Id = {} выполнена успешно", orderId);
        } else {
            log.info("Не удалось выполнить сборку заказа Id = {}, произошла ошибка", orderId);
        }
        runtimeService.createMessageCorrelation(CorrelateMessageConstants.ORDER_ASSEMBLED)
                .processInstanceBusinessKey(orderId)
                .setVariable(VariablesConstant.STATUS, status)
                .correlate();
    }

    @KafkaListener(topics = "delivery_order_response", containerFactory = "statusMessageKafkaListenerContainerFactory")
    public void orderDeliveryListener(final StatusMessage statusMessage) {
        String orderId = statusMessage.getOrderId();
        String status = statusMessage.getStatus();
        log.info("Получен ответ по доставке заказа Id = {}", orderId);
        if (status.equals(StatusConstant.DELIVERED)) {
            log.info("Доставка заказа Id = {} выполнена успешно", orderId);
        } else {
            log.info("Не удалось доставить заказа Id = {}, приносим свои извинения", orderId);
        }
        runtimeService.createMessageCorrelation(CorrelateMessageConstants.ORDER_DELIVERED)
                .processInstanceBusinessKey(orderId)
                .setVariable(VariablesConstant.STATUS, status)
                .correlate();
    }

}

package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.process.bpm.constant.StatusConstant;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

@Log4j2
@Component
public class SendForPackagingDelegate implements JavaDelegate {

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        log.info("Заявка на упаковку заказа Id = {} принята", orderId);
        execution.setVariable(VariablesConstant.STATUS, StatusConstant.PACKED);
        log.info("Упаковка заказа Id = {} выполнена успешно", orderId);
    }

}

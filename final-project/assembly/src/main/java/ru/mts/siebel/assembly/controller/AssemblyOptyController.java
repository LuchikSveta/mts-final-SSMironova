package ru.mts.siebel.assembly.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.siebel.assembly.entity.AssemblyOpty;
import ru.mts.siebel.assembly.service.AssemblyOptyService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/assembly")
public class AssemblyOptyController {

    @Autowired
    private AssemblyOptyService assemblyOptyService;

    @PostMapping("/all")
    public List<AssemblyOpty> getOrders() {
        return assemblyOptyService.findAll();
    }


    @PostMapping("/new")
    public List<AssemblyOpty> getNewOrders() {
        return assemblyOptyService.findNew();
    }

    @PostMapping("/status")
    public ResponseEntity<String> orderCheck(@RequestParam String orderId, @RequestParam String status) {
        log.info("Выполнение сборки заказа Id = {}", orderId);
        assemblyOptyService.updateAssemblyOrderStatus(orderId, status);
        return ResponseEntity.ok("Assembly opty is completed");
    }

}

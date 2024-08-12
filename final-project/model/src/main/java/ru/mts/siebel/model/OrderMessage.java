package ru.mts.siebel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {

    private String id;
    private String productCode;
    private Integer quantity;
    private String status;
    private String fio;
    private String phone;
    private String address;

}

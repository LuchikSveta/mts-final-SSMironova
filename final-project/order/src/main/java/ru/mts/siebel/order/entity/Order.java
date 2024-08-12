package ru.mts.siebel.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    private String id;
    private LocalDate created;
    private String status;
    @Column(name = "product_code")
    private String productCode;
    private Integer quantity;
    private String fio;
    private String phone;
    private String email;
    private String address;

}

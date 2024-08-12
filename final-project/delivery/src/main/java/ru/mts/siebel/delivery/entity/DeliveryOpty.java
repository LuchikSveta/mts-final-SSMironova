package ru.mts.siebel.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "delivery_opty")
public class DeliveryOpty {

    @Id
    private String id;
    private LocalDate created;
    @Column(name = "order_id")
    private String orderId;
    private String status;
    private String fio;
    private String phone;
    private String address;

}

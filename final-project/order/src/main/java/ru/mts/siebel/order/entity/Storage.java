package ru.mts.siebel.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "storage")
public class Storage {

    @Id
    private String id;
    @Column(name = "product_code")
    private String productCode;
    private Integer quantity;

}

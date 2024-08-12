package ru.mts.siebel.assembly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "assembly_opty")
public class AssemblyOpty {

    @Id
    private String id;
    private LocalDate created;
    @Column(name = "order_id")
    private String orderId;
    private String status;

}

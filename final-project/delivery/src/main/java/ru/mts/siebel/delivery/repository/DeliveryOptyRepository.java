package ru.mts.siebel.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.delivery.entity.DeliveryOpty;

import java.util.List;

@Repository
public interface DeliveryOptyRepository extends JpaRepository<DeliveryOpty, String> {

    List<DeliveryOpty> findByStatus(String status);

}

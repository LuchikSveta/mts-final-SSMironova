package ru.mts.siebel.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.order.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, String> {
}

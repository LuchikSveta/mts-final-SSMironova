package ru.mts.siebel.assembly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.assembly.entity.AssemblyOpty;

import java.util.List;

@Repository
public interface AssemblyOptyRepository extends JpaRepository<AssemblyOpty, String> {

    List<AssemblyOpty> findByStatus(String status);

}

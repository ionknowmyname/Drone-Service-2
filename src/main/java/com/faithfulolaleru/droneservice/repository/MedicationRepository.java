package com.faithfulolaleru.droneservice.repository;

import com.faithfulolaleru.droneservice.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {


    Optional<Medication> findByCode(String code);

    Optional<Medication> findByName(String name);
}

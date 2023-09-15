package com.faithfulolaleru.droneservice.repository;


import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, UUID> {


    Optional<Drone> findBySerial(UUID serial);

    List<Drone> findAllByState(DroneState state);

    List<Drone> findAll();
}

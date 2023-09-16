package com.faithfulolaleru.droneservice.repository;


import com.faithfulolaleru.droneservice.entity.DroneBatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DroneBatteryLogRepository extends JpaRepository<DroneBatteryLog, UUID> {

    // @Query("SELECT d FROM drone_battery_log_table AS d WHERE d.droneSerial = ?1")
    List<DroneBatteryLog> findAllByDroneSerial(UUID droneSerial);
}

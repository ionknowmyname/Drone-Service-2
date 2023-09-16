package com.faithfulolaleru.droneservice.services;


import com.faithfulolaleru.droneservice.dtos.DroneBatteryLogResponse;
import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.entity.DroneBatteryLog;
import com.faithfulolaleru.droneservice.repository.DroneBatteryLogRepository;
import com.faithfulolaleru.droneservice.repository.DroneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneBatteryLogService {

    private final DroneBatteryLogRepository droneBatteryLogRepository;

    private final DroneRepository droneRepository;

    private final ModelMapper modelMapper;


    public boolean saveDroneBatteryLog() {

        List<Drone> allDrones = droneRepository.findAll();

        List<DroneBatteryLog> toSave = allDrones.stream()
                .map(drone -> buildDroneBatteryLogEntity(drone))
                .collect(Collectors.toList());

        List<DroneBatteryLog> saved = droneBatteryLogRepository.saveAll(toSave);

        return true;
    }


    public List<DroneBatteryLogResponse> getAllBatteryLogsByDroneSerial(UUID serial) {

        return droneBatteryLogRepository.findAllByDroneSerial(serial).stream()
                .map(dbl -> modelMapper.map(dbl, DroneBatteryLogResponse.class))
                .collect(Collectors.toList());
    }
    private DroneBatteryLog buildDroneBatteryLogEntity(Drone entity) {

        return DroneBatteryLog.builder()
            .id(UUID.randomUUID())
            .droneSerial(entity.getSerial())
            .batteryPercentage(entity.getBatteryCapacity())
            .createdAt(LocalDateTime.now())
            .build();
    }
}

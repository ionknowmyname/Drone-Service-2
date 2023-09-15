package com.faithfulolaleru.droneservice.services;


import com.faithfulolaleru.droneservice.dtos.DroneRequest;
import com.faithfulolaleru.droneservice.dtos.DroneResponse;
import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.faithfulolaleru.droneservice.exception.ErrorResponse;
import com.faithfulolaleru.droneservice.exception.GeneralException;
import com.faithfulolaleru.droneservice.repository.DroneRepository;
import com.faithfulolaleru.droneservice.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final ModelMapper modelmapper;

    @Override
    public DroneResponse registerDrone(DroneRequest request) {

        // prepare to create
        Drone droneEntity = buildDroneEntity(request);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to Create Drone");
        }

        Drone savedEntity = droneRepository.save(droneEntity);

        return modelmapper.map(savedEntity, DroneResponse.class);
    }

    @Override
    public DroneResponse getDroneBySerial(UUID serial) {

        return droneRepository.findById(serial)
                .map(e -> modelmapper.map(e, DroneResponse.class))
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, ErrorResponse.ERROR_DRONE,
                        "Drone with serial not found"));
    }

    @Override
    public List<DroneResponse> getAllDronesByState(DroneState state) {

        // DroneState.IDLE can give you all available drones for loading

        return droneRepository.findAllByState(state).stream()
                .map(dEntity -> modelmapper.map(dEntity, DroneResponse.class))
                .collect(Collectors.toList());
    }

    private Drone buildDroneEntity(DroneRequest request) {
        return Drone.builder()
            .serial(UUID.randomUUID())
            .model(request.getModel())
            .weight(request.getWeight())
            .batteryCapacity(request.getBatteryCapacity())
            .state(request.getState())
            .build();
    }




}

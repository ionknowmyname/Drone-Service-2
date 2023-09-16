package com.faithfulolaleru.droneservice.services;

import com.faithfulolaleru.droneservice.dtos.BulkMedicationRequest;
import com.faithfulolaleru.droneservice.dtos.DroneRequest;
import com.faithfulolaleru.droneservice.dtos.DroneResponse;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.enums.DroneState;

import java.util.List;
import java.util.UUID;

public interface DroneService {

    DroneResponse registerDrone(DroneRequest request);

    DroneResponse getDroneBySerial(UUID serial);

    List<DroneResponse> getAllDronesByState(DroneState state);

    List<MedicationResponse> getMedicationsOnDrone(UUID serial);

    DroneResponse loadMedicationToDrone(UUID serial, BulkMedicationRequest requestDto);

    DroneResponse unloadMedicationToDrone(UUID serial, BulkMedicationRequest requestDto);

    String getBatteryPercentOfDrone(UUID serial);

}

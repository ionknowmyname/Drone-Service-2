package com.faithfulolaleru.droneservice.dtos;


import com.faithfulolaleru.droneservice.entity.Medication;
import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneResponse {

    private String serial;
    private DroneModel model;
    private Integer weight;
    private Integer batteryCapacity;
    private DroneState state;
    private List<Medication> medications;
}

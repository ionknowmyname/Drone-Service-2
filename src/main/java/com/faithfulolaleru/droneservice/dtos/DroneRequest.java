package com.faithfulolaleru.droneservice.dtos;

import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneRequest {

    private DroneModel model;
    private Integer weight;
    private Integer batteryCapacity;
    private DroneState state;
}

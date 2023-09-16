package com.faithfulolaleru.droneservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneBatteryLogResponse {

    private UUID id;
    private UUID droneSerial;
    private Integer batteryPercentage;
    private LocalDateTime createdAt;
}

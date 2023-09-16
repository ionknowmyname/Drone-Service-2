package com.faithfulolaleru.droneservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "drone_battery_log_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DroneBatteryLog {

    @Id
    private UUID id;

    @Column(name = "drone_serial")
    private UUID droneSerial;

    @Column(name = "battery_percentage")
    private Integer batteryPercentage;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}

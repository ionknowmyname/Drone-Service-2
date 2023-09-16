package com.faithfulolaleru.droneservice.entity;


import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity(name = "drone_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    private UUID serial;

    @Enumerated(EnumType.STRING)
    @Column(name = "drone_model")
    private DroneModel model;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "drone_state")
    private DroneState state;

    // @Builder.Default
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "drone", cascade = CascadeType.ALL)

    // @OnDelete(action = OnDeleteAction.CASCADE)
//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
    @Builder.Default
    // @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Medication> medications = new ArrayList<>();

}

package com.faithfulolaleru.droneservice.entity;


import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;


import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity(name = "drone_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

    // @OnDelete(action = OnDeleteAction.CASCADE)

//    @Column(columnDefinition = "jsonb")
//    @Type(type = "jsonb")
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drone", cascade = CascadeType.ALL)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Medication> medications = new ArrayList<>();

}

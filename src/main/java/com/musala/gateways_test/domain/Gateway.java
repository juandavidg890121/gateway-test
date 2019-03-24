package com.musala.gateways_test.domain;

import com.musala.gateways_test.domain.values.IPv4;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Gateway entity class
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "gateway")
public class Gateway implements Serializable {

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    @NotNull
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @NotNull
    @Embedded
    @Basic(optional = false)
    private IPv4 iPv4;
    @OneToMany(mappedBy = "gateway", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PeripheralDevice> peripheralDevices;

}

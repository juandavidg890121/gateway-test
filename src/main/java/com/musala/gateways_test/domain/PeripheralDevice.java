package com.musala.gateways_test.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musala.gateways_test.domain.values.STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Peripheral device entity class
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "peripheral_device")
public class PeripheralDevice implements Serializable {

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "uid", nullable = false)
    private String UID;
    @Column(name = "vendor")
    private String vendor;
    @Builder.Default
    @Column(name = "created")
    private LocalDate created = LocalDate.now();
    @Column(name = "status")
    private STATUS status;
    @JsonIgnore
    @JoinColumn(name = "gateway", referencedColumnName = "serial_number")
    @ManyToOne(fetch = FetchType.LAZY)
    private Gateway gateway;

}

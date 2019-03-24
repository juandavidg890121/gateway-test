package com.musala.gateways_test.web.assemblers.resources;

import com.musala.gateways_test.domain.values.STATUS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDate;

/**
 * Peripheral device resource to expose data
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Relation(value = "peripheral", collectionRelation = "peripherals")
public class PeripheralDeviceResource extends ResourceSupport {

    private String UID;
    private String vendor;
    private LocalDate created;
    private STATUS status;

}

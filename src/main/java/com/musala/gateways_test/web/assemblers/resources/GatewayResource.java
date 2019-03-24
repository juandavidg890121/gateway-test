package com.musala.gateways_test.web.assemblers.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Gateway resource to expose data
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Relation(value = "gateway", collectionRelation = "gateways")
public class GatewayResource extends ResourceSupport {

    private String serialNumber;
    private String name;
    private String iPv4;
}

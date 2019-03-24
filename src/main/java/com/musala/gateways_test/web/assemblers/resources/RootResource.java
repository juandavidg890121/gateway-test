package com.musala.gateways_test.web.assemblers.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

/**
 * Root resource to expose index links
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RootResource extends ResourceSupport {

    private final String name;
    private final String description;
}

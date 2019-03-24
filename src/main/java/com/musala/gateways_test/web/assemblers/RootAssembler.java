package com.musala.gateways_test.web.assemblers;

import com.musala.gateways_test.web.GatewayController;
import com.musala.gateways_test.web.PeripheralDeviceController;
import com.musala.gateways_test.web.assemblers.resources.RootResource;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Root assembler to expose index links
 */
@Service
public class RootAssembler {

    public RootResource buildRoot() {
        final RootResource resource = new RootResource("gateway-api", "Gateway HATEOAS API");
        resource.add(
                linkTo(methodOn(GatewayController.class).findAll()).withRel("gateways"),
                linkTo(methodOn(PeripheralDeviceController.class).findAll()).withRel("peripherals"));
        return resource;
    }
}

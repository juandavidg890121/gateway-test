package com.musala.gateways_test.web.assemblers;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.web.GatewayController;
import com.musala.gateways_test.web.PeripheralDeviceController;
import com.musala.gateways_test.web.assemblers.resources.GatewayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.musala.gateways_test.web.assemblers.ResourceIdFactory.getId;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Gateway resource assembler to build expose data and links of {@link Gateway}
 */
@Service
public class GatewayResourceAssembler extends BaseResourceAssembler<Gateway, GatewayResource, GatewayController> {

    @Autowired
    public GatewayResourceAssembler() {
        super(GatewayController.class, GatewayResource.class);
    }

    /**
     * Build a {@link GatewayResource} of a {@link Gateway}
     *
     * @param entity
     * @return
     */
    @Override
    public GatewayResource toResource(Gateway entity) {
        final GatewayResource resource = createResourceWithId(getId(entity), entity);
        resource.add(
                linkTo(methodOn(PeripheralDeviceController.class).findPeripheralByGateway(getId(entity))).withRel("peripherals"));
        resource.setSerialNumber(entity.getSerialNumber());
        resource.setName(entity.getName());
        resource.setIPv4(entity.getIPv4().getIPv4());
        return resource;
    }
}


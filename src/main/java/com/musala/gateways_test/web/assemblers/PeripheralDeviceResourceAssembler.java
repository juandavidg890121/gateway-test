package com.musala.gateways_test.web.assemblers;

import com.musala.gateways_test.domain.PeripheralDevice;
import com.musala.gateways_test.web.GatewayController;
import com.musala.gateways_test.web.PeripheralDeviceController;
import com.musala.gateways_test.web.assemblers.resources.PeripheralDeviceResource;
import org.springframework.stereotype.Component;

import static com.musala.gateways_test.web.assemblers.ResourceIdFactory.getId;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Peripheral device resource assembler to build expose data and links of {@link PeripheralDevice }
 */
@Component
public class PeripheralDeviceResourceAssembler extends BaseResourceAssembler<PeripheralDevice, PeripheralDeviceResource, PeripheralDeviceController> {

    public PeripheralDeviceResourceAssembler() {
        super(PeripheralDeviceController.class, PeripheralDeviceResource.class);
    }

    /**
     * Build a {@link PeripheralDeviceResource} of a {@link PeripheralDevice}
     *
     * @param entity
     * @return
     */
    @Override
    public PeripheralDeviceResource toResource(PeripheralDevice entity) {
        final PeripheralDeviceResource resource = createResourceWithId(getId(entity), entity);
        resource.add(
                linkTo(methodOn(GatewayController.class).findOne(entity.getGateway().getSerialNumber())).withRel("gateway"));
        resource.setUID(entity.getUID());
        resource.setVendor(entity.getVendor());
        resource.setCreated(entity.getCreated());
        resource.setStatus(entity.getStatus());
        return resource;
    }
}

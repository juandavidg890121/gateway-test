package com.musala.gateways_test.web;

import com.musala.gateways_test.domain.GatewayService;
import com.musala.gateways_test.domain.PeripheralDevice;
import com.musala.gateways_test.domain.PeripheralDeviceService;
import com.musala.gateways_test.web.assemblers.PeripheralDeviceResourceAssembler;
import com.musala.gateways_test.web.assemblers.resources.PeripheralDeviceResource;
import com.musala.gateways_test.web.dto.PeripheralDeviceDTO;
import com.musala.gateways_test.web.utils.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@ExposesResourceFor(PeripheralDeviceResource.class)
@RestController
@RequestMapping(path = "/peripheral", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeripheralDeviceController {

    private final PeripheralDeviceService peripheralDeviceService;
    private final PeripheralDeviceResourceAssembler peripheralDeviceAssembler;
    private final GatewayService gatewayService;
    private final HeaderUtil headerUtil;

    @Autowired
    public PeripheralDeviceController(PeripheralDeviceService peripheralDeviceService, PeripheralDeviceResourceAssembler peripheralDeviceAssembler, GatewayService gatewayService, HeaderUtil headerUtil) {
        this.peripheralDeviceService = peripheralDeviceService;
        this.peripheralDeviceAssembler = peripheralDeviceAssembler;
        this.gatewayService = gatewayService;
        this.headerUtil = headerUtil;
    }

    /**
     * Save a peripheral device validating the data. Then return into {@link org.springframework.http.HttpHeaders}
     * the location {@link org.springframework.hateoas.Link} of the resource through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Save a Peripheral Device",
            response = ResponseEntity.class
    )
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody PeripheralDeviceDTO peripheralDeviceDTO) {
        if (!gatewayService.findById(peripheralDeviceDTO.getGatewayId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headerUtil.errorHeader("Gateway was not found")).build();
        }

        if (peripheralDeviceService.findById(peripheralDeviceDTO.getUID()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(headerUtil.errorHeader("Peripheral already exists")).build();
        }

        if (gatewayService.findById(peripheralDeviceDTO.getGatewayId()).get().getPeripheralDevices().size() >= 10) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(headerUtil.errorHeader("Maximum number of peripherals allowed per gateway is 10")).build();
        }

        PeripheralDevice peripheralDevice = peripheralDeviceService.save(peripheralDeviceDTO);
        return ResponseEntity
                .created(linkTo(methodOn(PeripheralDeviceController.class)
                        .findOne(peripheralDevice.getUID())).toUri()).build();
    }

    /**
     * Update a peripheral device validating the data. Then return into {@link org.springframework.http.HttpHeaders}
     * the location {@link org.springframework.hateoas.Link} of the resource through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Update a Peripheral Device",
            response = ResponseEntity.class
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody PeripheralDeviceDTO peripheralDeviceDTO) {
        if (!peripheralDeviceService.findById(id).isPresent())
            return ResponseEntity.notFound()
                    .headers(headerUtil.errorHeader("Peripheral was not found")).build();

        if (!gatewayService.findById(peripheralDeviceDTO.getGatewayId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headerUtil.errorHeader("Gateway was not found")).build();
        }

        peripheralDeviceDTO.setUID(id);
        PeripheralDevice peripheralDevice = peripheralDeviceService.save(peripheralDeviceDTO);
        return ResponseEntity.ok().location(linkTo(methodOn(GatewayController.class)
                .findOne(peripheralDevice.getUID())).toUri()).build();
    }

    /**
     * Find all peripheral devices, and transform them into a REST collection resource using
     * {@link PeripheralDeviceResourceAssembler#toResources(Iterable)}. Then return them through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Retrieve all Peripheral Devices",
            response = ResponseEntity.class
    )
    @GetMapping
    public ResponseEntity<Collection<PeripheralDeviceResource>> findAll() {
        return ResponseEntity.ok(peripheralDeviceAssembler.toResources(peripheralDeviceService.findAll()));

    }

    /**
     * Get a peripheral device {@link PeripheralDevice} and transform it into a REST resource using
     * {@link PeripheralDeviceResourceAssembler#toResource(Object)}. Then return it through
     * Spring Web's {@link ResponseEntity} fluent API.
     *
     * @param uid
     */
    @ApiOperation(
            value = "Get a Peripheral Device",
            response = ResponseEntity.class
    )
    @GetMapping("/{uid}")
    public ResponseEntity<PeripheralDeviceResource> findOne(@PathVariable String uid) {
        Optional<PeripheralDevice> peripheralDevice = peripheralDeviceService.findById(uid);
        if (!peripheralDevice.isPresent()) {
            return ResponseEntity.notFound()
                    .headers(headerUtil.errorHeader("Peripheral was not found")).build();
        }
        return ResponseEntity.ok(peripheralDeviceAssembler.toResource(peripheralDevice.get()));
    }

    /**
     * Remove a peripheral device {@link PeripheralDevice} and transform it into a REST resource using
     * {@link PeripheralDeviceResourceAssembler#toResource(Object)}. Then return it through
     * Spring Web's {@link ResponseEntity} fluent API.
     *
     * @param uid
     */
    @ApiOperation(
            value = "Remove a Peripheral Device from a Gateway",
            response = ResponseEntity.class
    )
    @DeleteMapping("/{uid}")
    public ResponseEntity<Boolean> remove(@PathVariable String uid) {
        if (!peripheralDeviceService.findById(uid).isPresent()) {
            return ResponseEntity.notFound()
                    .headers(headerUtil.errorHeader("Peripheral was not found")).build();
        }
        if (peripheralDeviceService.remove(uid))
            return ResponseEntity.ok()
                    .headers(headerUtil.successHeader("Peripheral device deleted"))
                    .body(Boolean.TRUE);
        return ResponseEntity.unprocessableEntity()
                .headers(headerUtil.successHeader("Can't delete the peripheral device"))
                .body(Boolean.FALSE);
    }

    /**
     * Find all peripheral devices by the gateway serial number, and transform them into a REST collection resource using
     * {@link PeripheralDeviceResourceAssembler#toResources(Iterable)}. Then return them through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Retrieve all Peripheral Devices of a Gateway",
            response = ResponseEntity.class
    )
    @GetMapping("/{serial}/gateway/")
    public ResponseEntity<Collection<PeripheralDeviceResource>> findPeripheralByGateway(@PathVariable String serial) {
        return ResponseEntity.ok(peripheralDeviceAssembler.toResources(peripheralDeviceService.findByGateway(serial)));
    }
}

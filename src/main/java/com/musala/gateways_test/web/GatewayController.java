package com.musala.gateways_test.web;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.GatewayService;
import com.musala.gateways_test.web.assemblers.GatewayResourceAssembler;
import com.musala.gateways_test.web.assemblers.resources.GatewayResource;
import com.musala.gateways_test.web.dto.GatewayDTO;
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

@ExposesResourceFor(GatewayResource.class)
@RestController
@RequestMapping(path = "/gateway", produces = MediaType.APPLICATION_JSON_VALUE)
public class GatewayController {

    private final GatewayService gatewayService;
    private final GatewayResourceAssembler gatewayAssembler;
    private final HeaderUtil headerUtil;

    @Autowired
    public GatewayController(GatewayService gatewayService, GatewayResourceAssembler gatewayAssembler, HeaderUtil headerUtil) {
        this.gatewayService = gatewayService;
        this.gatewayAssembler = gatewayAssembler;
        this.headerUtil = headerUtil;
    }

    /**
     * Save a gateway validating the data. Then return into {@link org.springframework.http.HttpHeaders}
     * the location {@link org.springframework.hateoas.Link} of the resource through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Save a Gateway",
            response = ResponseEntity.class
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(@RequestBody GatewayDTO gatewayDTO) {
        if (!gatewayService.validate(gatewayDTO)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(headerUtil.errorHeader("Bad format for IPv4")).build();
        }

        if (gatewayService.findById(gatewayDTO.getSerialNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(headerUtil.errorHeader("Gateway already exists")).build();
        }

        Gateway gateway = gatewayService.save(gatewayDTO);
        return ResponseEntity
                .created(linkTo(methodOn(GatewayController.class)
                        .findOne(gateway.getSerialNumber())).toUri()).build();
    }

    /**
     * Update a gateway validating the data. Then return into {@link org.springframework.http.HttpHeaders}
     * the location {@link org.springframework.hateoas.Link} of the resource through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Update a Gateway",
            response = ResponseEntity.class
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody GatewayDTO gatewayDTO) {
        if (!gatewayService.findById(id).isPresent())
            return ResponseEntity.notFound()
                    .headers(headerUtil.errorHeader("Gateway was not found")).build();

        if (!gatewayService.validate(gatewayDTO)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(headerUtil.errorHeader("Bad format for IPv4")).build();
        }

        gatewayDTO.setSerialNumber(id);
        Gateway gateway = gatewayService.save(gatewayDTO);
        return ResponseEntity.ok().location(linkTo(methodOn(GatewayController.class)
                .findOne(gateway.getSerialNumber())).toUri()).build();
    }

    /**
     * Find all gateways, and transform them into a REST collection resource using
     * {@link GatewayResourceAssembler#toResources(Iterable)}. Then return them through
     * Spring Web's {@link ResponseEntity} fluent API.
     */
    @ApiOperation(
            value = "Retrieve all Gateways",
            response = ResponseEntity.class
    )
    @GetMapping
    public ResponseEntity<Collection<GatewayResource>> findAll() {
        return ResponseEntity.ok(gatewayAssembler.toResources(gatewayService.findAll()));
    }

    /**
     * Get a gateway {@link Gateway} and transform it into a REST resource using
     * {@link GatewayResourceAssembler#toResource(Object)}. Then return it through
     * Spring Web's {@link ResponseEntity} fluent API.
     *
     * @param id
     */
    @ApiOperation(
            value = "Get a Gateway",
            response = ResponseEntity.class
    )
    @GetMapping("/{id}")
    public ResponseEntity<GatewayResource> findOne(@PathVariable String id) {
        Optional<Gateway> gateway = gatewayService.findById(id);
        if (!gateway.isPresent())
            return ResponseEntity.notFound()
                    .headers(headerUtil.errorHeader("Gateway was not found")).build();
        return ResponseEntity.ok(gatewayAssembler.toResource(gateway.get()));
    }
}

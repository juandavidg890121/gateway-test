package com.musala.gateways_test.web;

import com.musala.gateways_test.web.assemblers.RootAssembler;
import com.musala.gateways_test.web.assemblers.resources.RootResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    private final RootAssembler rootAssembler;

    @Autowired
    public RootController(RootAssembler rootAssembler) {
        this.rootAssembler = rootAssembler;
    }

    @GetMapping("/")
    public ResponseEntity<RootResource> root() {
        return ResponseEntity.ok(rootAssembler.buildRoot());
    }
}

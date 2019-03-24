package com.musala.gateways_test.web.assemblers;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for extend all assembler resources
 *
 * @param <T>
 * @param <D>
 * @param <C>
 */
public abstract class BaseResourceAssembler<T, D extends ResourceSupport, C> extends ResourceAssemblerSupport<T, D> {

    protected final Class<C> controllerClass;

    public BaseResourceAssembler(Class<C> controllerClass, Class<D> resourceType) {
        super(controllerClass, resourceType);
        this.controllerClass = controllerClass;
    }

    public Collection<D> toResources(List<T> entities) {
        return entities.stream().map(this::toResource).collect(Collectors.toList());
    }
}

package com.grupoasv.controller;

import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.usecase.CreateServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RepositoryRestController
public class ServiceRequestController {

    private final CreateServiceRequest createServiceRequest;

    @Autowired
    public ServiceRequestController(CreateServiceRequest createServiceRequest) {
        this.createServiceRequest = createServiceRequest;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/serviceRequests")
    public ResponseEntity<PersistentEntityResource> createServiceRequest(
            @RequestBody ServiceRequest serviceRequest,
            PersistentEntityResourceAssembler HateoasConverter) {

        createServiceRequest.execute(serviceRequest);

        return ResponseEntity.ok(HateoasConverter.toResource(serviceRequest));
    }
}

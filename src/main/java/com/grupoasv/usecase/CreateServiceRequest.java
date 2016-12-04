package com.grupoasv.usecase;

import com.grupoasv.domain.Client;
import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.repository.ClientRepository;
import com.grupoasv.repository.ServiceRepository;
import com.grupoasv.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateServiceRequest {
    private final ServiceRequestRepository serviceRequestRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public CreateServiceRequest(ServiceRequestRepository serviceRequestRepository, ClientRepository clientRepository, ServiceRepository serviceRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public void execute(ServiceRequest request) {
        createUserWhenNotExists(request.getClient());
        createServiceRequest(request);
        createServiceInstance(request);
    }

    private void createServiceInstance(ServiceRequest request) {
        serviceRepository.save(request.generateService());
    }

    private void createServiceRequest(ServiceRequest request) {
        serviceRequestRepository.save(request);
    }

    private void createUserWhenNotExists(Client client) {
        if (!clientRepository.exists(client.getSip())){
            clientRepository.save(client);
        }
    }
}

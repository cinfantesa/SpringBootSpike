package com.grupoasv.usecase;

import com.grupoasv.domain.Client;
import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.repository.ClientRepository;
import com.grupoasv.repository.ServiceRepository;
import com.grupoasv.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void execute(ServiceRequest request) {
        CreateUserWhenNotExists(request.getClient());
        CreateServiceRequest(request);
        CreateServiceInstance(request);
    }

    private void CreateServiceInstance(ServiceRequest request) {
        serviceRepository.save(request.generateService());
    }

    private void CreateServiceRequest(ServiceRequest request) {
        serviceRequestRepository.save(request);
    }

    private void CreateUserWhenNotExists(Client client) {
        if (!clientRepository.exists(client.getSip())){
            clientRepository.save(client);
        }
    }
}

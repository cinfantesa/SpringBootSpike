package com.grupoasv.usecase;

import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.repository.ClientRepository;
import com.grupoasv.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateServiceRequest {
    private ServiceRequestRepository serviceRequestRepository;
    private ClientRepository clientRepository;

    @Autowired
    public CreateServiceRequest(ServiceRequestRepository serviceRequestRepository, ClientRepository clientRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.clientRepository = clientRepository;
    }

    public void execute(ServiceRequest request) {
        if (!clientRepository.exists(request.getClient().getSip())){
            clientRepository.save(request.getClient());
        }

        serviceRequestRepository.save(request);
    }
}

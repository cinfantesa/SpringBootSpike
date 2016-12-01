package com.grupoasv.usecase;

import com.grupoasv.domain.Client;
import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.repository.ClientRepository;
import com.grupoasv.repository.ServiceRequestRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateServiceRequestShould {
    ServiceRequestRepository serviceRequestRepository = Mockito.spy(ServiceRequestRepository.class);
    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);

    CreateServiceRequest createServiceRequest = new CreateServiceRequest(serviceRequestRepository, clientRepository);


    @Test
    public void create_service_request_and_when_user_not_exists() throws Exception {
        ServiceRequest request = aServiceRequest();

        when(clientRepository.exists(request.getClient().getSip())).thenReturn(false);

        createServiceRequest.execute(request);

        verify(serviceRequestRepository).save(request);
        verify(clientRepository).save(request.getClient());
    }

    @Test
    public void create_service_request_and_not_creating_users_when_user_already_exists() throws Exception {
        ServiceRequest request = aServiceRequest();

        when(clientRepository.exists(request.getClient().getSip())).thenReturn(true);

        createServiceRequest.execute(request);

        verify(serviceRequestRepository).save(request);
        verify(clientRepository, never()).save(request.getClient());
    }


    private ServiceRequest aServiceRequest() {
        return new ServiceRequest(
                1L,
                new Client(1,"Juan de Dios","666777888"),
                "Cancer",
                new Date());
    }
}

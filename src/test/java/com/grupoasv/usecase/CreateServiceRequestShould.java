package com.grupoasv.usecase;

import com.grupoasv.domain.Client;
import com.grupoasv.domain.Service;
import com.grupoasv.domain.ServiceRequest;
import com.grupoasv.repository.ClientRepository;
import com.grupoasv.repository.ServiceRepository;
import com.grupoasv.repository.ServiceRequestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateServiceRequestShould {
    public static final Integer CLIENT_SIP = 1;
    public static final long SERVICE_REQUEST_ID = 1L;
    public static final Date REQUESTED_DATE = new Date();
    public static final String TREATMENT_TYPE = "anyType";

    @Mock ServiceRepository serviceRepository;
    @Mock ServiceRequestRepository serviceRequestRepository;
    @Mock ClientRepository clientRepository;
    @Captor ArgumentCaptor<Service> createdServiceInstance;

    @Test
    public void create_service_request_and_user_when_user_does_not_exits() {
        ServiceRequest request = aServiceRequest();

        given(clientRepository.exists(CLIENT_SIP)).willReturn(false);

        createServiceRequest().execute(request);

        then(serviceRequestRepository).should().save(request);
        then(clientRepository).should().save(request.getClient());
    }

    @Test
    public void create_service_with_existent_user() {
        ServiceRequest request = aServiceRequest();

        given(clientRepository.exists(CLIENT_SIP)).willReturn(true);

        createServiceRequest().execute(request);

        then(serviceRequestRepository).should().save(request);
        then(clientRepository).should(never()).save(request.getClient());
    }

    @Test
    public void create_service_instance_from_service_request() {
        ServiceRequest request = aServiceRequest();

        when(clientRepository.exists(CLIENT_SIP)).thenReturn(true);

        createServiceRequest().execute(request);

        then(serviceRepository).should().save(createdServiceInstance.capture());
        assertThat(createdServiceInstance.getValue().getClient().getSip(), is(CLIENT_SIP));
        assertThat(createdServiceInstance.getValue().getRequestedDate(), is(REQUESTED_DATE));
        assertThat(createdServiceInstance.getValue().getType(), is(TREATMENT_TYPE));
        assertThat(createdServiceInstance.getValue().getServiceRequest().getId(), is(SERVICE_REQUEST_ID));
    }


    private ServiceRequest aServiceRequest() {
        return new ServiceRequest(
                SERVICE_REQUEST_ID,
                aClient(),
                TREATMENT_TYPE,
                REQUESTED_DATE);
    }

    private Client aClient() {
        return new Client(CLIENT_SIP, "anyName","anyPhoneNumber");
    }

    private CreateServiceRequest createServiceRequest() {
        return new CreateServiceRequest(
                serviceRequestRepository,
                clientRepository,
                serviceRepository
        );
    }
}

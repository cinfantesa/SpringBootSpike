package com.grupoasv.repository;

import com.grupoasv.domain.ServiceRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest,Long>{
}

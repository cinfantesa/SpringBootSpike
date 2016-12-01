package com.grupoasv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    Long id;

    @ManyToOne
    ServiceRequest serviceRequest;

    @ManyToOne
    Client client;
    String type;
    Date requestedDate;

}

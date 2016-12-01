package com.grupoasv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by cinfantes on 30/11/16.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    @Id
    private Long id;

    @ManyToOne
    private Client client;
    private String type;
    private Date requestedDate;
}

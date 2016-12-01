package com.grupoasv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by cinfantes on 30/11/16.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private Integer sip;

    private String name;
    private String phone;
}

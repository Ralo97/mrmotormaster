/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

/**
 *
 * @author rfernandez
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "user")
@Data
public class User {
    
    
    public enum Rol{
        USUARIO,
        MECANICO,
        ADMINISTRADOR
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50, unique = true)
    private String nif;
    private String password;
    private String name;
    private String surname;
    private int phone;
    private String email;
    @Enumerated(EnumType.STRING)
    private Rol cargo;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Vehicle> vehicles;
    
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nif='" + nif + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", cargo=" + cargo +
                // 'vehicles' field is not included to avoid potential circular dependency
                '}';
    }

}


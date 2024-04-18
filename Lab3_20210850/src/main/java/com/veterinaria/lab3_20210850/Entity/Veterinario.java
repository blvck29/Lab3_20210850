package com.veterinaria.lab3_20210850.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "veterinario")
public class Veterinario {
    @Id
    private Integer id;
    @Column
    private String nombre;
    @Column
    private String correo;
    @Column
    private Integer sede_id;

}

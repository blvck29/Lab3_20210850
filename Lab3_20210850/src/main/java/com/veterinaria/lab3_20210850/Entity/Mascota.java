package com.veterinaria.lab3_20210850.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "mascota")
public class Mascota {

    @Id
    private Integer id;
    @Column
    private String nombre;
    @Column
    private Integer edad;
    @Column
    private String genero;
    @Column
    private String diagnostico;
    @Column
    private Date fecha_cita;
    @Column
    private Integer numero_consultorio;
    @Column
    private Integer veterinario_id;
    @Column
    private Integer sede_id;

}

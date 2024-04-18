package com.veterinaria.lab3_20210850.Repository;

import com.veterinaria.lab3_20210850.Entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    @Query(value = "SELECT * FROM mascota m WHERE m.sede_id = ?1", nativeQuery = true)
    List<Mascota> buscarMascotaPorSede(Integer sede_id);

}

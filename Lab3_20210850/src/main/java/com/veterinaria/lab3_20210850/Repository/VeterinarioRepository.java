package com.veterinaria.lab3_20210850.Repository;

import com.veterinaria.lab3_20210850.Entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {

   @Query(value = "SELECT * FROM veterinario v WHERE v.sede_id = ?1", nativeQuery = true)
    List<Veterinario> buscarVeterinariosPorSede(Integer sede_id);

}

package com.veterinaria.lab3_20210850.Repository;

import com.veterinaria.lab3_20210850.Entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {
}

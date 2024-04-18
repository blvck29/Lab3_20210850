package com.veterinaria.lab3_20210850.Controller;

import com.veterinaria.lab3_20210850.Entity.Mascota;
import com.veterinaria.lab3_20210850.Entity.Sede;
import com.veterinaria.lab3_20210850.Entity.Veterinario;
import com.veterinaria.lab3_20210850.Repository.MascotaRepository;
import com.veterinaria.lab3_20210850.Repository.SedeRepository;
import com.veterinaria.lab3_20210850.Repository.VeterinarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class VeterinariaController {


    final MascotaRepository mascotaRepository;
    final SedeRepository sedeRepository;
    final VeterinarioRepository veterinarioRepository;

    public VeterinariaController(MascotaRepository mascotaRepository, SedeRepository sedeRepository, VeterinarioRepository veterinarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.sedeRepository = sedeRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    List<Veterinario> veterinarioList = new ArrayList<>();
    List<Mascota> mascotaList = new ArrayList<>();

    @GetMapping("/")
    public String inicio(Model model) {

        List<Sede> listaSedes = sedeRepository.findAll();
        model.addAttribute("listaSedes", listaSedes);

        return "index";
    }

    @GetMapping("/veterinaria/veterinarios")
    public String verVeterinariosTotal(){
        return "veterinarios";
    }

    @GetMapping("/veterinaria/mascotas")
    public String verMascotasTotal(){
        return "mascotas";
    }

    @PostMapping("/mostrar_veterinarios")
    public String consultarVeterinarios(@RequestParam(value = "idSede") String idSede){

        veterinarioList = veterinarioRepository.buscarVeterinariosPorSede(Integer.valueOf(idSede));
        return "redirect:/veterinaria/veterinarios_sede";
    }

    @PostMapping("/mostrar_mascotas")
    public String consultarMascotas(@RequestParam(value = "idSede") String idSede){

        mascotaList = mascotaRepository.buscarMascotaPorSede(Integer.valueOf(idSede));
        return "redirect:/veterinaria/mascotas_sede";
    }

    @GetMapping("veterinaria/veterinarios_sede")
    public String mostrarVeterinarios(Model model){

        Optional<Sede> optionalSede = sedeRepository.findById(veterinarioList.get(0).getSede_id());
        Sede sedeSel = new Sede();
        if (optionalSede.isPresent()){
            sedeSel = optionalSede.get();
        }
        String sede = sedeSel.getNombre();
        model.addAttribute("sede", sede);
        model.addAttribute("veterinarioList", veterinarioList);

        System.out.println(veterinarioList.size());

        return "ver/ver_veterinarios";
    }

    @GetMapping("veterinaria/mascotas_sede")
    public String mostrarMascotas(Model model){

        Optional<Sede> optionalSede = sedeRepository.findById(veterinarioList.get(0).getSede_id());
        Sede sedeSel = new Sede();
        if (optionalSede.isPresent()){
            sedeSel = optionalSede.get();
        }
        String sede = sedeSel.getNombre();
        model.addAttribute("sede", sede);
        model.addAttribute("mascotaList", mascotaList);

        return "ver/ver_mascotas";
    }

}

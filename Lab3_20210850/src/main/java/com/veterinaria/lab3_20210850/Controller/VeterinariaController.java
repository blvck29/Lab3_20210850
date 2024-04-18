package com.veterinaria.lab3_20210850.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VeterinariaController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

}

package cibertec.edu.pe.ruedasFront.Controller;

import cibertec.edu.pe.ruedasFront.Dto.AutoRequestDTO;
import cibertec.edu.pe.ruedasFront.Dto.AutoResponseDTO;
import cibertec.edu.pe.ruedasFront.ViewModel.AutoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/auto")
public class AutoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String auto(Model model) {
        AutoModel autoModel = new AutoModel(
                "00",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        model.addAttribute("AutoModel", autoModel);
        return "buscar";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().length() == 0
                || placa.length() != 7) {


            AutoModel autoModel = new AutoModel(
                    "01",
                    "Debe ingresar una Placa Valido",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
            model.addAttribute("AutoModel", autoModel);
            return "buscar";
        }

        try{
            String endpoint = "http://localhost:8081/autenticacion/auto";
            AutoRequestDTO autoRequestDTO = new AutoRequestDTO(placa);
            AutoResponseDTO autoResponse = restTemplate.postForObject(endpoint, autoRequestDTO, AutoResponseDTO.class);

            if (autoResponse.codigo().equals("00")) {
                AutoModel autoModel = new AutoModel(
                        "00",
                        "",
                        autoResponse.autoMarca(),
                        autoResponse.autoModelo(),
                        autoResponse.autoNroAsientos(),
                        autoResponse.autoPrecio(),
                        autoResponse.autoColor()
                );
                model.addAttribute("AutoModel", autoModel);
                return "detalle";
            } else {
                AutoModel autoModel = new AutoModel(
                        "02",
                        "Autenticacion fallida",
                        "",
                        "",
                        "",
                        "",
                        ""
                );
                model.addAttribute("AutoModel", autoModel);
                return "buscar";
            }
        } catch (Exception e) {
            AutoModel autoModel = new AutoModel(
                    "99",
                    "Ocurrio un problema en la autenticacion",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
            model.addAttribute("AutoModel", autoModel);
            System.out.println(e.getMessage());
            return "buscar";
        }

    }
}
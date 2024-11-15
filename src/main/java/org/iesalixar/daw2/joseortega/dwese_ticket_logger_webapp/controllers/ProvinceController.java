package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao.ProvinceDAO;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao.RegionDAO;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/provinces")

public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);


    // DAO para gestionar las operaciones de las provincias en la base de datos
    @Autowired
    private ProvinceDAO provinceDAO;

    @Autowired
    private RegionDAO regionDAO;


    /**
     * Lista todas las provincias y las pasa como atributo al modelo para que sean
     * accesibles en la vista `province.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = null;
        try {
            listProvinces = provinceDAO.listAllProvinces();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las provincias.");
        }
        model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
        return "province"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        List<Region> listRegions = new ArrayList<>();
        logger.info("Mostrando formulario para nueva provincia.");
        try {
            listRegions = regionDAO.listAllRegions();
        }catch(Exception e){
        }
        model.addAttribute("province", new Province()); // Crear un nuevo objeto provincia
        model.addAttribute("listRegions",listRegions);
        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar una provincia existente.
     *
     * @param id    ID de la provincia a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Province province = null;
        List<Region> listRegions=new ArrayList<>();
        try {
            province = provinceDAO.getProvinceById(id);
            listRegions=regionDAO.listAllRegions();
            if (province == null) {
                logger.warn("No se encontró la provincia con ID {}", id);
            }
        } catch (Exception e) {
            logger.error("Error al obtener la provincia con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la provincia.");
        }
        model.addAttribute("province", province);
        model.addAttribute("listRegions", listRegions);
        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva provincia en la base de datos.
     *
     * @param province              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/insert")
    public String insertProvince(@Valid @ModelAttribute("province") Province province,BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        try{
            if(result.hasErrors()){
                return "province-form";     // Devuelve el formulario para mostrar los errores de validación
            }

            if (provinceDAO.existsProvinceByCode(province.getCode())) {
                logger.warn("El código de la provincia {} ya existe.", province.getCode());
                redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
                return "redirect:/provinces/new";
            }
            provinceDAO.insertProvince(province);
            logger.info("Provincia {} insertada con éxito.", province.getCode());
        } catch (Exception e) {
            logger.error("Error al insertar la provincia {}: {}", province.getCode(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la provincia.");
        }
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }


    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param province              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") Province province, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        logger.info("Actualizando provincia con ID {}", province.getId());
        try {
            if (result.hasErrors()) {
                return "province-form";  // Devuelve el formulario para mostrar los errores de validación
            }
                if (provinceDAO.existsProvinceByCodeAndNotId(province.getCode(),province.getId())) {
                logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
                redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
                return "redirect:/provinces/edit?id=" + province.getId();
            }
            provinceDAO.updateProvince(province);
            logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", province.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la provincia.");
        }
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }


    /**
     * Elimina una provincia de la base de datos.
     *
     * @param id                 ID de la provincia a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/delete")
    public String deleteProvince(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            provinceDAO.deleteProvince(id);
            logger.info("Provincias con ID {} eliminada con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la provincia.");
        }
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }
}

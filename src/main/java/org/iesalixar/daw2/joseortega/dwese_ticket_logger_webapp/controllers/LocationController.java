package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities.Location;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories.LocationRepository;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Location`.
 * Utiliza `LocationDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/locations")

public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);


    // DAO para gestionar las operaciones de las ubicaciones en la base de datos
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las ubicaciones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de ubicaciones.
     */
    @GetMapping
    public String listLocations(Model model) {
        logger.info("Solicitando la lista de todas las localizaciones...");
        List<Location> listLocations = null;
        listLocations = locationRepository.findAll();
        logger.info("Se han cargado {} localizaciones.", listLocations.size());
        model.addAttribute("listLocations", listLocations); // Pasar la lista de localizaciones al modelo
        return "location"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear una nueva ubicacion.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva localización.");
        model.addAttribute("location", new Location()); // Crear un nuevo objeto Location
        model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
        model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar una ubicacion existente.
     *
     * @param id    ID de la ubicacion a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la localización con ID {}", id);
        Optional<Location> location = null;
        location = locationRepository.findById(id);
        if (location == null) {
            logger.warn("No se encontró la localización con ID {}", id);
        }
        model.addAttribute("location", location);
        model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
        model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Inserta una nueva ubicacion en la base de datos.
     *
     * @param location              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model, Locale locale) {
        logger.info("Insertando nueva localización con dirección {}", location.getAddress());
        if (result.hasErrors()) {
            model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
            model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsLocationByAddress(location.getAddress())) {
            logger.warn("El dirección de la localización {} ya existe.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.addressExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/new";
        }
        locationRepository.save(location);
        logger.info("Localización {} insertada con éxito.", location.getAddress());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }

    /**
     * Actualiza una ubicacion existente en la base de datos.
     *
     * @param location              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes,Model model,Locale locale) {
        logger.info("Actualizando localización con ID {}", location.getId());
        if (result.hasErrors()) {
            model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
            model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsLocationByAddressAndNotId(location.getAddress(), location.getId())) {
            logger.warn("El dirección de la localización {} ya existe para otra localización.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.addressExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/edit?id=" + location.getId();
        }
        locationRepository.save(location);
        logger.info("Localización con ID {} actualizada con éxito.", location.getId());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }

    /**
     * Elimina una ubicacion de la base de datos.
     *
     * @param id                 ID de la ubicacion a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando localizacion con ID {}", id);
        //Obtener el objeto de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Verificar si el usuario tiene el rol ADMIN
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            String username = (auth != null) ? auth.getName() : "Usuario desconocido";
            String errorMessage = "El usuario " + username + " no tiene permisos para borrar la localizacion.";
            logger.warn(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/403"; // Redirige a la página de error 403 o a otra página de tu elección
        }
        try {
            locationRepository.deleteById(id);
            logger.info("Localizacion con ID {} eliminada con éxito.", id);
        }catch(Exception e) {
            logger.error("Error al eliminar la localizacion con ID {}: {}", id,
                e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la localizacion.");
        }
        return "redirect:/locations"; // Redirigir a la lista de categorias
    }
}

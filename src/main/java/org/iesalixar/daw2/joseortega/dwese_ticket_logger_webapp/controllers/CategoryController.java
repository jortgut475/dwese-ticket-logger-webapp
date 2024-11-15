package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao.CategoryDAO;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Category;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);


    // DAO para gestionar las operaciones de las categorias en la base de datos
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileStorageService fileStorageService;


    /**
     * Lista todas las categorias y las pasa como atributo al modelo para que sean
     * accesibles en la vista `category.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de categorias.
     */
    @GetMapping
    public String listCategories(Model model) {
        logger.info("Solicitando la lista de todas las categorias...");
        List<Category> listCategories = null;
        try {
            listCategories = categoryDAO.listAllCategories();
            logger.info("Se han cargado {} categorias.", listCategories.size());
        } catch (Exception e) {
            logger.error("Error al listar las categorias: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las categorias.");
        }
        model.addAttribute("listCategories", listCategories); // Pasar la lista de categorias al modelo
        return "category"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear una nueva categoria.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva categoria.");
        model.addAttribute("category", new Category()); // Crear un nuevo objeto categoria

        List<Category> listCategories=new ArrayList<>();
        try{
            listCategories=categoryDAO.listAllCategories();
            logger.info("Se han cargado {} categorias  ",listCategories.size());
        }catch (Exception e){
            logger.error("Error al listar los nombres categorias {}",e.getMessage());
            model.addAttribute("errorMessage","Error al listar los nomrbres de las categorias");
        }
        model.addAttribute("listCategories",listCategories);
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar una categoria existente.
     *
     * @param id    ID de la categoria a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la categoria con ID {}", id);
        Category category = null;
        try {
            category = categoryDAO.getCategoryById(id);
            if (category == null) {
                logger.warn("No se encontró la categoria con ID {}", id);
            }
        } catch (Exception e) {
            logger.error("Error al obtener la categoria con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la categoria.");
        }
        List<Category> listCategories=new ArrayList<>();
        try{
            listCategories=categoryDAO.listAllCategories();
            logger.info("Se han cargado {} categorias ",listCategories.size());
        } catch (Exception e) {
            logger.error("Error al listar nombre de categoria {}",e.getMessage());
            model.addAttribute("errorMessage","Error al listar nombre de categorias");
        }

        model.addAttribute("listCategories",listCategories);
        model.addAttribute("category", category);
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Inserta una nueva categoria en la base de datos.
     *
     * @param category           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorias.
     */
    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva categoria con nombre {}", category.getName());
        try {
            if (result.hasErrors()) {
                return "category-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (categoryDAO.existsCategoryByName(category.getName())) {
            logger.warn("El nombre de la categoria {} ya existe.", category.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Exito al insertar la categoria.");
            return "redirect:/categories/new";
            }
        categoryDAO.insertCategory(category);
        logger.info("categoria {} insertada con éxito.", category.getName());
            //Guardar la imagen subida
            if (!imageFile.isEmpty()) {
                String fileName = fileStorageService.saveFile(imageFile);
                if (fileName != null) {
                    category.setImage(fileName); // Guardar el nombre del archivo en la entidad
                }
            }
            categoryDAO.insertCategory(category);
            logger.info("Categoria {} insertada con éxito.", category.getName());
                redirectAttributes.addFlashAttribute("successMessage", "Exito al insertar la categoria.");
        } catch (Exception e) {
            logger.error("Error al insertar la categoria  {}: {}", category.getName(), e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la categoria.");
        }
        return "redirect:/categories"; // Redirigir a la lista de categorias
    }

    /**
     * Actualiza una categoria existente en la base de datos.
     *
     * @param category           Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorias.
     */
    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando categoria con ID {}", category.getId());
        try {
            if (result.hasErrors()) {
                return "category-form";  // Devuelve el formulario para mostrar los errores de validación
            }
            if (categoryDAO.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
                logger.warn("El nombre de la categoria {} ya existe para otra categoria.", category.getName());
                redirectAttributes.addFlashAttribute("errorMessage", "El nombre de la categoria " +
                        "ya existe para otra categoria.");
                return "redirect:/categories/edit?id=" + category.getId();
            }
            // Guardar la imagen subida
            if (!imageFile.isEmpty()) {
                String fileName = fileStorageService.saveFile(imageFile);
                if (fileName != null) {
                    category.setImage(fileName); // Guardar el nombre del archivo en la entidad
                }
            }
            categoryDAO.updateCategory(category);
            logger.info("Categoria con ID {} actualizada con éxito.", category.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar la categoria con ID {}: {}", category.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la categoria.");
        }
        return "redirect:/categories"; // Redirigir a la lista de categorias
    }

    /**
     * Elimina una categoria de la base de datos.
     *
     * @param id                 ID de la categoria a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorias.
     */
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando categoria con ID {}", id);

        //obtener la categoria antes de eliminarla para acceder a la imagen
        Category category=categoryDAO.getCategoryById(id);

        if(category!=null) {
            //elimino categoria
            categoryDAO.deleteCategory(id);
            logger.info("Categoria con ID {} eliminada con éxito.", id);

            // Eliminar la imagen asociada, si existe
            if (category.getImage() != null && !category.getImage().isEmpty()) {
                fileStorageService.deleteFile(category.getImage());
                logger.info("Imagen {} eliminada del almacenamiento.", category.getImage());
            }
        }else{
            logger.warn("No se encontró la categoria con ID {}", id);
        }
        return "redirect:/categories"; // Redirigir a la lista de regiones
    }
}
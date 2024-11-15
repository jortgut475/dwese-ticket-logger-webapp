package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class CategoryDAOImpl implements CategoryDAO {
    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger =
            LoggerFactory.getLogger(CategoryDAOImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lista todas las categorias de la base de datos.
     * @return Lista de categorias
     */
    @Override
    public List<Category> listAllCategories() {
        logger.info("Listing all categories from the database.");
        String query = "SELECT c FROM Category c ";
        List<Category> categories = entityManager.createQuery(query,
                Category.class).getResultList();
        logger.info("Retrieved {} categories from the database.", categories.size());
        return categories;
    }

    /**
     * * Inserta una nueva  categorias en la base de datos.
     * * @param Categoria categoria a insertar
     * */
    @Override
    public void insertCategory(Category category) {
        logger.info("Inserting category with name: {} , image: {} and parent_id: {}" ,
                category.getName(),category.getImage(),category.getParentCategory());
        entityManager.persist(category);
        logger.info("Inserted category with ID: {}", category.getId());
    }

    /**
     * Actualiza una categoria existente en la base de datos.
     * @param category Categoria a actualizar
     */
    @Override
    public void updateCategory(Category category) {
        logger.info("Updating category with id: {}", category.getId());
        entityManager.merge(category);
        logger.info("Updated category with id: {}", category.getId());
    }

    /**
     * Elimina una categoria de la base de datos.
     * @param id ID de la categoria a eliminar
     */
    @Override
    public void deleteCategory(int id) {
        logger.info("Deleting category with id: {}", id);
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            entityManager.remove(category);
            logger.info("Deleted category with id: {}", id);
        } else {
            logger.warn("Category with id: {} not found.", id);
        }
    }

    /**
     * Recupera una categoria por su ID.
     * @param id ID de la categoria a recuperar
     * @return Categoria encontrada o null si no existe
     */
    @Override
    public Category getCategoryById(int id) {
        logger.info("Retrieving category by id: {}", id);
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            logger.info("Category retrieved: {} - {} - {}", category.getName(),
                    category.getImage(),category.getParentCategory());
        } else {
            logger.warn("No region found with id: {}", id);
        }
        return category;
    }

    /**
     * Verifica si una categoria con el nombre especificado ya existe en la base
     de datos.
     * @param name el nombre de la categoria a verificar.
     * @return true si una categoria con el nombre ya existe, false de lo
    contrario.
     */
    @Override
    public boolean existsCategoryByName(String name) {
        logger.info("Checking if category with name: {} exists", name);
        String query = "SELECT COUNT(c) FROM Category c WHERE UPPER(c.name) =:name";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Category with name: {} exists: {}", name, exists);
        return exists;
    }

    /**
     * Verifica si una categoria con el nombre especificado ya existe en la base
     de datos,
     * excluyendo una categoria con un ID específico.
     * @param name el nombre de la categoria a verificar.
     * @param id el ID de la categoria a excluir de la verificación.
     * @return true si una categoria con el nombre ya existe (y no es la categoria
    con el ID dado),
     * false de lo contrario.
     */
    @Override
    public boolean existsCategoryByNameAndNotId(String name, int id) {
        logger.info("Checking if category with name: {} exists excluding id: {}",
                name, id);
        String query = "SELECT COUNT(c) FROM Category c WHERE UPPER(c.name) =" +
                ":name AND c.id != :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name",name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Category with name: {} exists excluding id {}: {}", name, id,
                exists);
        return exists;
    }

    @Override
    public Category findById(int id) {
        return null;
    }
}
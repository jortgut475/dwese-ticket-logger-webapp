package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class LocationDAOImpl implements LocationDAO{
    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger =
            LoggerFactory.getLogger(LocationDAOImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        String query = "SELECT l FROM Location l JOIN FETCH l.supermarket " +
                "JOIN FETCH l.province";
        List<Location> locations = entityManager.createQuery(query,
                Location.class).getResultList();
        logger.info("Retrieved {} locations from the database.", locations.size());
        return locations;
    }

    /**
     * Inserta una nueva Ubicacion en la base de datos.
     * @param location Ubicacion a insertar
     */
    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting location with address: {} and city: {}",
                location.getAddress(), location.getCity());
        entityManager.persist(location);
        logger.info("Inserted province with ID: {}", location.getId());
    }

    /**
     * Actualiza una ubicacion existente en la base de datos.
     * @param location Ubicacion a actualizar
     */

    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        entityManager.merge(location);
        logger.info("Updated location with id: {}", location.getId());
    }

    /**
     * Elimina una ubicacion de la base de datos.
     * @param id ID de la ubicacion a eliminar
     */
    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        Location location = entityManager.find(Location.class, id);
        if (location != null) {
            entityManager.remove(location);
            logger.info("Deleted location with id: {}", id);
        } else {
            logger.warn("Location with id: {} not found.", id);
        }
    }

    /**
     * Obtiene una provincia por su ID.
     * @param id ID de la provincia
     * @return Provincia correspondiente al ID
     */

    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        Location location = entityManager.find(Location.class, id);
        if (location != null) {
            logger.info("Location retrieved: {} - {}",
                    location.getAddress(),location.getCity());
        } else {
            logger.warn("No province found with id: {}", id);
        }
        return location;
    }
}

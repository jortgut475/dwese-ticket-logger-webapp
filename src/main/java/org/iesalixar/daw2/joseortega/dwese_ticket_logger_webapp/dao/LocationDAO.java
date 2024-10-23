package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity.Location;
import java.sql.SQLException;
import java.util.List;

public interface LocationDAO {
    List<Location> listAllLocations() throws SQLException;
    void insertLocation(Location location) throws SQLException;
    void updateLocation(Location location) throws SQLException;
    void deleteLocation(int id) throws SQLException;
    Location getLocationById(int id) throws SQLException;
}

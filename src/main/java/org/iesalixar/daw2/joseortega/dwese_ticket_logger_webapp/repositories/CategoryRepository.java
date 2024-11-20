package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories;

import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsCategoryByName(String name);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.id != :id")
    boolean existsCategoryByNameAndIdNot(@Param("name") String name,
                                       @Param("id") Long id);
}

package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories;

import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Ticket que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad
 Ticket.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /**
     * Obtiene todos los tickets que tienen un descuento mayor que un valor
     específico.
     *
     * @param discount el valor mínimo del descuento.
     * @return una lista de tickets con un descuento mayor que el valor
    especificado.
     */
    List<Ticket> findByDiscountGreaterThan(Float discount);

}

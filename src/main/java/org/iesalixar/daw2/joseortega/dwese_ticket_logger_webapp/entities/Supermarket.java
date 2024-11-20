package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Supermercado` representa una entidad que modela un supermercado dentro de la base de datos.
 * Contiene tres campos: `id` y `name`, donde `id` es el identificador único del supermercado,
 * y `name` es el nombre de la región.
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "supermarkets") // Especifica el nombre de la tabla asociada a esta entidad.
@Data  // Esta anotación de Lombok genera automáticamente los siguientes métodos:
// - Getters y setters para todos los campos (id, name).
// - Los métodos `equals()` y `hashCode()` basados en todos los campos no transitorios.
// - El método `toString()` que incluye todos los campos.
// - Un método `canEqual()` que verifica si una instancia puede ser igual a otra.
// Esto evita tener que escribir manualmente todos estos métodos y mejora la mantenibilidad del código.

@NoArgsConstructor  // Esta anotación genera un constructor sin argumentos (constructor vacío),
//  es útil cuando quieres crear un objeto `Supermercado` sin inicializarlo inmediatamente
// con valores. Esto es muy útil en frameworks como Hibernate o JPA,
// que requieren un constructor vacío para la creación de entidades.


@AllArgsConstructor
// Esta anotación genera un constructor que acepta todos los campos como parámetros (id, name).
// Este constructor es útil cuando necesitas crear una instancia completamente inicializada de `Supermercado`.
// Ejemplo: new Supermarket(1, 'Mercadona');

public class Supermarket {

    // Campo que almacena el identificador único del supermercado. Este campo suele ser autogenerado
    // por la base de datos, lo que lo convierte en un buen candidato para una clave primaria.
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID.
    private Long id;

    // Campo que almacena el nombre completo del supermercado, como "Lidl" o "Mercadona".No puede estar vacio
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Column(name = "name", nullable = false) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`. Un supermercado puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch =
            FetchType.LAZY)
    private List<Location> locations;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Supermercado` cuando no es necesario o no se conoce el `id` del supermercado
     * (por ejemplo, antes de insertar el supermercado en la base de datos, donde el `id` es autogenerado).
     *
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}

package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Location` representa una entidad que modela una ubicacion dentro de la base de datos.
 * Contiene cinco campos: `id`, `address` , `city` , `supermarket_id` y `province_id`
 * donde `id` es el identificador único de la ubicacion, `address` es la direccion asociada a la ubicacion,
 * `city` es la ciudad de la location, `supermerket_id` es el identificador del supermercado
 * asociada a la location y ´province_id´ es el identificador de la province aosociado a location.
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Data  // Esta anotación de Lombok genera automáticamente los siguientes métodos:
// - Getters y setters para todos los campos (id, address, city,supermarket_id, province_id).
// - Los métodos `equals()` y `hashCode()` basados en todos los campos no transitorios.
// - El método `toString()` que incluye todos los campos.
// - Un método `canEqual()` que verifica si una instancia puede ser igual a otra.
// Esto evita tener que escribir manualmente todos estos métodos y mejora la mantenibilidad del código.


@NoArgsConstructor  // Esta anotación genera un constructor sin argumentos (constructor vacío),
//  es útil cuando quieres crear un objeto `Location` sin inicializarlo inmediatamente
// con valores. Esto es muy útil en frameworks como Hibernate o JPA,
// que requieren un constructor vacío para la creación de entidades.


@AllArgsConstructor
// Esta anotación genera un constructor que acepta todos los campos como parámetros (id, address, city,supermarket_id, province_id).
// Este constructor es útil cuando necesitas crear una instancia completamente inicializada de `Location`.
// Ejemplo: new Location(2, 'Av. Canal Sur, s/n', 'Tomares', 1, 41);

public class Location {
    // Campo que almacena el identificador único de la ubicacion. Este campo suele ser autogenerado
    // por la base de datos, lo que lo convierte en un buen candidato para una clave primaria.
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    private Integer id;


    // Campo que almacena la direccion de la ubicacion.
    // Ejemplo: "1" para CTRA, Camino de Tomares.
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    private String address;

    // Campo que almacena la direccion de la ubicacion, como "CTRA, Camino de Tomares".
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    private String city;

    // Campo que almacena un identificador del supermercado asociado a la ubicacion, como "1".
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    private Supermarket supermarket;

    // Campo que almacena un identificador de la provincia asociado a la ubicacion, como "41".
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    private Province province;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Location` cuando no es necesario o no se conoce el `id` de la ubicacion
     * (por ejemplo, antes de insertar la ubicacion en la base de datos, donde el `id` es autogenerado).
     * @param address direcion de la ubicacion.
     * @param city ciudad de la ubicacion.
     * @param supermarket ID del supermercado.
     * @param province ID de la provincia
     */
    public Location(String address,String city,Supermarket supermarket,Province province) {
        this.address=address;
        this.city=city;
        this.supermarket = supermarket;
        this.province=province;
    }
}

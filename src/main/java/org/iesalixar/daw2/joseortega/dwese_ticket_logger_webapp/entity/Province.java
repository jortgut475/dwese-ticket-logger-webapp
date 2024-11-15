package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code` , `name` y `region_id`  donde `id` es el identificador único de la región,
 * `code` es un código asociado a la región, `name` es el nombre de la región e region_id es el identificador de la
 * provincia asociada a la region.
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "provinces") // Define el nombre de la tabla asociada a esta entidad.
@Data  // Esta anotación de Lombok genera automáticamente los siguientes métodos:
// - Getters y setters para todos los campos (id, code, name, region_id).
// - Los métodos `equals()` y `hashCode()` basados en todos los campos no transitorios.
// - El método `toString()` que incluye todos los campos.
// - Un método `canEqual()` que verifica si una instancia puede ser igual a otra.
// Esto evita tener que escribir manualmente todos estos métodos y mejora la mantenibilidad del código.


@NoArgsConstructor  // Esta anotación genera un constructor sin argumentos (constructor vacío),
//  es útil cuando quieres crear un objeto `Province` sin inicializarlo inmediatamente
// con valores. Esto es muy útil en frameworks como Hibernate o JPA,
// que requieren un constructor vacío para la creación de entidades.

@AllArgsConstructor
// Esta anotación genera un constructor que acepta todos los campos como parámetros (id, code, name,region_id).
// Este constructor es útil cuando necesitas crear una instancia completamente inicializada de `Province`.
// Ejemplo: new Province(1, "01", "Araba/Álava", 16);

public class Province {
    // Campo que almacena el identificador único de la provincia. Este campo suele ser autogenerado
    // por la base de datos, lo que lo convierte en un buen candidato para una clave primaria.
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo que almacena el código de la provincia, normalmente una cadena corta que identifica la provincia.
    // Ejemplo: "01" para Araba/Álava.
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;


    // Campo que almacena el nombre completo de la provincia, como "Araba/Álava" o "Sevilla".
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.notEmpty}")
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`. Una provincia puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch =
            FetchType.LAZY)
    private List<Location> locations;

    // Relación con la entidad `Region`, representando la comunidad autónoma a la que pertenece la provincia.
    @NotNull(message = "{msg.province.region.notNull}")
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchas provincias a una región.
    @JoinColumn(name = "region_id", nullable = false) // Clave foránea en la tabla provinces
    // que referencia a la tabla regions.
    private Region region;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Province` cuando no es necesario o no se conoce el `id` de la provincia
     * (por ejemplo, antes de insertar la provincia en la base de datos, donde el `id` es autogenerado).
     * @param code Código de la región.
     * @param name Nombre de la región.
     * @param region la region a la que pertenece la provincia.
     */
    public Province(String code, String name,Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}
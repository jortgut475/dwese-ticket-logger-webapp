package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Category` representa una entidad que modela una Categoria dentro de la base de datos.
 * Contiene cuatro campos: `id`, `name` , `image`  y `parentCategory`
 * donde `id` es el identificador único de la categoria, `name` es el nombre de la categoria,
 * `image` es la imagen de la categoria y `parentCategory` es la categoria padre del nombre de la categoria
 * asociada a la categoria.
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data  // Esta anotación de Lombok genera automáticamente los siguientes métodos:
// - Getters y setters para todos los campos (id, name,image,parentCategory).
// - Los métodos `equals()` y `hashCode()` basados en todos los campos no transitorios.
// - El método `toString()` que incluye todos los campos.
// - Un método `canEqual()` que verifica si una instancia puede ser igual a otra.
// Esto evita tener que escribir manualmente todos estos métodos y mejora la mantenibilidad del código.

@NoArgsConstructor  // Esta anotación genera un constructor sin argumentos (constructor vacío),
//  es útil cuando quieres crear un objeto `Category` sin inicializarlo inmediatamente
// con valores. Esto es muy útil en frameworks como Hibernate o JPA,
// que requieren un constructor vacío para la creación de entidades.

@AllArgsConstructor
// Esta anotación genera un constructor que acepta todos los campos como parámetros (id, name,image,parent_id).
// Este constructor es útil cuando necesitas crear una instancia completamente inicializada de `Category`.
// Ejemplo: new Category(1, 'Electrónica', NULL, NULL);

public class Category {
    // Campo que almacena el identificador único de la categoria. Este campo suele ser autogenerado
    // por la base de datos, lo que lo convierte en un buen candidato para una clave primaria.
    // No añadimos validación en el ID porque en este caso puede ser nulo al insertarse
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo que almacena el nombre de la categoria.
    // Ejemplo: "1" para Electronica no puede estar vacia
    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Column(name = "name", nullable = false)
    private String name;

    // Campo que almacena una Imagen de la categoria.No puede ser nulo y su tamaño maximo es de 500
    @Size(max=500, message = "{msg.category.image.size}")
    @Column(name = "image", nullable = true,length = 500)
    private String image;

    // Campo que almacena la categoria padre del nombre de la categoria asociada a la categoria como "3".
    // No añadimos validación en el ID porque en este caso no puede ser nulo al insertarse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true) // Clave foránea de la misma tabla de categoria
    private Category parentCategory;

    //Relacion uno a muchos con categorias hijas.Una categoria puede tener multiples subcategorias
    @OneToMany(mappedBy = "parentCategory" ,cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Category> categories;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Location` cuando no es necesario o no se conoce el `id` de la ubicacion
     * (por ejemplo, antes de insertar la ubicacion en la base de datos, donde el `id` es autogenerado).
     * @param name nombre de la categoria.
     * @param image imagen de la categoria.
     * @param parentCategory es la categoria padre del nombre de la categoria asociada a la categoria
     */
    public Category(String name, String image,Category parentCategory) {
        this.name=name;
        this.image=image;
        this.parentCategory = parentCategory;
    }
}

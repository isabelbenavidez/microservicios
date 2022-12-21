package com.microservicio.microservicio.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@Table(name = "tbl_products")
@Data
@AllArgsConstructor//Genera constructor con todas las propiedades de la entidad.
@NoArgsConstructor//Genera constructor sin argumentos
@Builder//Permite hacer nuevas instancias de la entidad
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "id", nullable = false)
    private Long id;


    @NotEmpty(message = "El nombre no debe ser vacío")
    private String name; //No se les pone el @Column, porque el atributo va a tener el mismo nombre que en la tabla.
    private String description;
    @Positive(message = "El stock debe ser mayor que cero")//@Positive, para que le número sea mayor a 0
    private Double stock;
    private Double price;
    private String status;

    @Column(name = "create_at")// Solo se pone aquí @Column, porque el nombre es diferente al que se le va a poner a la tabla por el espacio.
    @Temporal(TemporalType.TIMESTAMP)//El TIMESTAMP, es parara registrar FECHA Y HORA.
    private Date createAt;

    @NotNull(message = "La categoría no puede ser vacía")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Category category;

}

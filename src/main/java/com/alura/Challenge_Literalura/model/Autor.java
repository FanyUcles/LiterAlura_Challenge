package com.alura.Challenge_Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String nombre;

    @JsonAlias("birth_year")
    private int fechaNacimiento;

    @JsonAlias("death_year")
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Libro> libros;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}



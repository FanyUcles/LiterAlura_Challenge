package com.alura.Challenge_Literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne()
    private Autor autor;

    private String nombreAutor;
    private String idiomas;
    private Double numeroDeDescargas;

    public Libro() {
    }

    public Libro(LibroResponse datosLibros) {
        this.titulo = datosLibros.titulo();
        this.nombreAutor = obtenerPrimerAutor(datosLibros).getNombre();
        this.idiomas = obtenerPrimerIdioma(datosLibros);
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas){
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    //metodos
    public Autor obtenerPrimerAutor(LibroResponse datosLibro){
        AutorResponse datosAutor = datosLibro.autor().get(0);
        return new Autor(datosAutor);
    }

    public String obtenerPrimerIdioma(LibroResponse datosLibros){
        String idioma = datosLibros.idiomas().toString();
        return idioma;
    }

    @Override
    public String toString(){
        return
                "\n <<------------------------------------->> " +
                "\n <<--------------Libro------------------>>\n  "+
                " Titulo:  " + titulo +
                "\n  Autor: " + nombreAutor +
                "\n  Idiomas: " + idiomas+
                " \n  Numero de descargas:  " + numeroDeDescargas +
                "\n <<------------------------------------->>  " +
                "\n <<------------------------------------->>\n  "
                ;
    }
}





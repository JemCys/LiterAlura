package com.rebollarclary.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {}

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idioma = datosLibros.idioma().get(0);
        this.numeroDescargas = datosLibros.numeroDescargas();
    }


    //Getters & Setters

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }


    //End Getters & Setters


    @Override
    public String toString() {
        return String.format(
                "*.~.~.~.~.~.~.~ Libro ~.~.~.~.~.~.~.%n" +
                        "Titulo: %s%n" +
                        "Autor: %s%n" +
                        "Idioma: %s%n" +
                        "Numero de Descargas: %d%n" +
                        "~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.%n",
                titulo, (autor != null ? autor.getNombre() : "Autor desconocido"), idioma, numeroDescargas
        );
    }
}
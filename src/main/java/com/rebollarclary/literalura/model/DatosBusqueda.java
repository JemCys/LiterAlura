package com.rebollarclary.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)
public record DatosBusqueda(
        @JsonAlias ("results")List<DatosLibros> resultadoLibros) {
}

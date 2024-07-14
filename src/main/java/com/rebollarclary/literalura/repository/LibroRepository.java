package com.rebollarclary.literalura.repository;

import com.rebollarclary.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    long countByIdioma(String idioma);

    @Query("SELECT COUNT(l) FROM Libro l WHERE l.idioma = :idioma")
    long countLibrosByIdioma(@Param("idioma") String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC LIMIT 10")
    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}



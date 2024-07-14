package com.rebollarclary.literalura.repository;

import com.rebollarclary.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("""
            SELECT a
            FROM Autor a
            WHERE a.fechaNacimiento <= :anio AND a.fechaDeceso >= :anio
            """)
    List<Autor> buscarAutoresPorAnio(int anio);
}
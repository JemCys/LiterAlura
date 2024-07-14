package com.rebollarclary.literalura;

import com.rebollarclary.literalura.principal.Principal;
import com.rebollarclary.literalura.repository.AutorRepository;
import com.rebollarclary.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
    @Autowired
    private LibroRepository repositoryLibro;
    @Autowired
    private AutorRepository repositoryAutor;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repositoryLibro, repositoryAutor);
        principal.muestraMenu();

    }
}


package com.apibooks.livros.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import com.apibooks.livros.dtos.LivroRecordDto;
import com.apibooks.livros.models.LivrosModels;
import com.apibooks.livros.propeties.FileStorageProperties;
import com.apibooks.livros.repositories.LivrosRepository;

import jakarta.validation.Valid;

@Controller
public class LivroController {
    
    @Autowired
    LivrosRepository livrosRepository;

    private Path fileStorageLocation;

    public LivroController(FileStorageProperties fileStorageProperties){
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    /*Método para criar/salvar um livro ou ebook */
    @PostMapping("/novo-livro")
    public ResponseEntity<LivrosModels> salvarProduto(@RequestBody @Valid LivroRecordDto livroRecordDto){
        // Recuperar img 

        var livroModel = new LivrosModels();
        BeanUtils.copyProperties(livroRecordDto, livroModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(livrosRepository.save(livroModel));

    }

}

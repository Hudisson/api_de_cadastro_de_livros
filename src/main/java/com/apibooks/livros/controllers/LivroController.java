package com.apibooks.livros.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.apibooks.livros.dtos.LivroRecordDto;
import com.apibooks.livros.models.LivrosModels;
import com.apibooks.livros.propeties.FileStorageProperties;
import com.apibooks.livros.repositories.LivrosRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
      
        var livroModel = new LivrosModels();
        BeanUtils.copyProperties(livroRecordDto, livroModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(livrosRepository.save(livroModel));
    }
    
    /*Método para definir uma imagem para o livro ou ebook apartir do ID */
    @PutMapping("/livro/img/{bookid}")
    public ResponseEntity<String> definirImgLivro(@PathVariable(value = "bookid") UUID bookId,
                            LivroRecordDto livroRecordDto, @RequestParam("file") @Valid MultipartFile file){
        
        Optional<LivrosModels> livroObj = livrosRepository.findById(bookId);
        if(livroObj.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro Não encontrado. Não foi possível definir uma arquivo de imagem para o livro");
        }
        
        String fileName = StringUtils.cleanPath(bookId+"_"+file.getOriginalFilename());
        System.out.println("\nURL: "+fileName);

        try{

            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/livro/img/").path(fileName).toUriString();

            var livroModel = livroObj.get();
            livroModel.setUrlImg(fileDownloadUri);
            BeanUtils.copyProperties(livroRecordDto, livroModel);
            ResponseEntity.status(HttpStatus.OK).body(livrosRepository.save(livroModel));
            return ResponseEntity.ok("Imagem do livro salva com sucesso!");  

        }catch(IOException err){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível salvar a imagem do livro: "+err);
        }
    }

    /*Método para atualizar as informações de um livro ou ebook */
    @PutMapping("/atualizar-livro/{bookid}")
    public ResponseEntity<Object> atualizarLivro(@PathVariable(value = "bookid") UUID bookId, @RequestBody @Valid LivroRecordDto livroRecordDto){

        Optional<LivrosModels> livroObj = livrosRepository.findById(bookId);
        if(livroObj.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível atualizar. Livro não encontrado");
        }

        var livroModel = livroObj.get();
        BeanUtils.copyProperties(livroRecordDto, livroModel);
        return ResponseEntity.status(HttpStatus.OK).body(livrosRepository.save(livroModel));
    }

    /*Método que retorna todos os livros e ebook */
    @GetMapping("/livros")
    public ResponseEntity<List<LivrosModels>> buscarTodosLivros(){

        List<LivrosModels> listaDeLivros = livrosRepository.findAll();

        if(!listaDeLivros.isEmpty()){
            for(LivrosModels livros : listaDeLivros){
                UUID bookId = livros.getBookId();
                livros.add(linkTo(methodOn(LivroController.class).buscarLivroPorId(bookId)).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(listaDeLivros);
    }

    /*Método para buscar um livros ou ebook pelo id*/
    @GetMapping("/livros/{bookid}")
    public ResponseEntity<Object> buscarLivroPorId(@PathVariable(value = "bookid") UUID bookId){

        Optional<LivrosModels> livroObj = livrosRepository.findById(bookId);

        if(livroObj.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro ou ebook não encontrado.");
        }

        livroObj.get().add(linkTo(methodOn(LivroController.class).buscarTodosLivros()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(livroObj.get());
    }

    /*Método para retorna a imagem do livro ou ebook */
   @GetMapping("/livro/img/{fileName:.+}")
   public ResponseEntity<Resource> buscarImgLivro(@PathVariable String fileName, HttpServletRequest request) throws IOException{

        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        try{

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if(contentType == null){
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                   .contentType(MediaType.parseMediaType(contentType))
                   .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: fileName=\"" + resource.getFilename() + "\"")
                   .body(resource);

        }catch(MalformedURLException err){
            return ResponseEntity.badRequest().build();
        }

   }

   /*Método para deletar ou excluir um livro ou ebook */
   
}

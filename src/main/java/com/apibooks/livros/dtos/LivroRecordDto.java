package com.apibooks.livros.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRecordDto(@NotBlank String bookTitle, @NotBlank char bookType, @NotNull BigDecimal price, 
                              String bookDesc, String bookAuthor, String bookPub) {
    
  /**
   *  bookTitle - Título do livro 
   *  bookType - Tipo do  livro (físico, digital/Ebook) 
   *  price - Preço do livro
   *  bookDesc - descrição do livro 
   *  bookAuthor - Autor(es) do livro 
   *  bookPub - Editora que publicou o livro 
   */
}

package com.apibooks.livros.dtos;

import java.math.BigDecimal;

public record LivroRecordDto(String bookTitle, char bookType, BigDecimal price, String bookDesc, String bookAuthor, String bookPub) {
    
  /**
   *  bookTitle - Título do livro 
   *  bookType - Tipo do  livro (físico, digital/Ebook) 
   *  price - Preço do livro
   *  bookDesc - descrição do livro 
   *  bookAuthor - Autor(es) do livro 
   *  bookPub - Editora que publicou o livro 
   */
}

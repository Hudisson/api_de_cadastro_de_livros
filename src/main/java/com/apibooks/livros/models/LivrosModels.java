package com.apibooks.livros.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_livros")
public class LivrosModels extends RepresentationModel<LivrosModels> implements Serializable {
    /**     bookPub */

    private static final long serialVersionUID = 1L; // Número de controle de versão de cada classe serializada

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID bookId;

    private String bookTitle;        // Título do livro 
    private char bookType;          // A - Livro físico, B - Livro digital(E-book)
    private String urlImg;         // book img (imagem do livro)
    private BigDecimal price;     // Preço do livro
    private String bookDesc;     // book descriptio (descrição do livro)
    private String bookAuthor;  // Autor(es) do livro
    private String bookPub;    // book publisher/publisher company (Editora que publicou o livro )
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public UUID getBookId() {
        return bookId;
    }
    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public char getBookType() {
        return bookType;
    }
    public void setBookType(char bookType) {
        this.bookType = bookType;
    }
    public String getUrlImg() {
        return urlImg;
    }
    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getBookDesc() {
        return bookDesc;
    }
    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }
    public String getBookAuthor() {
        return bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public String getBookPub() {
        return bookPub;
    }
    public void setBookPub(String bookPub) {
        this.bookPub = bookPub;
    }

    
    
}

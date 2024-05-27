package com.apibooks.livros.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apibooks.livros.models.LivrosModels;

@Repository
public interface LivrosRepository extends JpaRepository<LivrosModels, UUID>{

}

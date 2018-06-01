package com.udemy.spring_boot_ionic_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.spring_boot_ionic_backend.domain.Categoria;
import com.udemy.spring_boot_ionic_backend.repositories.CategoriaRepository;
import com.udemy.spring_boot_ionic_backend.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: "+ Categoria.class.getName()));
	}

}

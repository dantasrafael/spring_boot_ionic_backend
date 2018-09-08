package com.udemy.spring_boot_ionic_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.spring_boot_ionic_backend.domain.Cidade;
import com.udemy.spring_boot_ionic_backend.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;

	public List<Cidade> findByEstado(Integer estadoId) {
		return repository.findCidades(estadoId);
	}
	
}

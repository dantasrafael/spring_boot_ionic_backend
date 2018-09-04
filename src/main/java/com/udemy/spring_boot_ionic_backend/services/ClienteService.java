package com.udemy.spring_boot_ionic_backend.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udemy.spring_boot_ionic_backend.domain.Cidade;
import com.udemy.spring_boot_ionic_backend.domain.Cliente;
import com.udemy.spring_boot_ionic_backend.domain.Endereco;
import com.udemy.spring_boot_ionic_backend.domain.enums.Perfil;
import com.udemy.spring_boot_ionic_backend.domain.enums.TipoCliente;
import com.udemy.spring_boot_ionic_backend.dto.ClienteDTO;
import com.udemy.spring_boot_ionic_backend.dto.ClienteNewDTO;
import com.udemy.spring_boot_ionic_backend.repositories.ClienteRepository;
import com.udemy.spring_boot_ionic_backend.repositories.EnderecoRepository;
import com.udemy.spring_boot_ionic_backend.security.UserSS;
import com.udemy.spring_boot_ionic_backend.services.exceptions.AuthorizationException;
import com.udemy.spring_boot_ionic_backend.services.exceptions.DataIntegrityException;
import com.udemy.spring_boot_ionic_backend.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;
	
	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: "+ Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);		
		obj = repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());		
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);		
		return repository.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			repository.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir poque há pedidos relacionados");
		}
	}
	
	public List<Cliente> findAll() {
		return repository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, (Cidade)cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user==null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		URI uri = s3Service.uploadFile(multipartFile);
		Cliente cli = find(user.getId());
		cli.setImageURL(uri.toString());
		repository.save(cli);
		
		return uri;
	}
	
}

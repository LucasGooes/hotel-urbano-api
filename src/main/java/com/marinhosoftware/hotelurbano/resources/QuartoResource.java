package com.marinhosoftware.hotelurbano.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marinhosoftware.hotelurbano.domain.Quarto;
import com.marinhosoftware.hotelurbano.domain.enums.StatusQuarto;
import com.marinhosoftware.hotelurbano.dto.QuartoDTO;
import com.marinhosoftware.hotelurbano.services.QuartoService;

@RestController
@RequestMapping(value = "/quartos")
public class QuartoResource {
	
	@Autowired
	private QuartoService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Quarto> find(@PathVariable Integer id) {
		Quarto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping("disponiveis")
	public ResponseEntity<List<Quarto>> buscarQuartosDisponiveis() {
		List<Quarto> lista = service.buscarQuartosDisponiveis(StatusQuarto.DISPONIVEL.getCod());;
		return ResponseEntity.ok().body(lista);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Quarto obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody QuartoDTO objDto, @PathVariable Integer id) {
		Quarto obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Quarto> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<QuartoDTO>> findAll() {
		List<Quarto> list = service.findAll();
		List<QuartoDTO> listDto = list.stream()
				.map(obj -> new QuartoDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
}

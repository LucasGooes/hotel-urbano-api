package com.marinhosoftware.hotelurbano.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marinhosoftware.hotelurbano.domain.Quarto;
import com.marinhosoftware.hotelurbano.domain.enums.StatusQuarto;
import com.marinhosoftware.hotelurbano.domain.enums.TipoQuarto;
import com.marinhosoftware.hotelurbano.dto.QuartoDTO;
import com.marinhosoftware.hotelurbano.repositories.QuartoRepository;
import com.marinhosoftware.hotelurbano.services.exceptions.DataIntegrityException;
import com.marinhosoftware.hotelurbano.services.exceptions.ObjectNotFoundException;

@Service
public class QuartoService {

	@Autowired
	private QuartoRepository repo;

	public Quarto find(Integer id) {
		Optional<Quarto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Quarto.class.getName()));
	}

	@Transactional
	public Quarto insert(Quarto obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Quarto update(Quarto obj) {
		Quarto newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	// SE FOR POSSIVEL, CRIAR O METODO ATUALIZAR TIPO QUARTO E ATUALIZAR STATUS DO
	// QUARTO E NORMALIZAR ISSO NO DIAGRAMA

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(
					"NÃO É POSSÍVEL EXCLUIR UM QUARTO QUE POSSUI RESERVAS E/OU MANUTENÇÕES REGISTRADAS");
		}
	}

	public List<Quarto> findAll() {
		return repo.findAll();
	}

	public Quarto fromDTO(QuartoDTO objDto) {
		return new Quarto(null, objDto.getNumero(), null, TipoQuarto.toEnum(objDto.getTipoQuarto()), StatusQuarto.toEnum(objDto.getStatus()), objDto.getValorDiaria());
	}

	private void updateData(Quarto newObj, Quarto obj) {
		newObj.setNumero(obj.getNumero());
		newObj.setTipoQuarto(obj.getTipoQuarto());
		newObj.setStatus(obj.getStatus());
	}

	public List<Quarto> buscarQuartosDisponiveis(int status) {
		return repo.findByStatus(status);
	}

}

package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.EstadoCivil;

public interface EstadoCivilDAOIf {

	public List<EstadoCivil> getEstadosCivil(EstadoCivil pEstadoCivil);
}
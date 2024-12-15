package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.NivelCompetencia;

public interface NivelCompetenciaDAOIf {

	public List<NivelCompetencia> getNiveisCompetencia(NivelCompetencia pNivelCompetencia);
}
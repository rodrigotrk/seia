package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Municipio;

public interface MunicipioDAOIf {

	public List<Municipio> getMunicipios(Municipio pMunicipio);

	Municipio getMunicipioByEmpreendimento(Empreendimento empreendimento);
}
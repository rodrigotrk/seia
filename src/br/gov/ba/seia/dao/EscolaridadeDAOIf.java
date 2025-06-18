package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Escolaridade;

public interface EscolaridadeDAOIf {

	public List<Escolaridade> getEscolaridades(Escolaridade pEscolaridade);
}
package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Secao;

public interface SecaoDAOIf {

	public List<Secao> getSecoes(Secao pSecao);
}
package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Orgao;

public interface OrgaoDAOIf {

	public List<Orgao> getOrgaos(Orgao pOrgao);
}
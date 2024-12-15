package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoVeiculo;
import br.gov.ba.seia.entity.TipoVeiculo;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpreendimentoVeiculoService {
	
	@Inject
	private IDAO<EmpreendimentoVeiculo> empreendimentoVeiculolDAO;
	@Inject
	private IDAO<TipoVeiculo> tipoVeiculolDAO;
	
	
	public void salvarEmpreendimentoVeiculo(EmpreendimentoVeiculo empVeic) {
		empreendimentoVeiculolDAO.salvarOuAtualizar(empVeic);
	}
	
	public List<TipoVeiculo> listarTipoVeiculo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoVeiculo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoVeiculolDAO.listarPorCriteria(criteria);
	}
	
	public List<EmpreendimentoVeiculo> listarEmpreendimentoVeiculoByEmpreedimento(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoVeiculo.class);
		criteria.add(Restrictions.eq("ideEmpreendimento", empreendimento));
		criteria.add(Restrictions.isNull("dtcExclusaoVeiculo"));
		criteria.setFetchMode("ideTipoVeiculo", FetchMode.JOIN);
		return empreendimentoVeiculolDAO.listarPorCriteria(criteria);
	}
	
	public void excluirEmpreendimentoVeiculo(EmpreendimentoVeiculo empVeic) {
		empVeic.setDtcExclusaoVeiculo(new Date());
		salvarEmpreendimentoVeiculo(empVeic);
	}
	
	public void excluirEmpreendimentoVeiculoByEmpreedimento( List<EmpreendimentoVeiculo> listEmpVeic) {
		if(!Util.isNull(listEmpVeic)){
			for (EmpreendimentoVeiculo empreendimentoVeiculo : listEmpVeic) {
				empreendimentoVeiculo.setDtcExclusaoVeiculo(new Date());
				salvarEmpreendimentoVeiculo(empreendimentoVeiculo);
			}
		}
	}
}

package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.util.Util;



@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutorgaLocalizacaoGeograficaFinalidadeService {

	@Inject
	private IDAO<OutorgaLocalizacaoGeograficaFinalidade> outorgaLocalizacaoGeograficaFinalidadeDAO;

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 06/01/2016
	 * @see adicionar o #ticket
	 */
	private DetachedCriteria obterCriteria(OutorgaLocalizacaoGeografica outorgaLocGeo) {
		return DetachedCriteria.forClass(OutorgaLocalizacaoGeograficaFinalidade.class)
				.createAlias("ideOutorgaLocalizacaoGeografica", "olg", JoinType.INNER_JOIN)
				.createAlias("ideTipoFinalidadeUsoAgua", "fin", JoinType.INNER_JOIN)
				.createAlias("olg.ideOutorga", "o", JoinType.INNER_JOIN)
				.createAlias("o.ideModalidadeOutorga", "mod", JoinType.INNER_JOIN)
				.add(Restrictions.eq("olg.ideOutorgaLocalizacaoGeografica", outorgaLocGeo.getIdeOutorgaLocalizacaoGeografica()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaFinalidade> obterListaOutorgaLocalizacaoGeograficaFinalidades(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {
		DetachedCriteria criteria = obterCriteria(outorgaLocalizacaoGeografica);
		return outorgaLocalizacaoGeograficaFinalidadeDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaFinalidade> obterListaOutorgaLocalizacaoGeograficaFinalidades(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica, boolean indCaptacao)  {
		DetachedCriteria criteria = obterCriteria(outorgaLocalizacaoGeografica);
		criteria.add(Restrictions.eq("indCaptacao", indCaptacao));
		return outorgaLocalizacaoGeograficaFinalidadeDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaFinalidade> listarOutorgaLocalizacaoGeoFinalidadeByIdRequerimentoAndIdeFin(Requerimento requerimento, TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeograficaFinalidade.class);
		criteria.createAlias("ideOutorgaLocalizacaoGeografica", "olg");
		criteria.createAlias("olg.ideLocalizacaoGeografica", "lg");
		criteria.createAlias("olg.ideOutorga", "o");
		criteria.createAlias("o.ideModalidadeOutorga", "mod"); // FCE - OUTORGA AQUICULTURA
		criteria.add(Restrictions.eq("o.ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("ideTipoFinalidadeUsoAgua", ideTipoFinalidadeUsoAgua));
		if(!ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId()) && !ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.AQUICULTURA_TANQUE_REDE.getId())){
			criteria.add(Restrictions.eq("indCaptacao", true));
		}
		List<OutorgaLocalizacaoGeograficaFinalidade> tempOutLocGeoFinalidade = outorgaLocalizacaoGeograficaFinalidadeDAO.listarPorCriteria(criteria); 
		for(OutorgaLocalizacaoGeograficaFinalidade outLocGeoFin : tempOutLocGeoFinalidade) {
			outLocGeoFin.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			if(outLocGeoFin.getIdeOutorgaLocalizacaoGeografica().getIdeOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade())) {
				outLocGeoFin.getIdeOutorgaLocalizacaoGeografica().getIdeOutorga().setTipoCaptacao(outLocGeoFin.getIdeOutorgaLocalizacaoGeografica().getIdeOutorga().getTipoCaptacaoCollection().iterator().next());
			}
		}
		return tempOutLocGeoFinalidade;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOutorgaLocGeoFinalidade(OutorgaLocalizacaoGeograficaFinalidade geograficaFinalidade)  {
		if (Util.isNullOuVazio(geograficaFinalidade.getIndExcluido()))
			geograficaFinalidade.setIndExcluido(false);
		outorgaLocalizacaoGeograficaFinalidadeDAO.salvarOuAtualizar(geograficaFinalidade);
		//solução do ticket 34217
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(OutorgaLocalizacaoGeografica outorgaLoc)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideOutorgaLocalizacaoGeografica", outorgaLoc);
		outorgaLocalizacaoGeograficaFinalidadeDAO.executarNamedQuery("OutorgaLocalizacaoGeograficaFinalidade.removeByideOutargaLocGeo", map);
	}
}
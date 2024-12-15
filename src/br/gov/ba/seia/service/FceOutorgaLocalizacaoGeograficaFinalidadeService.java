package br.gov.ba.seia.service;

import java.util.List;

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
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;

/**
 * 
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 06/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaLocalizacaoGeograficaFinalidadeService {

	@Inject
	private IDAO<FceOutorgaLocalizacaoGeograficaFinalidade> fceOutorgaLocalizacaoGeograficaFinalidadeIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeograficaFinalidade> listarFceOutorgaLocGeoFinalidadeBy(Requerimento requerimento, TipologiaEnum tipologiaEnum, TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeograficaFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua","tf", JoinType.INNER_JOIN)
			.createAlias("ideFceOutorgaLocalizacaoGeografica","folg", JoinType.INNER_JOIN)
			.createAlias("folg.ideLocalizacaoGeografica","lg", JoinType.INNER_JOIN)
			.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
			.createAlias("folg.ideFce","f", JoinType.INNER_JOIN)
			.createAlias("f.ideDadoOrigem","of", JoinType.INNER_JOIN)
			.createAlias("f.ideRequerimento","r", JoinType.INNER_JOIN)
			.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.eq("of.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
			.add(Restrictions.eq("folg.ideTipologia.ideTipologia", tipologiaEnum.getId()))
			.add(Restrictions.eq("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua", finalidadeUsoAguaEnum.getId()));
		List<FceOutorgaLocalizacaoGeograficaFinalidade> lista = fceOutorgaLocalizacaoGeograficaFinalidadeIDAO.listarPorCriteria(criteria);
		for(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade : lista){
			FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica();
			fceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeograficaFinalidade> listarTipoFinalidadeUsoAguaByFce(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeograficaFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua","ideTipoFinalidadeUsoAgua")
			.createAlias("ideFceOutorgaLocalizacaoGeografica","ideFceOutorgaLocalizacaoGeografica")
			.createAlias("ideFceOutorgaLocalizacaoGeografica.ideFce","fce")
			.createAlias("fce.ideAnaliseTecnica", "at")
			.add(Restrictions.eq("ideFceOutorgaLocalizacaoGeografica.ideFceOutorgaLocalizacaoGeografica", fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
		return fceOutorgaLocalizacaoGeograficaFinalidadeIDAO.listarPorCriteria(detachedCriteria);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeograficaFinalidade> listarTipoFinalidadeUsoAguaByFinalidade(TipoFinalidadeUsoAgua finalidadeUsoAgua, AnaliseTecnica analiseTecnica) {	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeograficaFinalidade.class)
				.createAlias("ideFceOutorgaLocalizacaoGeografica","folg")
				.createAlias("folg.ideFce","f")
				.createAlias("f.ideAnaliseTecnica","at")
				.createAlias("ideTipoFinalidadeUsoAgua","tf")
				.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", finalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()))
				.add(Restrictions.eq("at.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
				;
		return fceOutorgaLocalizacaoGeograficaFinalidadeIDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoGeograficaFinalidade(List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOuorgaLocalizacaoGeograficaFinalidade) {
		for(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade : listaFceOuorgaLocalizacaoGeograficaFinalidade){
			salvar(fceOutorgaLocalizacaoGeograficaFinalidade);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvar(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade)  {
		fceOutorgaLocalizacaoGeograficaFinalidadeIDAO.salvarOuAtualizar(fceOutorgaLocalizacaoGeograficaFinalidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade)  {
		fceOutorgaLocalizacaoGeograficaFinalidadeIDAO.remover(fceOutorgaLocalizacaoGeograficaFinalidade);	
	}

}
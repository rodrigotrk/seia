package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalizacaoGeograficaDAOImpl {
	
	@Inject
	private IDAO<LocalizacaoGeografica> localizacaoGeograficaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarTipologiaDadoConcedido(ProcessoAto processoAto)  {
			
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class)
			.createAlias("atoAmbientalTipologiaCollection", "aat", JoinType.INNER_JOIN)
			.createAlias("aat.ideAtoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("aa.documentoAtoCollection", "da", JoinType.INNER_JOIN)
			
			.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
			.createAlias("p.analiseTecnicaCollection", "at", JoinType.INNER_JOIN)
			.createAlias("at.fceCollection", "fce", JoinType.INNER_JOIN)
			.createAlias("fce.fceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN)
			.createAlias("folg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.INNER_JOIN)
			
			.add(Restrictions.eqProperty("aa.ideAtoAmbiental", "pa.atoAmbiental.ideAtoAmbiental"))
			.add(Restrictions.in("aa.ideAtoAmbiental", new Integer[] {
					AtoAmbientalEnum.AOUT.getId(),
					AtoAmbientalEnum.COUT.getId(),
					AtoAmbientalEnum.DOUT.getId(),
					AtoAmbientalEnum.PPV_OUT.getId(),
					AtoAmbientalEnum.ROUT.getId(),
					AtoAmbientalEnum.OUTP.getId(),
					AtoAmbientalEnum.OUTORGA.getId()
			}))
			.add(Restrictions.eqProperty("ideTipologia", "pa.tipologia.ideTipologia"))
			.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
			
			.add(Restrictions.eq("pa.ideProcessoAto", processoAto.getIdeProcessoAto()))
			
			.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
			.add(Restrictions.eq("fce.ideDadoOrigem", new DadoOrigem(DadoOrigemEnum.TECNICO.getId())))
					
			.setProjection(Projections.distinct(
				Projections.projectionList()
					.add(Projections.groupProperty("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica")
					.add(Projections.groupProperty("sc.ideSistemaCoordenada"), "ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.groupProperty("sc.nomSistemaCoordenada"), "ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.groupProperty("da.ideDocumentoObrigatorio.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio")
				)
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	public Collection<LocalizacaoGeografica> listarLocalizacaoReservaRequerimento(Integer ideProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		
		criteria
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("r.processoCollection", "proc", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			.add(Restrictions.eq("p.codPergunta", PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_14.getCod()))
			.add(Restrictions.eq("proc.ideProcesso", ideProcesso))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
				.add(Projections.property("sc.ideSistemaCoordenada"),"ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("sc.nomSistemaCoordenada"),"ideSistemaCoordenada.nomSistemaCoordenada")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	public Collection<LocalizacaoGeografica> listarLocalizacaoReservaNotificacao(Integer ideProcesso, Integer ideImovelRural)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("notificacaoMotivoNotificacaoCollection", "nmn", JoinType.INNER_JOIN)
			.createAlias("nmn.ideMotivoNotificacao", "mn1", JoinType.INNER_JOIN)
			.createAlias("nmn.motivoNotificacaoImovelCollection", "mni", JoinType.INNER_JOIN)
			.createAlias("mni.ideImovel", "im1", JoinType.INNER_JOIN)
			.createAlias("arquivoProcessoCollection", "ap", JoinType.INNER_JOIN)
			.createAlias("ap.motivoNotificacao","mn2", JoinType.INNER_JOIN)
			.createAlias("ap.ideImovel","im2", JoinType.INNER_JOIN)
			.createAlias("ap.localizacaoGeografica","lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada","sc", JoinType.INNER_JOIN)
			
			.add(Restrictions.eqProperty("mn1.ideMotivoNotificacao", "mn2.ideMotivoNotificacao"))
			.add(Restrictions.eqProperty("im1.ideImovel", "im2.ideImovel"))
			.add(Restrictions.eq("im1.ideImovel", ideImovelRural))
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
				.add(Projections.property("sc.ideSistemaCoordenada"),"ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("sc.nomSistemaCoordenada"),"ideSistemaCoordenada.nomSistemaCoordenada")
				.add(Projections.property("ideNotificacao"),"notificacao.ideNotificacao")
				.add(Projections.property("numNotificacao"),"notificacao.numNotificacao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarLocalizacaoGeograficaByNumProcessoAndModalidadeOutorga(Processo ideProcesso, ModalidadeOutorgaEnum modalidadeOutorgaEnum, TipoIntervencaoEnum tipoIntervencaoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		criteria
		.createAlias("ideRequerimento", "r")
		.createAlias("r.outorgaCollection", "o")
		.createAlias("o.ideModalidadeOutorga" , "mo")
		.createAlias("o.outorgaLocalizacaoGeograficaCollection", "olg")
		.createAlias("olg.ideLocalizacaoGeografica", "lg")
		.createAlias("olg.tipoIntervencao", "ti")
		.add(Restrictions.eq("ideProcesso", ideProcesso.getIdeProcesso()))
		.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()))
		.add(Restrictions.eq("ti.ideTipoIntervencao", tipoIntervencaoEnum.getId()))
		.setProjection(Projections.projectionList()
				.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
				;
		return localizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
 }
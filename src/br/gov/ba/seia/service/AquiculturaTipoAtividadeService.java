package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AquiculturaTipoAtividadeService {

	@Inject
	private IDAO<AquiculturaTipoAtividade> aquiculturaTipoAtividadeDAO;

	/**
	 *
	 * Método para retornar todos os {@link AquiculturaTipoAtividade} que estão com o ind_ativo = true;
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @return listaAquiculturaTipoAtividade
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarTipoAtividadeByIndAtivo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AquiculturaTipoAtividade.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return aquiculturaTipoAtividadeDAO.listarPorCriteria(criteria, Order.asc("ideAquiculturaTipoAtividade"));
	}

	/**
	 * Método que retorna a lista de {@link AquiculturaTipoAtividade} de acordo com as {@link AquiculturaTipoAtividadeEnum} passadas no parâmetro
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 09/02/2015
	 * @param listaAtividadeEnum
	 * @return listaAquiculturaTipoAtividade
	 * @throws Exception
	 * @see Melhoria #6590
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarTipoAtividadeByIde(List<AquiculturaTipoAtividadeEnum> listaAtividadeEnum) {
		List<AquiculturaTipoAtividade> lista = new ArrayList<AquiculturaTipoAtividade>();
		for(AquiculturaTipoAtividadeEnum atividadeEnum : listaAtividadeEnum){
			DetachedCriteria criteria = DetachedCriteria.forClass(AquiculturaTipoAtividade.class);
			criteria.add(Restrictions.eq("ideAquiculturaTipoAtividade", atividadeEnum.getId()));
			criteria.add(Restrictions.eq("indAtivo", true));
			lista.add(aquiculturaTipoAtividadeDAO.buscarPorCriteria(criteria));
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadeByFceOutorgaAquiculturaInRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AquiculturaTipoAtividade.class)
				.createAlias("ideEspecieAquiculturaTipoAtividade", "eat")
				.createAlias("ideFceAquicultura", "fa")
				.createAlias("eat.ideAquiculturaTipoAtividade", "ata")
				.createAlias("fa.ideFce", "f")
				.createAlias("f.ideRequerimento", "r")
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideFceAquiculturaEspecie"), "ideFceAquiculturaEspecie"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(AquiculturaTipoAtividade.class));
		return aquiculturaTipoAtividadeDAO.listarPorCriteria(criteria);
	}
	
	
	/*
	 * 
		SELECT DISTINCT ide_aquicultura_tipo_atividade 
		FROM fce_aquicultura_especie fae
		JOIN especie_aquicultura_tipo_atividade eat ON eat.ide_especie_aquicultura_tipo_atividade = fae.ide_especie_aquicultura_tipo_atividade
		JOIN fce_aquicultura fa ON fa.ide_fce_aquicultura = fae.ide_fce_aquicultura
		JOIN tipo_aquicultura ta ON ta.ide_tipo_aquicultura = fa.ide_tipo_aquicultura
		JOIN fce f ON f.ide_fce = fa.ide_fce
		WHERE f.ide_requerimento = 62243 AND f.ide_documento_obrigatorio = 1444 AND ta.ide_tipo_aquicultura_pai = 6
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadePreenchidosNoFceOutorga(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AquiculturaTipoAtividade.class, "ata")
				.createAlias("ata.especieAquiculturaTipoAtividadeCollection", "eat")
				.createAlias("eat.fceAquiculturaEspecieCollection", "fae")
				.createAlias("fae.ideFceAquicultura", "fa")
				.createAlias("fa.ideTipoAquicultura", "ta")
				.createAlias("ta.ideTipoAquiculturaPai", "taPai")
				.createAlias("fa.ideFce", "f")
				.add(Restrictions.eq("f.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("f.ideDocumentoObrigatorio.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()))
				.add(Restrictions.eq("taPai.ideTipoAquicultura", tipoAquiculturaEnum.getId()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ata.ideAquiculturaTipoAtividade"), "ideAquiculturaTipoAtividade")
						.add(Projections.property("ata.nomAquiculturaTipoAtividade"), "nomAquiculturaTipoAtividade")
						/*.add(Projections.groupProperty("ata.ideAquiculturaTipoAtividade"))
						.add(Projections.groupProperty("ata.nomAquiculturaTipoAtividade"))*/
						).setResultTransformer(new AliasToNestedBeanResultTransformer(AquiculturaTipoAtividade.class));
		return aquiculturaTipoAtividadeDAO.listarPorCriteria(criteria);
	}
}
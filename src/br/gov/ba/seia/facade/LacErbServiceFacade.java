package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;

import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ErbEquipamento;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacErb;
import br.gov.ba.seia.entity.LacErbEquipamento;
import br.gov.ba.seia.entity.LacErbEquipamentoPK;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LacLegislacaoPK;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.TipoCanalErb;
import br.gov.ba.seia.entity.TipoDelimitacaoTerreno;
import br.gov.ba.seia.entity.TipoModalidadeErb;
import br.gov.ba.seia.enumerator.TipoLegislacaoEnum;
import br.gov.ba.seia.service.ErbEquipamentoService;
import br.gov.ba.seia.service.LacErbEquipamentoService;
import br.gov.ba.seia.service.LacErbService;
import br.gov.ba.seia.service.LacLegislacaoService;
import br.gov.ba.seia.service.LegislacaoService;
import br.gov.ba.seia.service.TipoCanalErbService;
import br.gov.ba.seia.service.TipoDelimitacaoTerrenoService;
import br.gov.ba.seia.service.TipoModalidadeErbService;

/***
 * 
 * @author luis
 * 
 */
@Stateless
public class LacErbServiceFacade extends LacServiceFacade {

	@EJB
	private LacErbService lacErbService;

	@EJB
	private ErbEquipamentoService erbEquipamentoService;

	@EJB
	private LacErbEquipamentoService lacErbEquipamentoService;

	@EJB
	private TipoDelimitacaoTerrenoService tipoDelimitacaoTerrenoService;

	@EJB
	private LacLegislacaoService lacLegislacaoService;

	@EJB
	private TipoModalidadeErbService tipoModalidadeErbService;

	@EJB
	private TipoCanalErbService tipoCanalErbService;

	@EJB
	private LegislacaoService legislacaoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLacErb(LacErb lacErb, Collection<ErbEquipamento> equipamentos,
			Collection<LacLegislacao> legislacoes) throws Exception {
		this.preencherTipoDelimitacaoTerreno(lacErb);
		this.lacErbService.salvar(lacErb);
		this.salvarErbEquipamento(equipamentos, lacErb);
		this.salvarLacErbLegislacao(lacErb, legislacoes);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarLacErb(LacErb lacErb, List<ErbEquipamento> equipamentos,
			List<ErbEquipamento> equipamentosAComparar) throws Exception {
		this.preencherTipoDelimitacaoTerreno(lacErb);
		this.lacErbService.atualizar(lacErb);
		this.atualizarErbEquipamento(equipamentos, lacErb, equipamentosAComparar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificadoLacErb(Certificado certificado){
		String numeroCertificao = this.certificadoService.gerarNumeroCertificado(certificado);
		certificado.setNumCertificado(numeroCertificao);
		this.certificadoService.salvar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTokenCertificadoLac(Certificado certificado) throws Exception {
		this.certificadoService.gerarEValidarToken(certificado);
		this.certificadoService.atualizar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Certificado carregarCertificado(Integer ideRequerimento) throws Exception {
		return this.certificadoService.carregarByIdRequerimentoAndAtoLac(ideRequerimento);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarErbEquipamento(Collection<ErbEquipamento> equipamentos, LacErb lacErb,
			List<ErbEquipamento> equipamentosAComparar) {

		for (ErbEquipamento erbEquipamento : equipamentos) {
			// Verifica se o equipamento foi adicionado a grid
			if (erbEquipamento.getIdeErbEquipamento() < 0) {
				erbEquipamento.setIdeErbEquipamento(null);
			}
			this.erbEquipamentoService.salvarOuAtualizar(erbEquipamento);
			this.atualizarLacErbEquipamento(erbEquipamento.getLacErbEquipamentos(), lacErb, erbEquipamento);
		}

		this.deletarEquipamento((List<ErbEquipamento>) CollectionUtils.subtract(equipamentosAComparar, equipamentos),
				lacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void deletarEquipamento(List<ErbEquipamento> equips, LacErb lacErb)  {
		for (ErbEquipamento erbEquipamento : equips) {
			for (LacErbEquipamento lacErbEquipamento : erbEquipamento.getLacErbEquipamentos()) {
				this.lacErbEquipamentoService.remover(lacErbEquipamento.getLacErbEquipamentoPK());
			}

			this.erbEquipamentoService.remover(erbEquipamento);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarLacErbEquipamento(Collection<LacErbEquipamento> lista, LacErb lacErb,
			ErbEquipamento erbEquipamento) {
		for (LacErbEquipamento lacErbEquipamento : lista) {
			lacErbEquipamento.setLacErbEquipamentoPK(new LacErbEquipamentoPK(lacErb.getIdeLac(), erbEquipamento
					.getIdeErbEquipamento(), lacErbEquipamento.getTipoCanalErb().getIdeTipoCanalErb()));
			this.lacErbEquipamentoService.salvarOuAtualizar(lacErbEquipamento);
		}

	}

	private void preencherTipoDelimitacaoTerreno(LacErb lacErb) throws Exception {
		ArrayList<TipoDelimitacaoTerreno> list = new ArrayList<TipoDelimitacaoTerreno>(
				lacErb.getTipoDelimitacaoTerrenoCollection());
		for (TipoDelimitacaoTerreno tipoDelimitacaoTerreno : list) {
			tipoDelimitacaoTerreno = this.tipoDelimitacaoTerrenoService.carregar(tipoDelimitacaoTerreno
					.getIdeTipoDelimitacaoTerreno());
			tipoDelimitacaoTerreno.setLacErbCollection(new ArrayList<LacErb>());
			tipoDelimitacaoTerreno.getLacErbCollection().add(lacErb);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarLacErbLegislacao(Lac lac, Collection<LacLegislacao> legislacoes){
		for (LacLegislacao lacLegislacao : legislacoes) {
			lacLegislacao.setLac(lac);
			lacLegislacao.setLacLegislacaoPK(new LacLegislacaoPK(lac.getIdeLac(), lacLegislacao.getLegislacao()
					.getIdeLegislacao()));
			this.lacLegislacaoService.salvarOuAtualizar(lacLegislacao);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarErbEquipamento(Collection<ErbEquipamento> equipamentos, LacErb lacErb)  {
		for (ErbEquipamento erbEquipamento : equipamentos) {
			erbEquipamento.setIdeErbEquipamento(null);
			this.erbEquipamentoService.salvar(erbEquipamento);
			this.salvarLacErbEquipamento(erbEquipamento, lacErb);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarLacErbEquipamento(ErbEquipamento erbEquipamento, LacErb lacErb){
		for (LacErbEquipamento lacErbEquipamento : erbEquipamento.getLacErbEquipamentos()) {
			lacErbEquipamento.setLacErb(lacErb);
			lacErbEquipamento.setLacErbEquipamentoPK(new LacErbEquipamentoPK(lacErb.getIdeLac(), erbEquipamento
					.getIdeErbEquipamento(), lacErbEquipamento.getTipoCanalErb().getIdeTipoCanalErb()));
			this.lacErbEquipamentoService.salvar(lacErbEquipamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> carregarTipoDelimitacaoTerrenoByIdeLacErb(Integer pIdeLacErb)
			throws Exception {
		return this.tipoDelimitacaoTerrenoService.carregarByIdeLacErb(pIdeLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacErb carregarLacErbByIdeRequerimento(Integer id) {
		return this.lacErbService.carregarByIdRequerimento(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacLegislacao> carregarLacErbLegislacao(int pIdeLacErb) {
		return this.lacLegislacaoService.carregarByIdeLac(pIdeLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ErbEquipamento carregarErbEquipamentoById(int ideErbEquipamento) {
		return this.erbEquipamentoService.carregarByIde(ideErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacErbEquipamento> carregarLacErbEquipamentoByIdeLacErb(int id) {
		return this.lacErbEquipamentoService.carregarByIde(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> listarTiposDelimitacaoTerreno() throws Exception {
		return this.tipoDelimitacaoTerrenoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoModalidadeErb> listarTipoModalidadeErb() throws Exception {
		return this.tipoModalidadeErbService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoCanalErb> listarTipoCanalErb() throws Exception {
		return this.tipoCanalErbService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Legislacao> listarLegislacao() {
		return this.legislacaoService.listar(TipoLegislacaoEnum.LAC.getId());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasLac(Integer ideRequerimento)  {
		return this.lacErbService.hasLac(ideRequerimento);
	}

}

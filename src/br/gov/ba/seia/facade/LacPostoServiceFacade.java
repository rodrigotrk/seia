package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.gov.ba.seia.entity.AreaAbastecimentoPostoCombustivel;
import br.gov.ba.seia.entity.AreaAbastecimentoPostoCombustivelPK;
import br.gov.ba.seia.entity.ClasseNbrPosto;
import br.gov.ba.seia.entity.DistribuidoraPosto;
import br.gov.ba.seia.entity.FaseEmpreendimento;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LacLegislacaoPK;
import br.gov.ba.seia.entity.LacPostoCombustivel;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.PostoCombustivelProdutoComercializado;
import br.gov.ba.seia.entity.PostoCombustivelProdutosComercializadosPK;
import br.gov.ba.seia.entity.PostoCombustivelTanque;
import br.gov.ba.seia.entity.PostoCombustivelTanqueProduto;
import br.gov.ba.seia.entity.PostoCombustivelTipoServico;
import br.gov.ba.seia.entity.PostoCombustivelTipoServicoPK;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.SistemaControlePosto;
import br.gov.ba.seia.entity.TipoAreaAbastecimento;
import br.gov.ba.seia.entity.TipoBandeiraPostoCombustivel;
import br.gov.ba.seia.entity.TipoEquipamentoEntornoPosto;
import br.gov.ba.seia.entity.TipoEstruturaTanque;
import br.gov.ba.seia.entity.TipoParedeTanque;
import br.gov.ba.seia.entity.TipoPermeabilidade;
import br.gov.ba.seia.entity.TipoServicoPosto;
import br.gov.ba.seia.entity.TipoSistemaControlePosto;
import br.gov.ba.seia.entity.TipoTanquePosto;
import br.gov.ba.seia.enumerator.TipoLegislacaoEnum;
import br.gov.ba.seia.service.AreaAbastecimentoPostoCombustivelService;
import br.gov.ba.seia.service.ClasseNbrPostoService;
import br.gov.ba.seia.service.DistribuidoraPostoService;
import br.gov.ba.seia.service.LacLegislacaoService;
import br.gov.ba.seia.service.LacPostoCombustivelService;
import br.gov.ba.seia.service.LegislacaoService;
import br.gov.ba.seia.service.PostoCombustivelProdutoComercializadoService;
import br.gov.ba.seia.service.PostoCombustivelTanqueService;
import br.gov.ba.seia.service.PostoCombustivelTipoServicoService;
import br.gov.ba.seia.service.ProdutoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.SistemaControlePostoService;
import br.gov.ba.seia.service.TipoAreaAbastecimentoService;
import br.gov.ba.seia.service.TipoBandeiraPostoCombustivelService;
import br.gov.ba.seia.service.TipoEquipamentoEntornoPostoService;
import br.gov.ba.seia.service.TipoEstruturaTanqueService;
import br.gov.ba.seia.service.TipoParedeTanqueService;
import br.gov.ba.seia.service.TipoPermeabilidadeService;
import br.gov.ba.seia.service.TipoServicoPostoService;
import br.gov.ba.seia.service.TipoSistemaControlePostoService;
import br.gov.ba.seia.service.TipoTanquePostoService;
import br.gov.ba.seia.util.Util;

@Stateless
public class LacPostoServiceFacade extends LacServiceFacade {

	@EJB
	RequerimentoUnicoService requerimentoUnicoService;

	@EJB
	LacPostoCombustivelService lacPostoCombustivelService;

	@EJB
	TipoServicoPostoService servicoPostoService;

	@EJB
	ClasseNbrPostoService classeNbrPostoService;

	@EJB
	ProdutoService produtoService;

	@EJB
	TipoSistemaControlePostoService tipoSistemaControlePostoService;

	@EJB
	TipoEquipamentoEntornoPostoService tipoEquipamentoEntornoPostoService;

	@EJB
	TipoAreaAbastecimentoService tipoAreaAbastecimentoService;

	@EJB
	TipoParedeTanqueService tipoParedeTanqueService;

	@EJB
	TipoBandeiraPostoCombustivelService tipoBandeiraPostoCombustivelService;

	@EJB
	TipoPermeabilidadeService tipoPermeabilidadeService;

	@EJB
	TipoEstruturaTanqueService tipoEstruturaTanqueService;

	@EJB
	TipoTanquePostoService tipoTanquePostoService;

	@EJB
	DistribuidoraPostoService distribuidoraPostoService;

	@EJB
	LegislacaoService legislacaoService;

	@EJB
	AreaAbastecimentoPostoCombustivelService areaAbastecimentoPostoCombustivelService;

	@EJB
	PostoCombustivelTipoServicoService postoCombustivelTipoServicoService;

	@EJB
	PostoCombustivelProdutoComercializadoService postoCombustivelProdutoComercializadoService;

	@EJB
	PostoCombustivelTanqueService postoCombustivelTanqueService;
	
	@EJB
	SistemaControlePostoService sistemaControlePostoService;

	@EJB
	LacLegislacaoService lacLegislacaoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarLacPosto(LacPostoCombustivel lacPostoCombustivel)  {
		this.lacPostoCombustivelService.salvarOuAtualizar(lacPostoCombustivel);
		this.salvarOuAtualizarTiposServicoPosto(lacPostoCombustivel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuAtualizarTiposServicoPosto(LacPostoCombustivel lacPostoCombustivel)  {
		this.gerarPostoCombustivelTipoServicoPK(lacPostoCombustivel);
		this.removerTiposServicoPosto(lacPostoCombustivel.getIdeLac());
		this.salvarOuAtualizarTiposServicoPosto(lacPostoCombustivel.getPostoCombustivelTipoServicoCollection());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLacLegislacao(Lac lac, Collection<LacLegislacao> legislacoes){
		for (LacLegislacao lacLegislacao : legislacoes) {
			if (!Util.isNullOuVazio(lacLegislacao.getLegislacao().getIdeLegislacao())) {
				lacLegislacao.setLac(lac);
				lacLegislacao.setLacLegislacaoPK(new LacLegislacaoPK(lac.getIdeLac(), lacLegislacao.getLegislacao()
						.getIdeLegislacao()));
				this.lacLegislacaoService.salvarOuAtualizar(lacLegislacao);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarAreaAbastecimento(AreaAbastecimentoPostoCombustivel areaAbastecimentoPostoCombustivel)
			 {
		this.gerarPKAreaAbastecimento(areaAbastecimentoPostoCombustivel.getPostoCombustivel());
		this.salvarOuAtualizarAreasAbastecimento(areaAbastecimentoPostoCombustivel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarProdutosComercializados(
			PostoCombustivelProdutoComercializado postoCombustivelProdutoComercializado) throws Exception {
		this.gerarPKProdutoComercializado(postoCombustivelProdutoComercializado.getPostoCombustivel());
		this.salvarOuAtualizarProdutoComercializados(postoCombustivelProdutoComercializado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FaseEmpreendimento buscarFaseEmpreendimentoByIdeRequerimento(Integer ideRequerimento) throws Exception {
		return this.requerimentoUnicoService.carregaFaseEmpreendimentoByIdeRequerimento(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarPostoCombustivelTipoServicoPK(LacPostoCombustivel lacPostoCombustivel) {
		for (PostoCombustivelTipoServico postoCombustivelTipoServico : lacPostoCombustivel
				.getPostoCombustivelTipoServicoCollection()) {
			PostoCombustivelTipoServicoPK pk = new PostoCombustivelTipoServicoPK();
			pk.setIdePostoCombustivel(lacPostoCombustivel.getIdeLac());
			pk.setIdeTipoServico(postoCombustivelTipoServico.getTipoServicoPosto().getIdeTipoServicoPosto());
			postoCombustivelTipoServico.setPostoCombustivelTipoServicoPK(pk);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarPKAreaAbastecimento(LacPostoCombustivel lacPostoCombustivel) {
		for (AreaAbastecimentoPostoCombustivel areaAbastecimento : lacPostoCombustivel
				.getAreaAbastecimentoPostoCombustivelCollection()) {
			AreaAbastecimentoPostoCombustivelPK pk = new AreaAbastecimentoPostoCombustivelPK();
			pk.setIdeTipoAreaAbastecimento(areaAbastecimento.getTipoAreaAbastecimento().getIdeTipoAreaAbastecimento());
			pk.setIdePostoCombustivel(areaAbastecimento.getPostoCombustivel().getIdeLac());
			pk.setIdeAntesReforma(areaAbastecimento.getTipoPermeabilidadeAntesReforma().getIdeTipoPermeabilidade());
			pk.setIdeDepoisReforma(areaAbastecimento.getTipoPermeabilidadeDepoisReforma().getIdeTipoPermeabilidade());
			areaAbastecimento.setAreaAbastecimentoPostoCombustivelPK(pk);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarPKProdutoComercializado(LacPostoCombustivel lacPostoCombustivel){
		for (PostoCombustivelProdutoComercializado produtoComercializado : lacPostoCombustivel
				.getPostoCombustivelProdutosComercializadosCollection()) {
			gerarPkProdutoComercializado(produtoComercializado, lacPostoCombustivel);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarPkProdutoComercializado(PostoCombustivelProdutoComercializado produtoComercializado,
			LacPostoCombustivel lacPostoCombustivel) {
		PostoCombustivelProdutosComercializadosPK pk = new PostoCombustivelProdutosComercializadosPK();
		pk.setIdePostoCombustivel(lacPostoCombustivel.getIdeLac());
		pk.setideProduto(produtoComercializado.getProduto().getIdeProduto());
		produtoComercializado.setPostoCombustivelProdutosComercializadosPK(pk);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarPostoCombustivelTanque(PostoCombustivelTanque postoCombustivelTanque) {
		this.postoCombustivelTanqueService.salvarOuAtualizar(postoCombustivelTanque);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuAtualizarAreasAbastecimento(AreaAbastecimentoPostoCombustivel areaAbastecimentoPostoCombustivel)
			 {
		this.areaAbastecimentoPostoCombustivelService.salvarOuAtualizar(areaAbastecimentoPostoCombustivel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPostosTanque(Collection<PostoCombustivelTanque> listPostoCombustivelTanqueARemover)
			 {
		for (PostoCombustivelTanque postoCombustivelTanque : listPostoCombustivelTanqueARemover) {
			this.postoCombustivelTanqueService.removerProdutos(postoCombustivelTanque.getProdutoCollection());
			postoCombustivelTanque.setProdutoCollection(null);
		}
		this.postoCombustivelTanqueService.remover(listPostoCombustivelTanqueARemover);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerAreasAbastecimento(Collection<AreaAbastecimentoPostoCombustivel> listAreaAbastecimentoARemover)
			 {
		this.areaAbastecimentoPostoCombustivelService.remover(listAreaAbastecimentoARemover);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTiposServicoPosto(Integer ideLac) {
		this.postoCombustivelTipoServicoService.remover(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerProdutoComercializado(Integer ideProduto, Integer idePostoCombustivel)  {
		this.postoCombustivelProdutoComercializadoService.remover(ideProduto, idePostoCombustivel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuAtualizarTiposServicoPosto(
			Collection<PostoCombustivelTipoServico> postoCombustivelTipoServicoCollection) {
		this.postoCombustivelTipoServicoService.salvarOuAtualizar(postoCombustivelTipoServicoCollection);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuAtualizarProdutoComercializados(
			PostoCombustivelProdutoComercializado postoCombustivelProdutoComercializado) {
		this.postoCombustivelProdutoComercializadoService.salvarOuAtualizar(postoCombustivelProdutoComercializado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideRequerimento) throws Exception {
		return this.certificadoService.exists(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacPostoCombustivel carregarLacPostoByIdeRequerimento(Integer ideRequerimento) {
		return this.lacPostoCombustivelService.carregarByIdeRequerimento(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoServicoPosto> listarServicosPosto()  {
		return this.servicoPostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ClasseNbrPosto> listarClassesNbr() {
		return this.classeNbrPostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Produto> listarProdutos() {
		return this.produtoService.carregarListaProdutosByTipoProduto(2);//produtos de posto
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Produto carregarProdutoByIde(Integer ideProduto)  {
		return this.produtoService.carregarByIde(ideProduto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Produto carregarProdutoByDescricao(String dscProduto)  {
		return this.produtoService.carregarProdutoByDescricao(dscProduto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoSistemaControlePosto> listarTiposSistemaControle() {
		return this.tipoSistemaControlePostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoEquipamentoEntornoPosto> listarTiposEquipamentoEntornoPosto() {
		return this.tipoEquipamentoEntornoPostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAreaAbastecimento> listarTiposAreaAbastecimento() {
		return this.tipoAreaAbastecimentoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoParedeTanque> listarTiposParedeTanque() {
		return this.tipoParedeTanqueService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoBandeiraPostoCombustivel> listarTiposBandeiraPosto() {
		return this.tipoBandeiraPostoCombustivelService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoPermeabilidade> listarTiposPermeabilidade(){
		return this.tipoPermeabilidadeService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoEquipamentoEntornoPosto carregarTipoEquipamentoByIde(Integer ideTipoEquipamentoEntorno) {
		return this.tipoEquipamentoEntornoPostoService.carregarByIde(ideTipoEquipamentoEntorno);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoSistemaControlePosto carregarTipoSistemaControleByIde(Integer ideTipoSistemaControle) {
		return this.tipoSistemaControlePostoService.carregarByIde(ideTipoSistemaControle);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoAreaAbastecimento carregarTipoAreaAbastecimentoByIde(Integer ideTipoSistemaControle){
		return this.tipoAreaAbastecimentoService.carregarByIde(ideTipoSistemaControle);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoEstruturaTanque> listarTiposEstruturaTanque() {
		return this.tipoEstruturaTanqueService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoTanquePosto> listarTiposTanque() {
		return this.tipoTanquePostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DistribuidoraPosto> listarDistribuidorasPosto(){
		return this.distribuidoraPostoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoTanquePosto carregarTipoTanquePostoByIde(Integer ideTipoTanque)  {
		return this.tipoTanquePostoService.carregarByIde(ideTipoTanque);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoPermeabilidade carregarTipoPermeabilidadeByIde(Integer ideTipoPermeabilidade){
		return this.tipoPermeabilidadeService.carregarByIde(ideTipoPermeabilidade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PostoCombustivelProdutoComercializado> carregarProdutosComercializadosByIdeLac(Integer ideLac)
			 {
		return this.postoCombustivelProdutoComercializadoService.carregarProdutosComercializadosByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaAbastecimentoPostoCombustivel> carregarAreaAbastecimentoByIdeLac(Integer ideLac)
			 {
		return this.areaAbastecimentoPostoCombustivelService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaControlePosto> carregarSistemasDeControleByIdeLac(Integer ideLac) throws Exception {
		return this.sistemaControlePostoService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoEquipamentoEntornoPosto> carregarEquipamentosEntornoPostoByIdeLac(Integer ideLac)
			throws Exception {
		return this.tipoEquipamentoEntornoPostoService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PostoCombustivelTanque> carregarTanquesPostoByIdeLac(Integer ideLac){
		return this.postoCombustivelTanqueService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PostoCombustivelTipoServico> carregarPostoTipoServicoByIdeLac(Integer ideLac){
		return this.postoCombustivelTipoServicoService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Legislacao> carregarLegislacoesLacPosto()  {
		return this.legislacaoService.listar(TipoLegislacaoEnum.LAC.getId());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PostoCombustivelTanqueProduto> carregarProdutoTanqueByIdeTanque(Integer ideTanque)
			 {
		return this.postoCombustivelTanqueService.carregarProdutoTanqueByIdeTanque(ideTanque);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarEquipamentosEntorno(TipoEquipamentoEntornoPosto tipoEquipamentoEntornoPosto)
			 {
		this.tipoEquipamentoEntornoPostoService.salvarOuAtualizar(tipoEquipamentoEntornoPosto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarSistemaControle(SistemaControlePosto sistemaControlePosto) {
		this.sistemaControlePostoService.salvarOuAtualizar(sistemaControlePosto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEquipamentoEntorno(Lac lac, TipoEquipamentoEntornoPosto tipoEquipamentoEntornoPosto)
			throws Exception {
		this.tipoEquipamentoEntornoPostoService.removerByIdeLac(lac.getIdeLac(),
				tipoEquipamentoEntornoPosto.getIdeTipoEquipamentoEntornoPosto());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSistemaControle(SistemaControlePosto sistemaControlePosto) {
		this.sistemaControlePostoService.remover(sistemaControlePosto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacLegislacao> buscarLegislacaoByIdeLac(Integer ideLac){
		return this.lacLegislacaoService.carregarByIdeLac(ideLac);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasLac(Integer ideRequerimento) {
		return this.lacPostoCombustivelService.hasLac(ideRequerimento);
	}
}

package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeOrganismo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeSistemaCultivo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoInstalacao;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.Organismo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SistemaCultivo;
import br.gov.ba.seia.entity.TipoInstalacao;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeOrganismoService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeSistemaCultivoService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeTipoInstalacaoService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeTipoProducaoService;
import br.gov.ba.seia.service.OrganismoService;
import br.gov.ba.seia.service.SistemaCultivoService;
import br.gov.ba.seia.service.TipoInstalacaoService;
import br.gov.ba.seia.util.Util;

/**
 * Facade para gerenciar os services para gerenciar as transações de {@link CaracterizacaoCultivoDTO}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 28/10/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracterizacaoCultivoServiceFacade {

	@EJB
	private FceAquiculturaLicencaTipoAtividadeService fceAquiculturaLicencaTipoAtividadeService;
	@EJB
	private FceAquiculturaLicencaTipoAtividadeTipoProducaoService fceAquiculturaLicencaTipoAtividadeTipoProducaoService;
	@EJB
	private FceAquiculturaLicencaTipoAtividadeSistemaCultivoService fceAquiculturaLicencaTipoAtividadeSistemaCultivoService;
	@EJB
	private FceAquiculturaLicencaTipoAtividadeTipoInstalacaoService fceAquiculturaLicencaTipoAtividadeTipoInstalacaoService;
	@EJB
	private FceAquiculturaLicencaTipoAtividadeOrganismoService fceAquiculturaLicencaTipoAtividadeOrganismoService;
	
	/*
	 * Tabelas Associativas
	 */
	@EJB
	private SistemaCultivoService sistemaCultivoService;
	@EJB
	private TipoInstalacaoService tipoInstalacaoService;
	@EJB
	private OrganismoService organismoService;
	

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	public void montarRespostas(List<FceAquiculturaLicencaTipoAtividade> listaAuiculturaLicencaTipoAtividade) throws Exception {
		for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : listaAuiculturaLicencaTipoAtividade){
			for(FceAquiculturaLicencaTipoAtividadeTipoProducao producao : listarTipoProducaoResposta(caracterizacaoCultivo)){
				if(producao.getIdeTipoProducao().isFormasJovenes()){
					caracterizacaoCultivo.setProducaoFormaJovem(true);
					listarDependentesFormasJovens(caracterizacaoCultivo);
					caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().setFceAquiculturaLicencaTipoAtividadeTipoProducao(producao);
					selecionarSistemaCultivo(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem());
					selecionarTipoInstalacao(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem());
					selecionarOrganismos(caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem());
				} 
				else if(producao.getIdeTipoProducao().isEngorda()){
					caracterizacaoCultivo.setProducaoEngorda(true);
					listarDependentesEngorda(caracterizacaoCultivo);
					caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().setFceAquiculturaLicencaTipoAtividadeTipoProducao(producao);
					selecionarSistemaCultivo(caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda());
					selecionarTipoInstalacao(caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda());
				}
			}
		}
	}

	public void listarDependentesFormasJovens(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo) throws Exception{
		caracterizacaoCultivo.setAquiculturaProducaoDTOFormaJovem(new CaracterizacaoCultivoDTO(caracterizacaoCultivo, TipoProducaoEnum.FORMAS_JOVENS));
		caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().setFceAquiculturaLicencaTipoAtividadeTipoProducao(new FceAquiculturaLicencaTipoAtividadeTipoProducao(caracterizacaoCultivo, TipoProducaoEnum.FORMAS_JOVENS));
		caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().setListaSistemaCultivo(listarSistemaCultivoToFormasJovens());
		caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().setListaTipoInstalacao(listarTipoInstalacaoToFormasJovens());
		caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().setListaOrganismo(listarOrganismo());
	}

	public void listarDependentesEngorda(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo) throws Exception{
		caracterizacaoCultivo.setAquiculturaProducaoDTOEngorda(new CaracterizacaoCultivoDTO(caracterizacaoCultivo, TipoProducaoEnum.ENGORDA));
		caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().setFceAquiculturaLicencaTipoAtividadeTipoProducao(new FceAquiculturaLicencaTipoAtividadeTipoProducao(caracterizacaoCultivo, TipoProducaoEnum.ENGORDA));
		caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().setListaSistemaCultivo(listarSistemaCultivoToEngordaBy(caracterizacaoCultivo.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade()));
		caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().setListaTipoInstalacao(listarTipoInstalacaoToEngorda(caracterizacaoCultivo.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade()));
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	public List<FceAquiculturaLicencaTipoAtividadeTipoProducao> listarTipoProducaoResposta(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo) {
		return fceAquiculturaLicencaTipoAtividadeTipoProducaoService.listarFceAquiculturaLicencaTipoAtividadeProducaoBy(caracterizacaoCultivo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCultivo> listarSistemaCultivoToFormasJovens() throws Exception{
		List<SistemaCultivo> lista = sistemaCultivoService.listarSistemaCultivoBy(TipoProducaoEnum.FORMAS_JOVENS);
		for(SistemaCultivo cultivo : lista){
			cultivo.setTipoProducaoEnum(TipoProducaoEnum.FORMAS_JOVENS);
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCultivo> listarSistemaCultivoToEngordaBy(AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception{
		List<SistemaCultivo> lista = sistemaCultivoService.listarSistemaCultivoBy(TipoProducaoEnum.ENGORDA);
		for(SistemaCultivo cultivo : lista){
			cultivo.setTipoProducaoEnum(TipoProducaoEnum.ENGORDA);
		}
		return lista;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void selecionarSistemaCultivo(CaracterizacaoCultivoDTO dto) throws Exception {
		List<SistemaCultivo> listaSistemaCultivoSelecionado = sistemaCultivoService.listarSistemaCultivoBy(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao()); 
		for(SistemaCultivo sistemaCultivo : dto.getListaSistemaCultivo()){
			if(listaSistemaCultivoSelecionado.contains(sistemaCultivo)){
				sistemaCultivo.setRowSelect(true);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Organismo> listarOrganismo() {
		return organismoService.listarOrganismo();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void selecionarOrganismos(CaracterizacaoCultivoDTO dto) {
		List<Organismo> listaOrganismo = dto.getListaOrganismo();
		List<Organismo> listaOrganismoSelecionado = organismoService.listarOrganismoBy(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao());
		for(Organismo organismo : listaOrganismo){
			if(listaOrganismoSelecionado.contains(organismo)){
				organismo.setRowSelect(true);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacaoToEngorda(AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception{
		List<TipoInstalacao> lista = tipoInstalacaoService.listarTipoInstalacaoEngorda(aquiculturaTipoAtividade);
		for(TipoInstalacao instalacao : lista){
			instalacao.setTipoProducaoEnum(TipoProducaoEnum.ENGORDA);
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacaoToFormasJovens() throws Exception{
		List<TipoInstalacao> lista = tipoInstalacaoService.listarTipoInstalacaoFormasJovens();
		for(TipoInstalacao instalacao : lista){
			instalacao.setTipoProducaoEnum(TipoProducaoEnum.FORMAS_JOVENS);
		}
		return lista;
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void selecionarTipoInstalacao(CaracterizacaoCultivoDTO dto) throws Exception {
		List<TipoInstalacao> listaTipoInstalacaoSelecionado = tipoInstalacaoService.listarTipoInstalacaoBy(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao()); 
		for(TipoInstalacao tipoInstalacaoSalvo : listaTipoInstalacaoSelecionado){
			for(TipoInstalacao tipoInstalacaoEditado : dto.getListaTipoInstalacao()){
				if(tipoInstalacaoSalvo.equals(tipoInstalacaoEditado)){
					tipoInstalacaoEditado.setRowSelect(true);
					tipoInstalacaoEditado.setNumEstrutura(tipoInstalacaoSalvo.getNumEstrutura());
					if(tipoInstalacaoEditado.isRaceway()){
						dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().setTipoInstalacaoRaceways(true);
					}
					break;
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<TipoInstalacao> getListaTipoInstalacaoSelecionada(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) throws Exception{
		return tipoInstalacaoService.listarTipoInstalacaoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarCaracterizacaoCultivoToViveiroEscavadoBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividadeEnum atividadeEnum) {
		return listarFceAquiculturaLicencaTipoAtividadeByTipoAquicultura(fceAquiculturaLicenca, TipoAquiculturaEnum.VIVEIRO_ESCAVADO, atividadeEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarCaracterizacaoCultivoToViveiroEscavadoBy(Requerimento requerimento) throws Exception {
		return fceAquiculturaLicencaTipoAtividadeService.listarFceAquiculturaLicencaTipoAtividadeBy(TipoAquiculturaEnum.VIVEIRO_ESCAVADO, requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarCaracterizacaoCultivoToTanqueRedeBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividadeEnum atividadeEnum) {
		return listarFceAquiculturaLicencaTipoAtividadeByTipoAquicultura(fceAquiculturaLicenca, TipoAquiculturaEnum.TANQUE_REDE, atividadeEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarCaracterizacaoCultivoToTanqueRedeBy(Requerimento requerimento) throws Exception {
		return fceAquiculturaLicencaTipoAtividadeService.listarFceAquiculturaLicencaTipoAtividadeBy(TipoAquiculturaEnum.TANQUE_REDE, requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeByTipoAquicultura(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum, AquiculturaTipoAtividadeEnum atividadeEnum) {
		return fceAquiculturaLicencaTipoAtividadeService.listarFceAquiculturaLicencaTipoAtividadeBy(fceAquiculturaLicenca, tipoAquiculturaEnum, atividadeEnum);
	}
	/*
	 * FIM - BUSCAR
	 */
	
	/*
	 * INI - SALVAR
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaractericazaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		excluirAssociativas(fceAquiculturaLicencaTipoAtividade);
		fceAquiculturaLicencaTipoAtividadeService.salvarFceAquiculturaLicencaTipoAtividade(fceAquiculturaLicencaTipoAtividade);
		montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeTipoProducao(fceAquiculturaLicencaTipoAtividade);
		montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeSistemaCultivo(fceAquiculturaLicencaTipoAtividade);
		montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeTipoInstalacao(fceAquiculturaLicencaTipoAtividade);
		montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeOrganismo(fceAquiculturaLicencaTipoAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		List<FceAquiculturaLicencaTipoAtividadeTipoProducao> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeTipoProducao>();
		if(fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem()){
			fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setIdeFceAquiculturaLicencaTipoAtividadeTipoProducao(null);
			listaToSalvar.add(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao());
		} 
		else {
			fceAquiculturaLicencaTipoAtividade.setAquiculturaProducaoDTOFormaJovem(null);
		}
		if(fceAquiculturaLicencaTipoAtividade.isProducaoEngorda()){
			fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setIdeFceAquiculturaLicencaTipoAtividadeTipoProducao(null);
			listaToSalvar.add(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao());
		}
		else {
			fceAquiculturaLicencaTipoAtividade.setAquiculturaProducaoDTOEngorda(null);
		}
		fceAquiculturaLicencaTipoAtividadeTipoProducaoService.salvarListaFceAquiculturaLicencaTipoAtividadeTipoProducao(listaToSalvar);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeSistemaCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		List<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeSistemaCultivo>();
		if(fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem()){
			listaToSalvar.addAll(montarListaSistemaCultivoSelecionado(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem()));
		}
		if(fceAquiculturaLicencaTipoAtividade.isProducaoEngorda()){
			listaToSalvar.addAll(montarListaSistemaCultivoSelecionado(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda()));
		}
		fceAquiculturaLicencaTipoAtividadeSistemaCultivoService.salvarListaFceAquiculturaLicencaTipoAtividadeSistemaCultivo(listaToSalvar);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	private List<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> montarListaSistemaCultivoSelecionado(CaracterizacaoCultivoDTO dto) {
		List<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeSistemaCultivo>();
		for(SistemaCultivo cultivo : dto.getListaSistemaCultivo()){
			if(cultivo.isRowSelect()){
				listaToSalvar.add(new FceAquiculturaLicencaTipoAtividadeSistemaCultivo(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao(), cultivo));
			}
		}
		return listaToSalvar;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeTipoInstalacao(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		List<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeTipoInstalacao>();
		if(fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem()){
			listaToSalvar.addAll(montarListaTipoInstalacaoSelecionado(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem()));
		}
		if(fceAquiculturaLicencaTipoAtividade.isProducaoEngorda()){
			listaToSalvar.addAll(montarListaTipoInstalacaoSelecionado(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda()));
		}
		fceAquiculturaLicencaTipoAtividadeTipoInstalacaoService.salvarListaFceAquiculturaLicencaTipoAtividadeTipoInstalacao(listaToSalvar);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	private List<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> montarListaTipoInstalacaoSelecionado(CaracterizacaoCultivoDTO dto) {
		List<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeTipoInstalacao>();
		for(TipoInstalacao instalacao : dto.getListaTipoInstalacao()){
			if(instalacao.isRowSelect()){
				listaToSalvar.add(new FceAquiculturaLicencaTipoAtividadeTipoInstalacao(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao(), instalacao));
			}
		}
		return listaToSalvar;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void montarAndSalvarListaFceAquiculturaLicencaTipoAtividadeOrganismo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		if(!Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem())){
			fceAquiculturaLicencaTipoAtividadeOrganismoService.salvarListaFceAquiculturaLicencaTipoAtividadeOrganismo(montarListaOrganismoSelecionado(fceAquiculturaLicencaTipoAtividade));
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	private List<FceAquiculturaLicencaTipoAtividadeOrganismo> montarListaOrganismoSelecionado(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		List<FceAquiculturaLicencaTipoAtividadeOrganismo> listaToSalvar = new ArrayList<FceAquiculturaLicencaTipoAtividadeOrganismo>();
		for(Organismo organismo : fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem().getListaOrganismo()){
			if(organismo.isRowSelect()){
				listaToSalvar.add(new FceAquiculturaLicencaTipoAtividadeOrganismo(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao(), organismo));
			}
		}
		return listaToSalvar;
	}
	/*
	 * FIM - SALVAR
	 */
	
	/*
	 * INI - EXCLUIR
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoAtividadeBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) {
		if(!Util.isNull(fceAquiculturaLicenca)){
			fceAquiculturaLicencaTipoAtividadeService.excluirListaFceAquiculturaLicencaTipoAtividadeBy(fceAquiculturaLicenca, tipoAquiculturaEnum);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		excluirAssociativas(fceAquiculturaLicencaTipoAtividade);
		fceAquiculturaLicencaTipoAtividadeService.excluirFceAquiculturaLicencaTipoAtividadeBy(fceAquiculturaLicencaTipoAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSemSaberTipoProducao(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		for(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao : listarTipoProducaoResposta(fceAquiculturaLicencaTipoAtividade)){
			excluirAssociativas(fceAquiculturaLicencaTipoAtividadeTipoProducao);
			excluirListaFceAquiculturaLicencaOrganismoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
		}
		excluirListaFceAquiculturaLicencaTipoProducaoBy(fceAquiculturaLicencaTipoAtividade);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAssociativas(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception {
		if(Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem()) && Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda())){
			excluirSemSaberTipoProducao(fceAquiculturaLicencaTipoAtividade);
		} else {
			if(!Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem())){
				excluirAssociativasFormasJovens(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao());
			}
			if(!Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda())){
				excluirAssociativas(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao());
			}
		}
		excluirListaFceAquiculturaLicencaTipoProducaoBy(fceAquiculturaLicencaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected void excluirAssociativasFormasJovens(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) throws Exception {
		excluirAssociativas(fceAquiculturaLicencaTipoAtividadeTipoProducao);
		excluirListaFceAquiculturaLicencaOrganismoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirAssociativas(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		excluirListaFceAquiculturaLicencaSistemaCultivoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
		excluirListaFceAquiculturaLicencaTipoInstalacaoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaFceAquiculturaLicencaTipoProducaoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade)  {
		fceAquiculturaLicencaTipoAtividadeTipoProducaoService.excluirFceAquiculturaLicencaTipoAtividadeTipoProducaoBy(fceAquiculturaLicencaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaFceAquiculturaLicencaSistemaCultivoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		fceAquiculturaLicencaTipoAtividadeSistemaCultivoService.excluirFceAquiculturaLicencaTipoAtividadeSistemaCultivoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaFceAquiculturaLicencaTipoInstalacaoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		fceAquiculturaLicencaTipoAtividadeTipoInstalacaoService.excluirFceAquiculturaLicencaTipoAtividadeTipoInstalacaoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaFceAquiculturaLicencaOrganismoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		fceAquiculturaLicencaTipoAtividadeOrganismoService.excluirFceAquiculturaLicencaTipoAtividadeOrganismoBy(fceAquiculturaLicencaTipoAtividadeTipoProducao);
	}
	/*
	 * FIM - EXCLUIR
	 */
}
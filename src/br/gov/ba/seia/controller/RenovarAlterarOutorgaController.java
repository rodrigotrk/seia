package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovelPK;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.TipoAlteracao;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.TipoSolicitacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("renovarAlterarOutorgaController")
@ViewScoped
public class RenovarAlterarOutorgaController extends BaseDialogOutorgaController {

	private Outorga outorga;

	private List<TipoSolicitacao> tiposSolicitacao;
	private List<ModalidadeOutorga> modalidadesOutorga;
	private List<TipoCaptacao> tiposCaptacao;
	private List<TipoAlteracao> tiposAlteracao;
	private List<TipoAlteracao> listaOriginal;

	@Inject
	private AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoAlteracaoProrrogacaoController;
	
	@EJB
	private TipoSolicitacaoService tipoSolicitacaoService;

	@EJB
	private OutorgaService outorgaService;

	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService;
	
	@EJB
	private ImovelService imovelService;

	
	@Override
	public void load() {
		try {
			this.limpar();
			this.carregarListas();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			this.outorga = (Outorga) objeto;
			
			this.carregarListas();
			OutorgaTipoCaptacao outorgaTipoCaptacao = this.outorgaService.buscarOutorgaTipoCaptacaoByIdeOutorga(this.outorga);
			
			if(!Util.isNullOuVazio(outorgaTipoCaptacao)){
				this.outorga.setTipoCaptacao(outorgaTipoCaptacao.getIdeTipoCaptacao());
			}
			this.outorga.setOutorgaLocalizacaoGeograficaCollection(new ArrayList<OutorgaLocalizacaoGeografica>());
			
			List<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeografica = this.outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeoByIdOutorga(outorga);
			if(outorgaLocalizacaoGeografica != null)
				this.outorga.getOutorgaLocalizacaoGeograficaCollection().addAll(outorgaLocalizacaoGeografica);
			
			for (OutorgaLocalizacaoGeografica olg : this.outorga.getOutorgaLocalizacaoGeograficaCollection()) {
				carregarFinalidadesUsoAgua(olg);
				olg.setListaImovel(imovelService.listarImovelPorOutorgaLocalizacaoGeografica(olg.getIdeOutorgaLocalizacaoGeografica()));
			}
			
			super.editMode = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	@Override
	public void limpar() {
		this.outorga = new Outorga(super.getRequerimento());
		super.editMode = false;
	}
	
	public void fecharTela() {
		LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController = (LocalizacaoGeograficaGenericController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaGenericController}", LocalizacaoGeograficaGenericController.class);
		localizacaoGeograficaGenericController.fecharDialog();
	}

	private void carregarListas() throws Exception {
		this.tiposSolicitacao = this.tipoSolicitacaoService.listarTipoSolicitacaoOutorga();
		this.tiposCaptacao = this.outorgaService.listarTipoCaptacao();
		this.modalidadesOutorga = this.outorgaService.listarModalidadeOutorgaSemPerfuracaoPoco();
		this.tiposAlteracao = this.outorgaService.carregaListaTipoAlteracao();
		this.listaOriginal = new ArrayList<TipoAlteracao>(this.tiposAlteracao);
	}
	
	public void valueChangeTipoSolicitacao(ValueChangeEvent event){
		this.outorga.setTipoCaptacao(null); 
		this.outorga.setIdeTipoAlteracao(null);
		this.outorga.setIdeModalidadeOutorga(null);
	}
	
	public void valueChangeTipoModalidade(ValueChangeEvent event){
		this.outorga.setTipoCaptacao(null); 
		this.outorga.setIdeTipoAlteracao(null);
		
		this.tiposAlteracao.clear();
		this.tiposAlteracao.addAll(this.listaOriginal);
		
		ModalidadeOutorga modalidade = (ModalidadeOutorga) event.getNewValue();

		if(modalidade.getIdeModalidadeOutorga() == 1) {
			this.tiposAlteracao.remove(new TipoAlteracao(7, "Alterar o efluente", Boolean.TRUE));
		} else if(modalidade.getIdeModalidadeOutorga() == 2) {
			this.tiposAlteracao.remove(new TipoAlteracao(6, "Alterar apenas a(s) finalidade(s)", Boolean.TRUE));
			this.tiposAlteracao.remove(new TipoAlteracao(8, "Desmembramento", Boolean.TRUE));
		}
	}
	
	@Override
	public void salvar() {
		try {
			
			if(validar()){
				
				this.outorgaService.salvarAtualizarOutorga(this.outorga);
				
				this.outorgaService.excluirTipoCaptacaoByIdeOutorga(this.outorga);
				
				this.salvarOutorgaTipoCaptacao();
				this.salvarOutorgasLocalizacaoGeografica();
				
				this.abaRenovacaoAlteracaoProrrogacaoController.adicionarOuAtualizarOutorga(outorga);
				
				for (OutorgaLocalizacaoGeografica olg : this.outorga.getOutorgaLocalizacaoGeograficaCollection()) {
					this.salvarOutorgaLocalizacaoGeograficaFinalidade(olg);
				}
				this.abaRenovacaoAlteracaoProrrogacaoController.atualizarVazaoTotalOutorgas();
				
				if(this.editMode){
					JsfUtil.addSuccessMessage("Outorga atualizada com sucesso.");
				}else{
					JsfUtil.addSuccessMessage("Outorga salva com sucesso.");
				}
				
				RequestContext.getCurrentInstance().execute("dialogAlterCancelOutorga.hide()");
			}
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	private void salvarOutorgasLocalizacaoGeografica() throws Exception {
		Collection<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica = this.outorga.getOutorgaLocalizacaoGeograficaCollection();
		if (!Util.isNullOuVazio(listaOutorgaLocalizacaoGeografica)) {
			for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocalizacaoGeografica) {
				outorgaLocalizacaoGeografica.setIdeOutorga(outorga);
				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(outorgaLocalizacaoGeografica);
				this.salvarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica);
			}
		}
	}

	private void salvarOutorgaTipoCaptacao() throws Exception {
		if(Util.isNullOuVazio( this.outorga.getTipoCaptacao()))
			return;
		OutorgaTipoCaptacao outorgaTipoCaptacao = new OutorgaTipoCaptacao(outorga, this.outorga.getTipoCaptacao());
		outorgaService.salvarAtualizarOutorgaTipoCaptacao(outorgaTipoCaptacao);
	}

	@Override
	public boolean validar() {
		
		boolean valido = true;
		
		if (Util.isNullOuVazio(this.outorga.getIdeTipoSolicitacao())) {
			JsfUtil.addWarnMessage("Por favor, selecione uma opção no campo 1.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorga.getNumProcessoOutorga())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorga.getNumPortariaOutorga())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
			valido = false;
		} else if (!Util.validaTamanhoString(this.outorga.getNumPortariaOutorga(), 50)) {
			JsfUtil.addWarnMessage("Número de Portaria não pode exceder 50 caracteres.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorga.getDtcPublicacaoPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 4.");
			valido = false;
		} else if (this.outorga.getDtcPublicacaoPortaria().after(new Date())) {
			JsfUtil.addWarnMessage("A data de publicação da portaria não pode ser superior a data de hoje.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorga.getDtcValidadeOutorga())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
			valido = false;
		} else if (!Util.isNullOuVazio(this.outorga.getDtcPublicacaoPortaria()) && this.outorga.getDtcValidadeOutorga().before(this.outorga.getDtcPublicacaoPortaria())) {
			JsfUtil.addWarnMessage("A data de validade não pode ser inferior a data de publicação.");
			valido = false;
		}

		if (!Util.isNullOuVazio(this.outorga) 
				&& !Util.isNullOuVazio(this.outorga.getIdeTipoSolicitacao()) && !Util.isNullOuVazio(this.outorga.getIdeTipoSolicitacao().getIdeTipoSolicitacao())
				&& this.outorga.getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(TipoSolicitacaoEnum.ALTERACAO_OUTORGA.getId())) {
			
			if (Util.isNullOuVazio(this.outorga.getIdeModalidadeOutorga())) {
				
				JsfUtil.addWarnMessage("Por favor, selecione uma opção no campo 6.");
				valido = false;
			
			} else {
				
				if(Util.isNullOuVazio(this.outorga.getIdeTipoAlteracao()) && Util.isNullOuVazio(this.outorga.getTipoCaptacao())
						&& this.outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade())) {
					
					JsfUtil.addWarnMessage("Por favor, selecione 'Captação superficial' ou 'Captação subterrânea'.");
					valido = false;
					
				} else if (Util.isNullOuVazio(this.outorga.getIdeTipoAlteracao()) && 
						(this.outorga.getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES.getIdModalidade()) 
						|| (!Util.isNullOuVazio(this.outorga.getTipoCaptacao()) && this.outorga.getTipoCaptacao().getIdeTipoCaptacao().equals(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId()))
						|| (!Util.isNullOuVazio(this.outorga.getTipoCaptacao()) && this.outorga.getTipoCaptacao().getIdeTipoCaptacao().equals(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId())))) {
					
					JsfUtil.addWarnMessage("Por favor, selecione uma opção no campo 6.1.");
					valido = false;
					
				} else if (Util.isNullOuVazio(this.outorga.getOutorgaLocalizacaoGeograficaCollection())) {
					
					JsfUtil.addWarnMessage("Por favor, inclua dados da localização geográfica no campo 7.");
					valido = false;
				}
			}
		}
	
		return valido;
	}

	public void salvarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocGeo) {
		try {
			OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel = new OutorgaLocalizacaoGeograficaImovel();
			OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK = new OutorgaLocalizacaoGeograficaImovelPK();
			for (Imovel imovel : outorgaLocGeo.getListaImovel()) {
				outorgaLocalizacaoGeograficaImovelPK.setIdeImovel(imovel.getIdeImovel());
				outorgaLocalizacaoGeograficaImovelPK.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeo.getIdeOutorgaLocalizacaoGeografica());
				outorgaLocalizacaoGeograficaImovel.setIdeImovel(imovel);
				outorgaLocalizacaoGeograficaImovel.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeo);
				outorgaLocalizacaoGeograficaImovel.setOutorgaLocalizacaoGeograficaImovelPK(outorgaLocalizacaoGeograficaImovelPK);
				this.outorgaLocalizacaoGeograficaService.salvarAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void adicionarOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		List<OutorgaLocalizacaoGeografica> localizacoesGeograficas = (List<OutorgaLocalizacaoGeografica>) this.outorga.getOutorgaLocalizacaoGeograficaCollection();
		if(localizacoesGeograficas.contains(outorgaLocalizacaoGeografica)){
			int posicao = localizacoesGeograficas.indexOf(outorgaLocalizacaoGeografica);
			localizacoesGeograficas.set(posicao, outorgaLocalizacaoGeografica);
		}else{
			localizacoesGeograficas.add(outorgaLocalizacaoGeografica);
		}
	}
	
	public void excluirLocalizacaoOutorga(){
		try {
			if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica))) {
				this.outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica);
			}
			
			if(!Util.isNullOuVazio(outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica()))
				outorgaLocalizacaoGeograficaFinalidadeService.excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(outorgaLocalizacaoGeografica);
			
			this.outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
			
			this.outorga.getOutorgaLocalizacaoGeograficaCollection().remove(outorgaLocalizacaoGeografica);
			
			JsfUtil.addSuccessMessage("Localização geográfica excluída com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Outorga getOutorga() {
		return outorga;
	}

	public void setOutorga(Outorga outorga) {
		this.outorga = outorga;
	}

	public Collection<TipoSolicitacao> getTiposSolicitacao() {
		return tiposSolicitacao;
	}

	public void setTiposSolicitacao(List<TipoSolicitacao> tiposSolicitacao) {
		this.tiposSolicitacao = tiposSolicitacao;
	}

	public List<ModalidadeOutorga> getModalidadesOutorga() {
		return modalidadesOutorga;
	}

	public void setModalidadesOutorga(List<ModalidadeOutorga> modalidadesOutorga) {
		this.modalidadesOutorga = modalidadesOutorga;
	}

	public Collection<TipoCaptacao> getTiposCaptacao() {
		return tiposCaptacao;
	}

	public void setTiposCaptacao(List<TipoCaptacao> tiposCaptacao) {
		this.tiposCaptacao = tiposCaptacao;
	}

	public List<TipoAlteracao> getTiposAlteracao() {
		return tiposAlteracao;
	}

	public void setTiposAlteracao(List<TipoAlteracao> tiposAlteracao) {
		this.tiposAlteracao = tiposAlteracao;
	}

	public Date getDataAtual() {
		return new Date();
	}

	
	/**
	 * @return the listaOriginal
	 */
	public List<TipoAlteracao> getListaOriginal() {
	
		return listaOriginal;
	}

	
	/**
	 * @param listaOriginal the listaOriginal to set
	 */
	public void setListaOriginal(List<TipoAlteracao> listaOriginal) {
	
		this.listaOriginal = listaOriginal;
	}

}

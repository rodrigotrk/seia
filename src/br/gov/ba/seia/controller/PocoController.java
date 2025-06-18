package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("pocoController")
@ViewScoped
public class PocoController extends BaseDialogOutorgaController {

	private Collection<Imovel> imoveis;
	
	@Override
	public void load() {
		
		try {
			
			this.limpar();
			
			Outorga outorgaPoco = this.outorgaService.buscarOutorgaByModalidadeAndRequerimento(ModalidadeOutorgaEnum.POCO ,super.getRequerimento());
			if(!Util.isNullOuVazio(outorgaPoco)){
				this.outorga = outorgaPoco;
				this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(this.outorga);
			}
			
			this.editMode = false;
			
			this.carregarImoveisParaOutorga();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void fecharTela() {
		LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController = (LocalizacaoGeograficaGenericController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaGenericController}", LocalizacaoGeograficaGenericController.class);
		localizacaoGeograficaGenericController.setIsPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede(false);
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			this.outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objeto;
			this.outorgaLocalizacaoGeografica.setListaImovel(new ArrayList<Imovel>());
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();
			
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			this.editMode = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void visualizar(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
			this.outorgaLocalizacaoGeografica.setListaImovel(new ArrayList<Imovel>());
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();
			
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			this.editMode = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){
				
				this.salvarOutorga(ModalidadeOutorgaEnum.POCO);

				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(this.outorgaLocalizacaoGeografica);
				
				this.salvarOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeografica);
				
				if(this.editMode)
					JsfUtil.addSuccessMessage("Perfuração de poço atualizada com sucesso.");
				else
					JsfUtil.addSuccessMessage("Perfuração de poço salva com sucesso.");
				
				if(!Util.isNullOuVazio(outorgaLocalizacaoGeografica) && !Util.isNullOuVazio(outorgaLocalizacaoGeografica.getListaImovel()) && !Util.isNullOuVazio(outorgaLocalizacaoGeografica.getListaImovel().get(0).getImovelRural()))
					outorgaLocalizacaoGeografica.setNomeImovel(outorgaLocalizacaoGeografica.getListaImovel().get(0).getImovelRural().getDesDenominacao());
				else
					outorgaLocalizacaoGeografica.setNomeImovel(novoRequerimentoController.getEmpreendimento().getNomEmpreendimento());
				
				this.abaOutorgaController.adicionarOuAtualizarPoco(outorgaLocalizacaoGeografica);
				
				RequestContext.getCurrentInstance().execute("dialogPoco.hide()");
				
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	boolean validar() {
		boolean valido = true;
		if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica()) || !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
			JsfUtil.addWarnMessage("Por favor, inclua os pontos.");
			valido  = false;	
		}
		if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
			JsfUtil.addWarnMessage("Por favor, selecione um Imóvel.");
			valido = false;
		}
		
		return valido;
	}

	public Collection<Imovel> getImoveis() {
		return imoveis;
	}

	public void setImoveis(Collection<Imovel> imoveis) {
		this.imoveis = imoveis;
	}

}

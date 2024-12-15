package br.gov.ba.seia.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("listaImoveisController")
@ViewScoped
public class ListaImoveisController extends SeiaControllerAb {
	
	private Empreendimento empreendimento;
	private Pessoa donoEmpreendimento;	
	private Imovel imovelSelecionado;
	private ImovelRural imovelRuralSelecionado;
	private boolean desabilitarInclusaoOuRemocaoDeVinculo;
	private LazyDataModel<ImovelRural> listaImovelRuralModel;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private EnderecoService enderecoService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private PessoaService pessoaService;

	@PostConstruct
	public void init() {
		carregarEmpreendimento();
		carregarDonoEmpreendimento();
		setarDesabilitarInclusaoOuRemocaoDeVinculo();
		if(!Util.isNullOuVazio(donoEmpreendimento)) consultar();
		
	}
	
	public void setarDesabilitarInclusaoOuRemocaoDeVinculo(){
		Exception erro = null;
		try{
			Requerimento req = requerimentoUnicoService.recuperarUltimoRequerimento(empreendimento);
			if(req != null){
				Integer ideDoStatusAtaulDoRequerimento = requerimentoUnicoService.retornaTramitacaoRequerimentoUnico(req.getIdeRequerimento());			
				desabilitarInclusaoOuRemocaoDeVinculo = validarInclusaoOuRemocaoDeVinculo(Arrays.asList(arrayDeStatusParaDesabilidar()),ideDoStatusAtaulDoRequerimento);	
			}else{
				//Para o caso de não haver nenhum requerimento
				desabilitarInclusaoOuRemocaoDeVinculo = validarInclusaoOuRemocaoDeVinculo(null, null);
			}
			
		}
		catch(Exception e){
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			desabilitarInclusaoOuRemocaoDeVinculo = true;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private boolean validarInclusaoOuRemocaoDeVinculo(List<Integer> desabilitarNosStatus, Integer ideStatusRequerimento) {
		if (desabilitarNosStatus != null && ideStatusRequerimento != null
				&& desabilitarNosStatus.contains(ideStatusRequerimento)) {
			return true;

		} else {
			return false;
		}
		
	}

	private Integer[] arrayDeStatusParaDesabilidar() {
		Integer elementos[] = {
				StatusRequerimentoEnum.VALIDADO.getStatus(),
				StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus(),
				StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus(),
				StatusRequerimentoEnum.FORMADO.getStatus(),
				StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus()				
		};
		return elementos;
	}
	
	public void consultar() {
		listaImovelRuralModel = new LazyDataModel<ImovelRural>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ImovelRural> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<ImovelRural> lista = null;
				
				Exception erro = null;
				try {
					setPageSize(pageSize);
					lista = populateListImovelRural(first, pageSize);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
				return lista;
			}
		};
		listaImovelRuralModel.setRowCount(getCountRowsImoveis());
	}
	
	public List<ImovelRural> populateListImovelRural(int first, int pageSize){
		Exception erro = null;
		try {
			return imovelRuralService.listarImovelRuralByProprietario(donoEmpreendimento, first, pageSize);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return new ArrayList<ImovelRural>();
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregarImovelComEndereco(){
		if(!Util.isNullOuVazio(this.imovelSelecionado) && !Util.isNullOuVazio(this.imovelSelecionado.getIdeEndereco())) {
			this.imovelSelecionado.setIdeEndereco(enderecoService.carregar(this.imovelSelecionado.getIdeEndereco().getIdeEndereco()));
		}
	}
	
	public int getCountRowsImoveis(){
		return imovelRuralService.countListarImovelByProprietario(donoEmpreendimento);
	}

	private void carregarDonoEmpreendimento() {
		if(!Util.isNullOuVazio(this.empreendimento) && !Util.isNullOuVazio(this.empreendimento.getIdeEmpreendimento())){
			this.donoEmpreendimento = this.empreendimento.getIdePessoa();
		}
		else{
			this.donoEmpreendimento = (Pessoa) getAtributoSession("REQUERENTE");
		}
		
	}

	private void carregarEmpreendimento() {
		this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");	
	}
	
	public Boolean podeAssociar(ImovelRural imovel) {
	
		if (Util.isNullOuVazio(empreendimento) || Util.isNullOuVazio(empreendimento.getImovelCollection())) {
			if (!Util.isNullOuVazio(imovel) && imovel.getStatusCadastro() != null && !imovel.isRegistroIncompleto() && !imovel.isCadastroIncompleto()) {
				return true;
			} else {
				return false;
			}
		}
		
		for (Imovel im : empreendimento.getImovelCollection()) {
			if (im.getIdeImovel().intValue() == imovel.getIdeImovelRural().intValue()) {
				return false;
			}
		}
		
		if (!Util.isNullOuVazio(imovel) && imovel.getStatusCadastro() != null && !imovel.isRegistroIncompleto() && !imovel.isCadastroIncompleto()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean ocultarBotoes(ImovelRural imovel){
		if (Util.isNullOuVazio(imovel) || Util.isNullOuVazio(this.empreendimento) || Util.isNullOuVazio(this.empreendimento.getImovelCollection())){
			return true;
		}
		for (Imovel im : this.empreendimento.getImovelCollection()) {
			if(im.getIdeImovel().intValue() ==  imovel.getIdeImovelRural().intValue()){
				return false;
			}
		}
		return true;
	}
	
	public void associarImovel() {
	
		try {
			if (Util.isNullOuVazio(this.empreendimento)) {
				carregarEmpreendimento();
			}
			
			imovelRuralService.associarImovel(this.imovelRuralSelecionado, empreendimento);
			
			this.empreendimento.getImovelCollection().add(this.imovelRuralSelecionado.getImovel());
			
			JsfUtil.addSuccessMessage("Inclusão Efetuada com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addSuccessMessage("Erro ao Associar Imóvel.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void associarImovelAoEmpreendimento(){
		try {
			if (Util.isNullOuVazio(this.empreendimento)) {
				carregarEmpreendimento();
			}
			
			this.empreendimento = imovelRuralService.associarEmpreendimentoAoImovel(this.imovelRuralSelecionado, empreendimento);
			
			this.empreendimento.getImovelCollection().add(this.imovelRuralSelecionado.getImovel());
			
			JsfUtil.addSuccessMessage("Inclusão Efetuada com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addSuccessMessage("Erro ao Associar Imóvel.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}
	
	public void desassociarImovel(){

		try {
			imovelRuralService.desassociarImovel(this.imovelSelecionado, this.empreendimento.getIdeEmpreendimento());
			this.empreendimento.getImovelCollection().remove(this.imovelSelecionado);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean exibeAlertaRequerenteAlterado(Imovel imv) {
		
		try {
			if(Util.isNullOuVazio(imv.getProprietario()) && imv.getImovelRural() != null) {
				imv.setProprietario(pessoaService.buscaProprietarioPorImovelRural(imv.getImovelRural().getIdeImovelRural()));
			}
			
			if(!Util.isNullOuVazio(imv.getProprietario()) && !Util.isNullOuVazio(empreendimento.getIdePessoa())) {
				if(!imv.getProprietario().getIdePessoa().equals(empreendimento.getIdePessoa().getIdePessoa())) {
					return true;
				}
			}
		
			return false;
		
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public LazyDataModel<ImovelRural> getListaImovelRuralModel() {
		return listaImovelRuralModel;
	}

	public void setListaImovelRuralModel(
			LazyDataModel<ImovelRural> listaImovelRuralModel) {
		this.listaImovelRuralModel = listaImovelRuralModel;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
		setarDesabilitarInclusaoOuRemocaoDeVinculo();
	}

	public boolean isDesabilitarInclusaoOuRemocaoDeVinculo() {
		return desabilitarInclusaoOuRemocaoDeVinculo;
	}
	
	public ImovelRural getImovelRuralSelecionado() {
		return imovelRuralSelecionado;
	}
	
	public void setImovelRuralSelecionado(ImovelRural imovelRuralSelecionado) {
		this.imovelRuralSelecionado = imovelRuralSelecionado;
	}

	public Imovel getImovelSelecionado() {
		return imovelSelecionado;
	}
	
	public void setImovelSelecionado(Imovel imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}
}
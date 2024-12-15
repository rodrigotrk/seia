package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ParticipacaoAcionariaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.VwRequerentePfExternoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.validators.CnpjValidator;

@Named("entidadeDeclaracaoTransporteController")
@ViewScoped
public class EntidadeDeclaracaoTransporteController {

	private Integer tipoPessoa = 0;
	private String nome;
	private String documento;
	private Pessoa requerente;
	private Boolean renderizaGrid;
	
	
	private boolean isRepresentanteInativado;
	private boolean isProcuradorInativado;
	private boolean isAcionarioInativado;
	
	
	private LazyDataModel<PessoaFisica> pessoasFisicaModel;
	private LazyDataModel<PessoaJuridica> pessoasJuridicaModel;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private VwRequerentePfExternoService vwRequerentePfExternoService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;

	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private ParticipacaoAcionariaService participacaoAcionarioService;
	
	@PostConstruct
	public void init() {
		tipoPessoa = 0;
		renderizaGrid =false;
	}
	
	public void limpar() {
		limparFiltro();
		renderizaGrid = false;
		requerente = null;
		pessoasFisicaModel = null;
		pessoasJuridicaModel = null;
	}

	private void limparFiltro() {
		nome = null;
		documento = null;
	}

	public void consultar() {
		String cnpj = this.documento;
		String razaoSocial = this.nome;
		
		if(CnpjValidator.validarCNPJ(cnpj) || !Util.isNullOuVazio(razaoSocial)) {

			renderizaGrid = true;
				
			try {
				 isRepresentanteInativado = isRepresentanteLegalInativado();
				 isProcuradorInativado = isProcuradorRepresentanteInativado();
				 isAcionarioInativado = isParticipanteAcionarioInativado();
				 
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			/** - Filtrar por usuário logado*/
			if(tipoPessoa == 0) {
				consultarPessoaFisica();
			}else if(tipoPessoa == 1) {
				consultarPessoaJuridica();
			}
		}
	}

	public void mudarTipoPessoa(ValueChangeEvent event) {
		limpar();
	}

	private void consultarPessoaFisica() {
		final boolean isUsuarioExterno = ContextoUtil.getContexto().isUsuarioExterno();
		
		final PessoaFisica lPessoa = new PessoaFisica();
		lPessoa.setNomPessoa(nome.trim());
		lPessoa.setNumCpf(documento.trim());
		
		try {
			lPessoa.setPessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		pessoasFisicaModel = new LazyDataModel<PessoaFisica>() {
				private static final long serialVersionUID = 3L;
				@Override
				public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<PessoaFisica> lista = null;
					Exception erro = null;
					try {
						
						if (isUsuarioExterno) {
							lista = (List<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExterno(lPessoa);
						} else {
							lista = (List<PessoaFisica>) pessoaFisicaService.filtrarPessoaRequerente(first,pageSize,lPessoa);
						}
					} catch (Exception e) {
						erro =e;
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
						pessoasFisicaModel.setRowCount(0);
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
					return lista;
				}
			};
			
			if(!isUsuarioExterno){
				pessoasFisicaModel.setRowCount(pessoaFisicaService.countFiltroPessoaFisicaSolicitante(lPessoa));
			}else{
				pessoasFisicaModel.setRowCount(vwRequerentePfExternoService.listarVwRequerentePfExterno(lPessoa).size());
			}
			
		} catch (Exception e) {
			pessoasFisicaModel.setRowCount(0);
		}
		
	}

	@SuppressWarnings("serial")
	private void consultarPessoaJuridica() {
		final boolean isUsuarioExterno = ContextoUtil.getContexto().isUsuarioExterno();
		
		final PessoaJuridica lPessoa = new PessoaJuridica();
		
		if(Util.isNullOuVazio(nome) && Util.isNullOuVazio(documento)) {
			JsfUtil.addErrorMessage("Informe pelo menos 1 filtro para realizar a consulta.");
			return;
		}
		
		lPessoa.setNomRazaoSocial(nome.trim());
		lPessoa.setNumCnpj(documento.trim());
		

		if(!Util.isNullOuVazio(isRepresentanteInativado)){
			lPessoa.setRepresentanteLegalInativado(isRepresentanteInativado);
		}
				
		Exception erro = null;
		try {
			pessoasJuridicaModel = new LazyDataModel<PessoaJuridica>() {
				@Override
				public List<PessoaJuridica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<PessoaJuridica> lista = null;
						try {
							if (isUsuarioExterno) {
								lista = pessoaJuridicaService.filtrarPJRequerenteExterno(lPessoa);
							} else {
								lista = pessoaJuridicaService.filtrarPessoaJuridica(first,pageSize,lPessoa);
							}
						} catch (Exception e) {
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
							pessoasJuridicaModel.setRowCount(0);
						}
					return lista;
				}
			};
			if(isUsuarioExterno){
				pessoasJuridicaModel.setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitanteExterno(lPessoa));
			}else{
				pessoasJuridicaModel.setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitante(lPessoa));
			}
			
			if(pessoasJuridicaModel.getRowCount() == 0) {
				JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_nao_encontrada"));
			}
		} catch (Exception e) {
			erro =e;
			pessoasJuridicaModel.setRowCount(0);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public boolean isRepresentanteLegalInativado(){
		
		int ideRequerente    = 0;
		int ideUsuarioLogado = 0;
		boolean isRepresenteLegalInativado = false;
		
		if(!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getIdePessoa())){
			ideRequerente  = requerente.getIdePessoa();
			
		} 
		if(!Util.isNullOuVazio(ContextoUtil.getContexto()) && 
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())&&
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica())){
			ideUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();				
		}

		
		if(!Util.isNullOuVazio(ideRequerente)    && (0 != ideRequerente) &&
		   !Util.isNullOuVazio(ideUsuarioLogado) && (0 != ideUsuarioLogado)){
			isRepresenteLegalInativado = representanteLegalService.isRepresentanteLegalInativado(ideUsuarioLogado, ideRequerente);
		}
	
		return isRepresenteLegalInativado;
	}
	

	public boolean isProcuradorRepresentanteInativado(){
		
		int ideRequerente    = 0;
		int ideUsuarioLogado = 0;
		boolean isProcuradorRepresentanteInativado = false;
		
		if(!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getIdePessoa())){
			ideRequerente  = requerente.getIdePessoa();
			
		} 
		if(!Util.isNullOuVazio(ContextoUtil.getContexto()) && 
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())&&
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica())){
			ideUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();				
		}

		
		if(!Util.isNullOuVazio(ideRequerente)    && (0 != ideRequerente) &&
		   !Util.isNullOuVazio(ideUsuarioLogado) && (0 != ideUsuarioLogado)){
			isProcuradorRepresentanteInativado = procuradorRepresentanteService.isProcuradorRepresentanteInativado(ideUsuarioLogado, ideRequerente);
		}
	
		return isProcuradorRepresentanteInativado;
	}

	public boolean isParticipanteAcionarioInativado() {
		
		int ideRequerente    = 0;
		int ideUsuarioLogado = 0;
		boolean isParticipanteAcionarioInativado = false;
		
		if(!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getIdePessoa())){
			ideRequerente  = requerente.getIdePessoa();
			
		} 
		if(!Util.isNullOuVazio(ContextoUtil.getContexto()) && 
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())&&
		   !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica())){
			ideUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();				
		}

		
		if(!Util.isNullOuVazio(ideRequerente)    && (0 != ideRequerente) &&
		   !Util.isNullOuVazio(ideUsuarioLogado) && (0 != ideUsuarioLogado)){
			isParticipanteAcionarioInativado = representanteLegalService.isRepresentanteLegalInativado(ideUsuarioLogado, ideRequerente);
		}
	
		return isParticipanteAcionarioInativado;
	}
	
	public boolean isListaPFNotEmpty(){
		return renderizaGrid && !Util.isNullOuVazio(pessoasFisicaModel) && pessoasFisicaModel.getRowCount() > 0;
	}
	
	public boolean isListaPJNotEmpty(){
		return renderizaGrid && !Util.isNullOuVazio(pessoasJuridicaModel) && pessoasJuridicaModel.getRowCount() > 0;
	}
	
	public int getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(int tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}


	public LazyDataModel<PessoaFisica> getPessoasFisicaModel() {
		return pessoasFisicaModel;
	}

	public void setPessoasFisicaModel(LazyDataModel<PessoaFisica> pessoasFisicaModel) {
		this.pessoasFisicaModel = pessoasFisicaModel;
	}

	public LazyDataModel<PessoaJuridica> getPessoasJuridicaModel() {
		return pessoasJuridicaModel;
	}

	public void setPessoasJuridicaModel(LazyDataModel<PessoaJuridica> pessoasJuridicaModel) {
		this.pessoasJuridicaModel = pessoasJuridicaModel;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Boolean getRenderizaGrid() {
		return renderizaGrid;
	}

	public void setRenderizaGrid(Boolean renderizaGrid) {
		this.renderizaGrid = renderizaGrid;
	}


	
}
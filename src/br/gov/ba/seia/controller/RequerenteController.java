package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.TipoPessoaDTO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.enumerator.TipoPessoaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.VwRequerentePfExternoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("requerenteController")
@ViewScoped
public class RequerenteController implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;

	@EJB
	private VwRequerentePfExternoService vwRequerentePfExternoService;
	
	
	private TipoPessoaDTO tipoPessoaDTO;
	private String nome;
	private String documento;
	private Pessoa requerente;
	private Boolean renderizaGrid;
	
	private DataTable dataTablePessoaFisica;
	
	private DataTable dataTablePessoaJuridica;
	
	private boolean isRepresentanteInativado;
	private MetodoUtil metodoExterno;
	
	
	private LazyDataModel<PessoaFisica> pessoasFisicaModel;
	private LazyDataModel<PessoaJuridica> pessoasJuridicaModel;

		
	@PostConstruct
	public void init(){
		tipoPessoaDTO = new TipoPessoaDTO();
		tipoPessoaDTO.setTipoPessoaEnum(TipoPessoaEnum.FISICA);
		tipoPessoaDTO.setUsuarioConvenio(pessoaFisicaService.isUsuarioConvenio());
	}
	
	public void load(MetodoUtil metodoExterno) {
		tipoPessoaDTO.setTipoPessoaEnum(TipoPessoaEnum.FISICA);
		limpar();
		this.metodoExterno = metodoExterno;
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

		renderizaGrid = true;
		this.dataTablePessoaFisica.setFirst(0);
		this.dataTablePessoaFisica.setPage(1);
		
		this.dataTablePessoaJuridica.setFirst(0);
		this.dataTablePessoaJuridica.setPage(1);
		
		try {
			 isRepresentanteInativado = isRepresentanteLegalInativado() ;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		// - Filtrar por usuário logado
		if(tipoPessoaDTO.getTipoPessoaEnum().equals(TipoPessoaEnum.FISICA)) {
			consultarPessoaFisica();
		}
		else if(tipoPessoaDTO.getTipoPessoaEnum().equals(TipoPessoaEnum.JURIDICA)) {
			consultarPessoaJuridica();
		}

	}

	public void selecionarRequerente(Object requerente) {
		try {
			Pessoa pessoaRequerente = new Pessoa();
			if(requerente instanceof PessoaFisica) {
				PessoaFisica pfRequerente = (PessoaFisica) requerente;
				pessoaRequerente.setIdePessoa(pfRequerente.getIdePessoaFisica());
				pessoaRequerente.setPessoaFisica(pfRequerente);
			}
			else if(requerente instanceof PessoaJuridica) {
				PessoaJuridica pjRequerente = (PessoaJuridica) requerente;
				pessoaRequerente.setIdePessoa(pjRequerente.getIdePessoaJuridica());
				pessoaRequerente.setPessoaJuridica(pjRequerente);
			}
			metodoExterno.executeMethod(pessoaRequerente);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
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
					
					try {
						
						if (isUsuarioExterno) {
							lista = (List<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExterno(lPessoa);
						} else {
							lista = (List<PessoaFisica>) pessoaFisicaService.filtrarPessoaRequerente(first,pageSize,lPessoa);
						}
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
						pessoasFisicaModel.setRowCount(0);
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
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
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
							pessoasFisicaModel.setRowCount(0);
						}
					return lista;
				}
			};
			if(isUsuarioExterno){
				lPessoa.setPessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
				pessoasJuridicaModel.setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitanteExterno(lPessoa));
			}else{
				pessoasJuridicaModel.setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitante(lPessoa));
			}
		} catch (Exception e) {
			erro =e;
			pessoasJuridicaModel.setRowCount(0);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public boolean isRepresentanteLegalInativado() throws Exception{
		
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
	

	public boolean isListaPFNotEmpty(){
		return renderizaGrid && !Util.isNullOuVazio(pessoasFisicaModel) && pessoasFisicaModel.getRowCount() > 0;
	}
	
	public boolean isListaPJNotEmpty(){
		return renderizaGrid && !Util.isNullOuVazio(pessoasJuridicaModel) && pessoasJuridicaModel.getRowCount() > 0;
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

	public DataTable getDataTablePessoaFisica() {
		return dataTablePessoaFisica;
	}

	public void setDataTablePessoaFisica(DataTable dataTablePessoaFisica) {
		this.dataTablePessoaFisica = dataTablePessoaFisica;
	}

	public DataTable getDataTablePessoaJuridica() {
		return dataTablePessoaJuridica;
	}

	public void setDataTablePessoaJuridica(DataTable dataTablePessoaJuridica) {
		this.dataTablePessoaJuridica = dataTablePessoaJuridica;
	}

	public Boolean getRenderizaGrid() {
		return renderizaGrid;
	}

	public void setRenderizaGrid(Boolean renderizaGrid) {
		this.renderizaGrid = renderizaGrid;
	}

	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}

	public TipoPessoaDTO getTipoPessoaDTO() {
		return tipoPessoaDTO;
	}

	public void setTipoPessoaDTO(TipoPessoaDTO tipoPessoaDTO) {
		this.tipoPessoaDTO = tipoPessoaDTO;
	}
	
}
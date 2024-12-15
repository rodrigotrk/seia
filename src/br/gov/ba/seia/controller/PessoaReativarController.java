package br.gov.ba.seia.controller;

import java.awt.Dialog;
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

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@ViewScoped
@Named("pessoaReativarController")

public class PessoaReativarController {
	
	@EJB
	private PessoaService pessoaService;
	
	private int tipoPessoaFisica;
	
	private String nomeRazao;
	private String cpfCnpj;
	private String nome ;
	private String razao;
	private String cpf;
	private String cnpj;
	private boolean renderFisica;
	private Pessoa pessoa ;
	private LazyDataModel<Pessoa> pessoaFisicaModel;
	private LazyDataModel<Pessoa> pessoaJuridicaModel;
	private PessoaFisica pessoaFisica;
	private PessoaJuridica pessoaJuridica;
	private int paginaAtual; 
	private int paginaFinal;
	private Dialog dlgPessoaReativar;
	private DataTable dataTablePessoaFisica;

	 private String message;
	 
	
	
	@PostConstruct
    public void init() {
		inicializarVariaveis();
		LimparTela();
		carregarLazyModelPessoa();
		
	}
		
	public void carregarLazyModelPessoa() {

		if (renderFisica) {

			pessoaFisicaModel = new LazyDataModel<Pessoa>() {
				private static final long serialVersionUID = -549249300009769836L;

				@Override
				public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,	Map<String, String> fields) {
					List<Pessoa> pessoas = null;
					
					try {
						setPageSize(pageSize);
						pessoas = pessoaService.listarPessoasPorDemanda(nome,cpf, first, pageSize);
					} catch (Exception e) {
						JsfUtil.addErrorMessage(e.getMessage());
					} 
					return pessoas;
				}
			};
			pessoaFisicaModel.setRowCount(getRowCountPessoaFisica());
			
		} else {
			
			pessoaJuridicaModel = new LazyDataModel<Pessoa>() {
			
				private static final long serialVersionUID = -7154472766822931312L;

				@Override
				public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,	Map<String, String> fields) {
					List<Pessoa> pessoas = null;
					try {
						setPageSize(pageSize);
						pessoas = pessoaService.listarPessoasJuridicaPorDemanda(razao, cnpj, first, pageSize);
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
						JsfUtil.addErrorMessage(e.getMessage());
					} 
					return pessoas;
				}
			};
			pessoaJuridicaModel.setRowCount(getRowCountPessoaJuridica());

		}
	}


	private int getRowCountPessoaFisica() {
		int totalRowCount = 0;
		Exception erro = null;
		try {
			totalRowCount = pessoaService.getRowsCountPessoaFisica(nome, cpf);
			
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return totalRowCount;
	}
		

	private int getRowCountPessoaJuridica() {
		int totalRowCount = 0;
		Exception erro = null;
		try {
			totalRowCount = pessoaService.getRowsCountPessoaJuridica(razao, cnpj);
			
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return totalRowCount;
	}

	
	
	public void trocaTipoPessoa(ValueChangeEvent event) {
		
		 Integer vlo = Integer.valueOf( event.getNewValue().toString());
		 
		 setNome("");
		 setRazao("");
		 setCpf("");
		 setCnpj("");
		 
		 limparLazyDataModel();
		 
		 if     (vlo == 0) {
			 renderFisica = true;
				setNomeRazao("Nome");
				setCpfCnpj("CPF");
		 } 
		 else if(vlo == 1) {
			 renderFisica = false;
			 setNomeRazao("Razão Social");
			 setCpfCnpj("CNPJ");
	     }
	}
	
	public void inicializarVariaveis(){
		
		paginaAtual = 0;
		
		setNomeRazao("Nome");
		setCpfCnpj("CPF");
		pessoa = new Pessoa();
		renderFisica = true;

	}
	
	@SuppressWarnings("serial")
	public void limparLazyDataModel(){
		pessoaFisicaModel = new LazyDataModel<Pessoa>() {
			
			@Override
			public List<Pessoa> load(int arg0, int arg1, String arg2, SortOrder arg3,
					Map<String, String> arg4) {
			
				return null;
			}
		};
		
		pessoaJuridicaModel = new LazyDataModel<Pessoa>() {
			
			@Override
			public List<Pessoa> load(int arg0, int arg1, String arg2, SortOrder arg3,
					Map<String, String> arg4) {
				return null;
			}
		};

	}
		
	public DataTable getDataTablePessoaFisica() {
		return dataTablePessoaFisica;
	}

	public void setDataTablePessoaFisica(DataTable dataTablePessoaFisica) {
		this.dataTablePessoaFisica = dataTablePessoaFisica;
	}


	public void LimparTela(){
		
		 setNome("");
		 setRazao("");
		 setCpf("");
		 setCnpj("");
		 
	}
	
	
	
	public void updateTable() {
		try {
			pessoa.setIndExcluido(false);
			pessoa.setDtcExclusao(null);
			
			pessoaService.atualizarPessoa(pessoa);
			
			JsfUtil.addSuccessMessage("Pessoa Reativada com Sucesso!");
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Dialog getDlgPessoaReativar() {
		return dlgPessoaReativar;
	}

	public void setDlgPessoaReativar(Dialog dlgPessoaReativar) {
		this.dlgPessoaReativar = dlgPessoaReativar;
	}

	public String getNomeRazao() {
		return nomeRazao;
	}

	public void setNomeRazao(String nomeRazao) {
		this.nomeRazao = nomeRazao;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public int getPaginaAtual() {
		return paginaAtual;
	}


	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}


	public int getPaginaFinal() {
		return paginaFinal;
	}


	public void setPaginaFinal(int paginaFinal) {
		this.paginaFinal = paginaFinal;
	}


	public int getTipoPessoaFisica() {
		return tipoPessoaFisica;
	}

	public void setTipoPessoaFisica(int tipoPessoaFisica) {
		this.tipoPessoaFisica = tipoPessoaFisica;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isRenderFisica() {
		return renderFisica;
	}
	
	public void setRenderFisica(boolean renderFisica) {
		this.renderFisica = renderFisica;
	}

	public LazyDataModel<Pessoa> getPessoaFisicaModel() {
		return pessoaFisicaModel;
	}

	public void setPessoaFisicaModel(LazyDataModel<Pessoa> pessoaFisicaModel) {
		this.pessoaFisicaModel = pessoaFisicaModel;
	}

	public LazyDataModel<Pessoa> getPessoaJuridicaModel() {
		return pessoaJuridicaModel;
	}

	public void setPessoaJuridicaModel(LazyDataModel<Pessoa> pessoaJuridicaModel) {
		this.pessoaJuridicaModel = pessoaJuridicaModel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}	
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }

}

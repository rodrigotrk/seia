package br.gov.ba.seia.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.DeclaracaoTransporteEntidadeTransportadora;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DeclaracaoTransporteService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.VwRequerentePfExternoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Named("entidadeTransportadoraController")
@ViewScoped
public class EntidadeTransportadoraController {

	private Integer tipoPessoa = 0;
	private String nome;
	private String documento;
	private String numeroProcessoLicenca; 
	
	private Pessoa pessoaEntidadeTransportadora;
	
	private DeclaracaoTransporteEntidadeTransportadora entidadeTransportadoraSelecionada;
	
	private DeclaracaoTransporte declaracaoTransporte;
	
	private boolean exibeInformacoes;
	
	private boolean permiteEditar;
	private boolean permiteEditarNumProcesso = true;
	private boolean permiteSalvar = false;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private VwRequerentePfExternoService vwRequerentePfExternoService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@Inject
	private DeclaracaoTransporteResiduoPerigosoController declaracaoTransporteController;
	
	@Inject
	private DeclaracaoTransporteService declaracaoTransporteService;

	@PostConstruct
	public void init() {
		tipoPessoa = 0;
	}
	
	public void limpar() {
		limparFiltro();
		pessoaEntidadeTransportadora = null;
		this.numeroProcessoLicenca = "";
		this.exibeInformacoes = false;
		this.tipoPessoa = 0;
		this.permiteEditarNumProcesso = true;
		this.permiteSalvar = false;
		
		entidadeTransportadoraSelecionada = new DeclaracaoTransporteEntidadeTransportadora();
	}

	private void limparFiltro() {
		nome = null;
		documento = null;
	}

	public void consultar() {
		if(tipoPessoa == 0) {
			consultarPessoaFisica();
		}else if(tipoPessoa == 1) {
			consultarPessoaJuridica();
		}
	}

	public void mudarTipoPessoa(ValueChangeEvent event) {
		limpar();
	}

	private void consultarPessoaFisica() {
		if(Util.isNullOuVazio(documento)) {
			JsfUtil.addErrorMessage("Informe o número do CPF.");
			return;
		}
		PessoaFisica lPessoa = new PessoaFisica();
		lPessoa.setNumCpf(documento.trim());
		
		Exception erro = null;
		
		exibeInformacoes = true;
		
		try {
			lPessoa = pessoaFisicaService.buscarPessoaFisicaByCPF(lPessoa);
			
			if(!Util.isNullOuVazio(lPessoa)) {
				if(!this.declaracaoTransporteService.existeTransportadora(new Pessoa(lPessoa.getPessoa().getIdePessoa(), lPessoa), declaracaoTransporte)) {
					if(!Util.isNull(lPessoa)) {
						this.pessoaEntidadeTransportadora = new Pessoa();
						this.pessoaEntidadeTransportadora.setPessoaFisica(lPessoa);
						this.pessoaEntidadeTransportadora.setIdePessoa(lPessoa.getPessoa().getIdePessoa());
						
						buscarEnderecoPessoa();
						
						permiteSalvar = true;
						
						Html.atualizar("form_dialog_transportadora:pnlBotao");
					}
					else{
						JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_nao_encontrada"));
						exibeInformacoes = false;
					}
				}else{
					JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_existente"));
					exibeInformacoes = false;
				}
			}else{
				JsfUtil.addWarnMessage("Pessoa Física não encontrada.");
				exibeInformacoes = false;
			}
		} catch (Exception e) {
			erro = e;
			exibeInformacoes = false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	@SuppressWarnings("serial")
	private void consultarPessoaJuridica() {
		if(Util.isNullOuVazio(documento)) {
			JsfUtil.addErrorMessage("Informe o número do CNPJ.");
			return;
		}
		
		PessoaJuridica lPessoa = new PessoaJuridica();
		lPessoa.setNumCnpj(documento.trim());
		exibeInformacoes = true;
		
		Exception erro = null;
		try {
			lPessoa = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(lPessoa);
			if(!Util.isNullOuVazio(lPessoa)) {
				if(!this.declaracaoTransporteService.existeTransportadora(new Pessoa(lPessoa.getPessoa().getIdePessoa(), lPessoa), declaracaoTransporte)) {
				
					if(!Util.isNull(lPessoa)) {
						this.pessoaEntidadeTransportadora = new Pessoa();
						this.pessoaEntidadeTransportadora.setPessoaJuridica(lPessoa);
						this.pessoaEntidadeTransportadora.setIdePessoa(lPessoa.getPessoa().getIdePessoa());
						
						buscarEnderecoPessoa();
						
						permiteSalvar = true;
						
						Html.atualizar("form_dialog_transportadora:pnlBotao");
					}
					else{
						JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_nao_encontrada"));
						Html.atualizar("form_dialog_transportadora:pnlDadosTransportadora");
						exibeInformacoes = false;
					}
				}else{
					JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_existente"));
					exibeInformacoes = false;
				}
			}else{
				JsfUtil.addWarnMessage(Util.getString("dtrp_mensagem_transportadora_nao_encontrada"));
				exibeInformacoes = false;
			}
		} catch (Exception e) {
			erro =e;
			exibeInformacoes = false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void buscarEnderecoPessoa() throws Exception {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPorPessoa(this.pessoaEntidadeTransportadora);
		
		if(!Util.isNull(enderecoPessoa)) {
			Endereco endereco = this.enderecoFacade.carregar(enderecoPessoa.getIdeEndereco().getIdeEndereco());
			
			if(!Util.isNull(endereco)) {
				this.pessoaEntidadeTransportadora.setEndereco(endereco);
			}
		}
		
		exibeInformacoes = true;
	}
	
	public void visualizarEntidadeTransportadora(DeclaracaoTransporteEntidadeTransportadora entidadeTransportadora) {
		Exception erro = null;
		
		try {
			this.entidadeTransportadoraSelecionada = entidadeTransportadora;
			
			if(!Util.isNull(entidadeTransportadora) && !Util.isNull(entidadeTransportadora.getPessoa().getIdePessoa())) {
				this.pessoaEntidadeTransportadora = new Pessoa();
				
				if(entidadeTransportadora.getPessoa().isPF()) {
					PessoaFisica pessoaFisica = pessoaFisicaService.buscarPessoaFisicaByCPF(entidadeTransportadora.getPessoa().getPessoaFisica());
					
					this.pessoaEntidadeTransportadora.setIdePessoa(pessoaFisica.getPessoa().getIdePessoa());
					this.pessoaEntidadeTransportadora.setPessoaFisica(pessoaFisica);
					
					this.tipoPessoa = 0;
					this.documento = this.pessoaEntidadeTransportadora.getPessoaFisica().getNumCpf();
				}else{
					PessoaJuridica pessoaJuridica = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(entidadeTransportadora.getPessoa().getPessoaJuridica());
					
					this.pessoaEntidadeTransportadora.setIdePessoa(pessoaJuridica.getPessoa().getIdePessoa());
					this.pessoaEntidadeTransportadora.setPessoaJuridica(pessoaJuridica);
					
					this.tipoPessoa = 1;
					this.documento = this.pessoaEntidadeTransportadora.getPessoaJuridica().getNumCnpj();
				}

				this.permiteEditar = false;
				this.permiteEditarNumProcesso = false;
				this.permiteSalvar = false;
				this.exibeInformacoes = true;
				this.buscarEnderecoPessoa();
				this.numeroProcessoLicenca = entidadeTransportadora.getNumProcessoLicenciamento();
				
				Html.atualizar("form_dialog_transportadora:pnlDadosPessoa");
				Html.atualizar("form_dialog_transportadora:pnlDadosTransportadora");
			}
		} catch (Exception e) {
			erro =e;
			exibeInformacoes = false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void editarEntidadeTransportadora(DeclaracaoTransporteEntidadeTransportadora entidadeTransportadora) {
		Exception erro = null;
		
		try {
			this.entidadeTransportadoraSelecionada = entidadeTransportadora;
			
			if(!Util.isNull(entidadeTransportadora) && !Util.isNull(entidadeTransportadora.getPessoa().getIdePessoa())) {
				this.pessoaEntidadeTransportadora = new Pessoa();
				
				if(entidadeTransportadora.getPessoa().isPF()) {
					PessoaFisica pessoaFisica = pessoaFisicaService.buscarPessoaFisicaByCPF(entidadeTransportadora.getPessoa().getPessoaFisica());
					
					this.pessoaEntidadeTransportadora.setIdePessoa(pessoaFisica.getPessoa().getIdePessoa());
					this.pessoaEntidadeTransportadora.setPessoaFisica(pessoaFisica);
					
					this.tipoPessoa = 0;
					
					this.documento = this.pessoaEntidadeTransportadora.getPessoaFisica().getNumCpf();
				}else{
					PessoaJuridica pessoaJuridica = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(entidadeTransportadora.getPessoa().getPessoaJuridica());
					
					this.pessoaEntidadeTransportadora.setIdePessoa(pessoaJuridica.getPessoa().getIdePessoa());
					this.pessoaEntidadeTransportadora.setPessoaJuridica(pessoaJuridica);
					
					this.tipoPessoa = 1;
					
					this.documento = this.pessoaEntidadeTransportadora.getPessoaJuridica().getNumCnpj();
				}

				this.permiteEditar = false;
				this.exibeInformacoes = true;
				this.permiteEditarNumProcesso = true;
				this.permiteSalvar = true;
				this.buscarEnderecoPessoa();
				this.numeroProcessoLicenca = entidadeTransportadora.getNumProcessoLicenciamento();
				
				Html.atualizar("form_dialog_transportadora:pnlDadosTransportadora");
				Html.atualizar("form_dialog_transportadora:pnlBotao");
			}
		} catch (Exception e) {
			erro =e;
			exibeInformacoes = false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
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

	public Pessoa getEntidadeTransportadora() {
		return pessoaEntidadeTransportadora;
	}

	public void setEntidadeTransportadora(Pessoa entidadeTransportadora) {
		this.pessoaEntidadeTransportadora = entidadeTransportadora;
	}

	public String getNumeroProcessoLicenca() {
		return numeroProcessoLicenca;
	}

	public void setNumeroProcessoLicenca(String numeroProcessoLicenca) {
		this.numeroProcessoLicenca = numeroProcessoLicenca;
	}

	public boolean isExibeInformacoes() {
		return exibeInformacoes;
	}

	public void setExibeInformacoes(boolean exibeInformacoes) {
		this.exibeInformacoes = exibeInformacoes;
	}

	public boolean isPermiteEditar() {
		return permiteEditar;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	public DeclaracaoTransporteEntidadeTransportadora getEntidadeTransportadoraSelecionada() {
		return entidadeTransportadoraSelecionada;
	}

	public void setEntidadeTransportadoraSelecionada(
			DeclaracaoTransporteEntidadeTransportadora entidadeTransportadoraSelecionada) {
		this.entidadeTransportadoraSelecionada = entidadeTransportadoraSelecionada;
	}

	public boolean isPermiteEditarNumProcesso() {
		return permiteEditarNumProcesso;
	}

	public void setPermiteEditarNumProcesso(boolean permiteEditarNumProcesso) {
		this.permiteEditarNumProcesso = permiteEditarNumProcesso;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public boolean isPermiteSalvar() {
		return permiteSalvar;
	}

	public void setPermiteSalvar(boolean permiteSalvar) {
		this.permiteSalvar = permiteSalvar;
	}

}
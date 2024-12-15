package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.ContratoConvenioCefirMunicipio;
import br.gov.ba.seia.entity.GestorFinanceiroCefir;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.enumerator.GestorFinenceiroCefirEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ContratoConvenioCefirServiceImpl;
import br.gov.ba.seia.service.GestorFinanceiroCefirService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("contratoConvenioCefirController")
@ViewScoped
public class ContratoConvenioCefirController {

	@EJB
	private ContratoConvenioCefirServiceImpl contratoConvenioService;

	@EJB
	private ContratoConvenioCefirServiceImpl contratoConvenioCerfirService;

	@EJB
	private GestorFinanceiroCefirService gestorFinanceiroService;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private PessoaJuridicaService pessoaJuridicaService;

	private ContratoConvenioCefir contratoConvenioCefirPesquisa;

	private ContratoConvenioCefir contratoConvenioCefir;

	private List<GestorFinanceiroCefir> listGestorFinanceiro;

	private List<ContratoConvenioCefir> listContratoConvenio;

	private DualListModel<Municipio> dualListMunicipios;

	private List<Municipio> listMunicipios;



	@PostConstruct
	public void init() {
		try {

			setContratoConvenioCefirPesquisa(new ContratoConvenioCefir());
			getContratoConvenioCefirPesquisa().setIdeGestorFinanceiroCefir(new GestorFinanceiroCefir());

			consultarContratoConvenio();
			if(Util.isNullOuVazio(listGestorFinanceiro)) {
				listGestorFinanceiro = gestorFinanceiroService.listGestorFinanceiro();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void salvarOuAtualizarContratoConvenio() {
		try {
			boolean insert = false;
			if(Util.isNull(getContratoConvenioCefir().getIdeContratoConvenioCefir())) {
				insert = true;
			}

			if(getContratoConvenioCefir().isIndProjetoBndes()) {
				adicionarMunicipios();
			} else {
				getContratoConvenioCefir().setIdePessoaJuridica(null);
				getContratoConvenioCefir().setContratoConvenioCefirMunicipioCollection(null);
			}

			contratoConvenioCerfirService.salvarOuAtualizar(getContratoConvenioCefir());

			RequestContext.getCurrentInstance().execute("dlgCadContratoConvenio.hide()");
			if(insert) {
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
			}else{
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			}
			init();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void adicionarMunicipios() {
		getContratoConvenioCefir().setContratoConvenioCefirMunicipioCollection(new ArrayList<ContratoConvenioCefirMunicipio>());

		for (Municipio municipio : dualListMunicipios.getTarget()) {
			ContratoConvenioCefirMunicipio contratoConvenioCefirMunicipio = new ContratoConvenioCefirMunicipio(getContratoConvenioCefir(), municipio);
			getContratoConvenioCefir().getContratoConvenioCefirMunicipioCollection().add(contratoConvenioCefirMunicipio);
		}
	}

	/**
	 * Método responsável por carregar o dualList de municípios
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void carregarDualListMunicipios() {
		try {
			if(Util.isNullOuVazio(listMunicipios)) {
				listMunicipios = municipioService.filtrarListaMunicipiosDaBahia();
			}
			List<Municipio> listaMunicipiosSource = new ArrayList<Municipio>(listMunicipios);

			if(!Util.isNullOuVazio(contratoConvenioCefir) && !Util.isNullOuVazio(contratoConvenioCefir.getContratoConvenioCefirMunicipioCollection())) {
				if(!listaMunicipiosSource.isEmpty()) {
					List<Municipio> listaMunicipiosTarget = new ArrayList<Municipio>();
					for (ContratoConvenioCefirMunicipio contratoConvenioCefirMunicipio : contratoConvenioCefir.getContratoConvenioCefirMunicipioCollection()) {
						listaMunicipiosTarget.add(contratoConvenioCefirMunicipio.getIdeMunicipio());
						listaMunicipiosSource.remove(contratoConvenioCefirMunicipio.getIdeMunicipio());
					}

					dualListMunicipios = Util.castListToDualListModel(listaMunicipiosSource, listaMunicipiosTarget);
				}
			} else {
				dualListMunicipios = new DualListModel<Municipio>(listaMunicipiosSource, new ArrayList<Municipio>());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar lista de municípios");
		}
	}

	/**
	 * Método responsável por renderizar o checkbox Projeto CAR/BNDES/INEMA na tela de cadastro de contrato convênio
	 * @return boolean
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public boolean getRenderedCheckProjetoBndes() {
		return (!Util.isNullOuVazio(contratoConvenioCefir) 
				&& !Util.isNullOuVazio(contratoConvenioCefir.getIdeGestorFinanceiroCefir()) 
				&& !Util.isNullOuVazio(contratoConvenioCefir.getIdeGestorFinanceiroCefir().getIdeGestorFinanceiroCefir()) 
				&&  contratoConvenioCefir.getIdeGestorFinanceiroCefir().getIdeGestorFinanceiroCefir().equals(GestorFinenceiroCefirEnum.INEMA.getIdeGestorFinanceiroCefir()));
	}

	/**
	 * Método responsável por pesquisar pessoa jurídica na tela de cadastro de contrato convênio
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 15/10/2015
	*/
	public void carregarPessoaJuridica() {
		try {
			if(!Util.isNull(contratoConvenioCefir) && !Util.isNull(contratoConvenioCefir.getIdePessoaJuridica()) && !contratoConvenioCefir.getIdePessoaJuridica().getNumCnpj().isEmpty()) {
				contratoConvenioCefir.setIdePessoaJuridica(pessoaJuridicaService.filtrarPessoaFisicaByCnpj(contratoConvenioCefir.getIdePessoaJuridica()));
				if(Util.isNull(contratoConvenioCefir.getIdePessoaJuridica())) {
					contratoConvenioCefir.setIdePessoaJuridica(new PessoaJuridica());
					JsfUtil.addWarnMessage("O CNPJ não é válido");
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao pesquisar pessoa jurídica");
		}
	}

	/**
	 * Método responsável por limpar os dados da pessoa juridica na tela de cadastro de contrato convênio
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void limparPessoaJuridica() {
		String cnpj = contratoConvenioCefir.getIdePessoaJuridica().getNumCnpj();
		contratoConvenioCefir.setIdePessoaJuridica(new PessoaJuridica());
		contratoConvenioCefir.getIdePessoaJuridica().setNumCnpj(cnpj);
	}

	public void excluirContratoConvenio() {
		try {
			setContratoConvenioCefir(contratoConvenioService.carregarContratoConvenio(getContratoConvenioCefir()));
			contratoConvenioCerfirService.excluir(getContratoConvenioCefir());
			init();
			JsfUtil.addSuccessMessage(Util.getString("msg_generica_exclusao_efetuada"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Método responsável por preparar o objeto contratoConvenioCefir para cadastro de um noco contrato convênio
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 15/10/2015
	*/
	public void incluir() {
		setContratoConvenioCefir(new ContratoConvenioCefir());
		getContratoConvenioCefir().setIdeGestorFinanceiroCefir(new GestorFinanceiroCefir());
		getContratoConvenioCefir().setIdePessoaJuridica(new PessoaJuridica());
		carregarDualListMunicipios();
	}

	public void editar() {
		try {
			setContratoConvenioCefir(contratoConvenioService.carregarContratoConvenio(getContratoConvenioCefir()));
			carregarDualListMunicipios();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar contrato convênio para edição.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void limparCampos() {
		getContratoConvenioCefir().setNomContratoConvenioCefir(null);
		getContratoConvenioCefir().setNumContratoConvenioCefir(null);
		getContratoConvenioCefir().setIndProjetoBndes(false);
		getContratoConvenioCefir().setIdeGestorFinanceiroCefir(new GestorFinanceiroCefir());
		getContratoConvenioCefir().setIdePessoaJuridica(new PessoaJuridica());
		getContratoConvenioCefir().setContratoConvenioCefirMunicipioCollection(new ArrayList<ContratoConvenioCefirMunicipio>());
		carregarDualListMunicipios();
	}

	public void consultarContratoConvenio() {
		try {
			listContratoConvenio = contratoConvenioService.filtrarContratoConvenio(getContratoConvenioCefirPesquisa());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Método responsável por limpar os campos referente ao projeto BNDES ao trocar o gestor financeiro na tela
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 04/11/2015
	*/
	public void changeGestorFinanceiro() {
		contratoConvenioCefir.setIndProjetoBndes(false);
		contratoConvenioCefir.setIdePessoaJuridica(new PessoaJuridica());
		contratoConvenioCefir.setContratoConvenioCefirMunicipioCollection(new ArrayList<ContratoConvenioCefirMunicipio>());
		carregarDualListMunicipios();
	}

	/**
	 * Método responsável por limpar os campos referente ao projeto BNDES ao trocar o alterar o checkbox Projeto CAR/BNDES/INEMA
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 04/10/2015
	*/
	public void changeIndProjetoBndes() {
		contratoConvenioCefir.setIdePessoaJuridica(new PessoaJuridica());
		contratoConvenioCefir.setContratoConvenioCefirMunicipioCollection(new ArrayList<ContratoConvenioCefirMunicipio>());
		carregarDualListMunicipios();
	}

	public ContratoConvenioCefir getContratoConvenioCefir() {
		return contratoConvenioCefir;
	}

	public void setContratoConvenioCefir(ContratoConvenioCefir contratoConvenioCefir) {
		this.contratoConvenioCefir = contratoConvenioCefir;
	}

	public List<ContratoConvenioCefir> getListContratoConvenio() {
		return listContratoConvenio;
	}

	public List<GestorFinanceiroCefir> getListGestorFinanceiro() {
		return listGestorFinanceiro;
	}

	public DualListModel<Municipio> getDualListMunicipios() {
		return dualListMunicipios;
	}

	public void setDualListMunicipios(DualListModel<Municipio> dualListMunicipios) {
		this.dualListMunicipios = dualListMunicipios;
	}


	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public ContratoConvenioCefir getContratoConvenioCefirPesquisa() {
		return contratoConvenioCefirPesquisa;
	}

	public void setContratoConvenioCefirPesquisa(ContratoConvenioCefir contratoConvenioCefirPesquisa) {
		this.contratoConvenioCefirPesquisa = contratoConvenioCefirPesquisa;
	}

}


package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private List<ImovelRural> listaImoveisProprietario;
    private List<Imovel> listaImoveisVinculados;

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

    private static final Integer[] STATUS_DESABILITAR = {
        StatusRequerimentoEnum.VALIDADO.getStatus(),
        StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus(),
        StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus(),
        StatusRequerimentoEnum.FORMADO.getStatus(),
        StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus()
    };
    
    @PostConstruct
    public void init() {
        carregarEmpreendimento();
        carregarDonoEmpreendimento();
        setarDesabilitarInclusaoOuRemocaoDeVinculo();
        if (!Util.isNullOuVazio(donoEmpreendimento)) {
            carregarImoveis();
        }
    }
    
    public void carregarImoveis() {
    	if(!Util.isNullOuVazio(empreendimento)) {
    		listaImoveisVinculados = (List<Imovel>) imovelRuralService.carregarImoveisByEmpreendimento(empreendimento);
    	}
    	
    	listaImoveisProprietario = imovelRuralService.listarImovelRuralByProprietarioEmpreendimento(donoEmpreendimento);
    }

    public void setarDesabilitarInclusaoOuRemocaoDeVinculo() {
        try {
            Requerimento req = requerimentoUnicoService.recuperarUltimoRequerimento(empreendimento);
            if (req != null) {
                Integer ideDoStatusAtualDoRequerimento = requerimentoUnicoService.retornaTramitacaoRequerimentoUnico(req.getIdeRequerimento());
                desabilitarInclusaoOuRemocaoDeVinculo = validarInclusaoOuRemocaoDeVinculo(Arrays.asList(STATUS_DESABILITAR), ideDoStatusAtualDoRequerimento);
            } else {
                // Para o caso de não haver nenhum requerimento
                desabilitarInclusaoOuRemocaoDeVinculo = validarInclusaoOuRemocaoDeVinculo(null, null);
            }
        } catch (Exception e) {
            Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
            desabilitarInclusaoOuRemocaoDeVinculo = true;
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }

    private boolean validarInclusaoOuRemocaoDeVinculo(List<Integer> desabilitarNosStatus, Integer ideStatusRequerimento) {
        return desabilitarNosStatus != null && ideStatusRequerimento != null && desabilitarNosStatus.contains(ideStatusRequerimento);
    }


    public List<ImovelRural> populateListImovelRural(int first, int pageSize) {
        try {
            return imovelRuralService.listarImovelRuralByProprietario(donoEmpreendimento, first, pageSize);
        } catch (Exception e) {
            Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
            return new ArrayList<ImovelRural>();
        }
    }

    public void carregarImovelComEndereco() {
        if (!Util.isNullOuVazio(this.imovelSelecionado) && !Util.isNullOuVazio(this.imovelSelecionado.getIdeEndereco())) {
            this.imovelSelecionado.setIdeEndereco(enderecoService.carregar(this.imovelSelecionado.getIdeEndereco().getIdeEndereco()));
        }
    }

    public int getCountRowsImoveis() {
        return imovelRuralService.countListarImovelByProprietario(donoEmpreendimento);
    }

    private void carregarDonoEmpreendimento() {
        if (isEmpreendimentoValido()) {
            this.donoEmpreendimento = this.empreendimento.getIdePessoa();
        } else {
            this.donoEmpreendimento = (Pessoa) getAtributoSession("REQUERENTE");
        }
    }

    private void carregarEmpreendimento() {
        this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
    }

    private boolean isEmpreendimentoValido() {
        return !Util.isNullOuVazio(this.empreendimento) && !Util.isNullOuVazio(this.empreendimento.getIdeEmpreendimento());
    }

    public Boolean podeAssociar(ImovelRural imovel) {
        if (!isEmpreendimentoValido() || Util.isNullOuVazio(listaImoveisVinculados)) {
            return isImovelValido(imovel);
        }

        for (Imovel im : listaImoveisVinculados) {
            if (im.getIdeImovel().intValue() == imovel.getIdeImovelRural().intValue()) {
                return false;
            }
        }

        return isImovelValido(imovel);
    }

    private boolean isImovelValido(ImovelRural imovel) {
        return !Util.isNullOuVazio(imovel) && imovel.getStatusCadastro() != null && !imovel.isRegistroIncompleto() && !imovel.isCadastroIncompleto();
    }

    public Boolean ocultarBotoes(ImovelRural imovel) {
        if (Util.isNullOuVazio(imovel) || !isEmpreendimentoValido() || Util.isNullOuVazio(this.listaImoveisVinculados)) {
            return true;
        }
        for (Imovel im : this.listaImoveisVinculados) {
            if (im.getIdeImovel().intValue() == imovel.getIdeImovelRural().intValue()) {
                return false;
            }
        }
        return true;
    }

    public void associarImovel() {
        associarImovelAoEmpreendimento();
    }

    public void associarImovelAoEmpreendimento() {
        try {
            if (!isEmpreendimentoValido()) {
                carregarEmpreendimento();
            }

            this.empreendimento = imovelRuralService.associarEmpreendimentoAoImovel(this.imovelRuralSelecionado, empreendimento);
            this.listaImoveisVinculados.add(this.imovelRuralSelecionado.getImovel());

            JsfUtil.addSuccessMessage("Inclusão Efetuada com Sucesso.");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Erro ao Associar Imóvel.");
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }

    public void desassociarImovel() {
        try {
            imovelRuralService.desassociarImovel(this.imovelSelecionado, this.empreendimento.getIdeEmpreendimento());
            this.listaImoveisVinculados.remove(this.imovelSelecionado);
        } catch (Exception e) {
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }

    public boolean exibeAlertaRequerenteAlterado(Imovel imv) {
        try {
            if (Util.isNullOuVazio(imv.getProprietario()) && imv.getImovelRural() != null) {
                imv.setProprietario(pessoaService.buscaProprietarioPorImovelRural(imv.getImovelRural().getIdeImovelRural()));
            }

            if (!Util.isNullOuVazio(imv.getProprietario()) && !Util.isNullOuVazio(empreendimento.getIdePessoa())) {
                return !imv.getProprietario().getIdePessoa().equals(empreendimento.getIdePessoa().getIdePessoa());
            }

            return false;
        } catch (Exception e) {
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }
    
    public Empreendimento getEmpreendimento() {
        return empreendimento;
    }

    public void setEmpreendimento(Empreendimento empreendimento) {
        this.empreendimento = empreendimento;
        setarDesabilitarInclusaoOuRemocaoDeVinculo();
    }
    
    public List<ImovelRural> getListaImoveisProprietario() {
		return listaImoveisProprietario;
	}

	public void setListaImoveisProprietario(List<ImovelRural> listaImoveisProprietario) {
		this.listaImoveisProprietario = listaImoveisProprietario;
	}

	public List<Imovel> getListaImoveisVinculados() {
		return listaImoveisVinculados;
	}

	public void setListaImoveisVinculados(List<Imovel> listaImoveisVinculados) {
		this.listaImoveisVinculados = listaImoveisVinculados;
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
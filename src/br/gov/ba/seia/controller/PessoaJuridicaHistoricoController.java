package br.gov.ba.seia.controller;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaFisicaHistoricoDAOImpl;
import br.gov.ba.seia.dao.PessoaJuridicaHistoricoDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "pessoaJuridicaHistoricoController")
@ViewScoped
public class PessoaJuridicaHistoricoController {

	@PersistenceContext(unitName = "seiapu")
    private EntityManager entityManager;
  
    private List<PessoaJuridicaHistorico> historicoModificacao;
    private int pageSize = 20; 
    private int first = 0; 
	private Date dataInicio;
    private Date dataFim;
    
	@Inject
	PessoaJuridicaHistoricoDAOImpl pessoaJuridicaHistoricoDAOImpl;
	
	@EJB
	PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	
	public void pesquisar(PessoaJuridica pessoaJuridica) {
        try {
        	
        	historicoModificacao = pessoaJuridicaHistoricoDAOImpl.listarHistoricoPessoaPorIdePessoa(pessoaJuridica, dataInicio, dataFim);
        	
        } catch (Exception e) {
        	JsfUtil.addErrorMessage("Erro ao carregar histórico. Por favor, entre em contato com o suporte.");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }
	
	public StreamedContent imprimirHistorico(PessoaJuridica pessoaJuridica, Date dataInicioSet, Date dataFimSet) throws Exception {
		pessoaJuridica = pessoaJuridicaService.filtrarPessoaJuridicaByCnpj(pessoaJuridica);
		return pessoaJuridicaHistoricoService.imprimirHistorico(pessoaJuridica.getIdePessoaJuridica(), dataInicioSet, dataFimSet);
		
	}
	
	public void limparCalendario() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
		setDataInicio(calendar.getTime());
		setDataFim(new Date());
		
		historicoModificacao.removeAll(historicoModificacao);
		
	}		
	

    public List<PessoaJuridicaHistorico> getHistoricoModificacao() {
        return historicoModificacao;
    }

    public void setHistoricoModificacao(List<PessoaJuridicaHistorico> historicoModificacao) {
        this.historicoModificacao = historicoModificacao;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }
    
    public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}

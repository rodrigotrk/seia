package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.dao.CerhLocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.dao.CerhPerguntaDadosGeraisDAOImpl;
import br.gov.ba.seia.dao.CerhProcessoDAOImpl;
import br.gov.ba.seia.dao.CerhProcessoSuspensaoDAOImpl;
import br.gov.ba.seia.dao.CerhRespostaDadosGeraisDAOImpl;
import br.gov.ba.seia.dao.CerhTipoUsoDAOImpl;
import br.gov.ba.seia.dao.ContratoConvenioDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.TipoUsoRecursoHidricoDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhRespostaDadosGerais;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhService {
	
	@EJB
	private CerhDAOImpl cerhDAOImpl;
	
	@EJB
	private ContratoConvenioDAOImpl contratoConvenioDAOImpl;
	
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;
	
	@EJB
	private CerhRespostaDadosGeraisDAOImpl cerhRespostaDadosGeraisDAOImpl;
	
	@EJB
	private CerhPerguntaDadosGeraisDAOImpl cerhPerguntasDadosGeraisDAOImpl;
	
	@EJB
	private CerhProcessoDAOImpl cerhProcessoListagemDAOImpl;
	
	@EJB
	private CerhTipoUsoDAOImpl cerhTipoUsoDAOImpl;
	
	@EJB
	private TipoUsoRecursoHidricoDAOImpl tipoUsoRecursoHidricoDAOImpl;
	
	@EJB
	private CerhLocalizacaoGeograficaDAOImpl cerhLocalizacaoGeograficaDAOImpl;
	
	@EJB
	private CerhProcessoSuspensaoDAOImpl cerhProcessoSuspensaoDAOImpl;
	
	
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarParaHistorico(Cerh c){
		try {
			Cerh cerh = cerhDAOImpl.buscar(c);

			cerh.setIdeContratoConvenio(contratoConvenioDAOImpl.buscar(cerh.getIdeContratoConvenio()));
			cerh.setIdePessoaFisicaCadastro(pessoaFisicaDAOImpl.buscar(cerh.getIdePessoaFisicaCadastro()));
			cerh.setCerhProcessoCollection(cerhProcessoListagemDAOImpl.listar(Restrictions.eq("ideCerh.ideCerh", cerh.getId())));

			for (CerhProcesso cerhProcesso : cerh.getCerhProcessoCollection()) {
				cerhProcesso.setCerhProcessoSuspensaoCollection(cerhProcessoSuspensaoDAOImpl.listar(Restrictions.eq("ideCerhProcesso.ideCerhProcesso", cerhProcesso.getId()), Order.asc("ideCerhProcessoSuspensao")));
			}
			
			cerh.setCerhRespostaDadosGeraisCollection(cerhRespostaDadosGeraisDAOImpl.listar(Restrictions.eq("ideCerh.ideCerh", cerh.getId()), Order.asc("ideCerhPerguntaDadosGerais")));
			
			for (CerhRespostaDadosGerais crdg : cerh.getCerhRespostaDadosGeraisCollection()) {
				crdg.setIdeCerhPerguntaDadosGerais(cerhPerguntasDadosGeraisDAOImpl.buscar(crdg.getIdeCerhPerguntaDadosGerais()));
			}
			
			return cerh;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getNumeroCerhAnterior(Cerh cerh) {
		try{
			Cerh c ;
			
			if(cerh.getIdeCerhPai() != null){
				c = cerh.getIdeCerhPai();
			}else{
				c = cerh;
			}
			
			return cerhDAOImpl.getUltimoNumeroCadastro(c);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}

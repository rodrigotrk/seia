package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhCaptacaoCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.dao.CerhPerguntaDadosGeraisDAOImpl;
import br.gov.ba.seia.dao.CerhProcessoDAOImpl;
import br.gov.ba.seia.dao.CerhProcessoSuspensaoDAOImpl;
import br.gov.ba.seia.dao.CerhRespostaDadosGeraisDAOImpl;
import br.gov.ba.seia.dao.CerhSituacaoRegularizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhTipoAtoDispensaDAOImpl;
import br.gov.ba.seia.dao.CerhTipoAutorizacaoOutorgadoDAOImpl;
import br.gov.ba.seia.dao.ContratoConvenioDAOImpl;
import br.gov.ba.seia.dao.EmpreendimentoDAOImpl;
import br.gov.ba.seia.dao.EnderecoDAOImpl;
import br.gov.ba.seia.dao.LocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.dao.PessoaJuridicaDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ProcessoDAOImpl;
import br.gov.ba.seia.dao.TipoUsoRecursoHidricoDAOImpl;
import br.gov.ba.seia.dao.TipologiaDAOImpl;
import br.gov.ba.seia.dto.CerhAbaDadoGeraisDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhPerguntaDadosGerais;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhRespostaDadosGerais;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;
import br.gov.ba.seia.entity.CerhTipoAtoDispensa;
import br.gov.ba.seia.entity.CerhTipoAutorizacaoOutorgado;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhDadosGeraisServiceFacade extends CerhAbasFacade {

	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	
	@Inject
	private PessoaJuridicaDAOImpl pessoaJuridicaDAOImpl;
	
	@Inject
	private EmpreendimentoDAOImpl empreendimentoDAOImpl;
	
	@EJB
	private CerhDAOImpl cerhDAOImpl;
	
	@EJB
	private CerhProcessoDAOImpl cerhProcessoListagemDAOImpl;
	
	@EJB
	private CerhRespostaDadosGeraisDAOImpl cerhRespostaDadosGeraisDAOImpl;
	
	@EJB
	private CerhSituacaoRegularizacaoDAOImpl cerhSituacaoRegularizacaoDAOImpl;
	
	@EJB
	private CerhPerguntaDadosGeraisDAOImpl cerhPerguntaDadosGeraisDAOImpl;
	
	@EJB
	private ProcessoDAOImpl processoDAOImpl;
	
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	
	@EJB
	private TipologiaDAOImpl tipologiaDAOImpl;
	
	@EJB
	private LocalizacaoGeograficaDAOImpl localizacaoGeograficaDAOImpl;
	
	@EJB
	private TipoUsoRecursoHidricoDAOImpl tipoUsoRecursoHidricoDAOImpl;
	
	@EJB
	private EnderecoDAOImpl enderecoDAOImpl;
	
	@EJB
	private CerhTipoAtoDispensaDAOImpl cerhTipoAtoDispensaDAOImpl;
	
	@EJB
	private CerhTipoAutorizacaoOutorgadoDAOImpl cerhTipoAutorizacaoOutorgadoDAOImpl;
	
	@EJB
	private ContratoConvenioDAOImpl contratoConvenioDAOImpl;
	
	@EJB
	private CerhProcessoSuspensaoDAOImpl cerhProcessoSuspensaoDAOImpl; 
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private CerhCaptacaoCaracterizacaoDAOImpl cerhCaptacaoCaracterizacaoDAO;
	
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Cerh cerh)  {
		cerhDAOImpl.salvar(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComoHistorico(Cerh cerh)  {
		cerh.setCerhTipoUsoCollection(null);
		cerh.setCerhProcessoCollection(null);
		cerh.setCerhRespostaDadosGeraisCollection(null);
		cerhDAOImpl.salvar(cerh);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerenciarUpdates(Cerh cerh) throws Exception {
		if(!Util.isNull(cerh.getIdeCerh())) {
			gerenciarUpdateCerhTipoUso(cerh);
			gerenciarUpdateCerhProcessoSuspensao(cerh);
			gerenciarUpdateCerhProcesso(cerh);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerenciarUpdateCerhTipoUso(Cerh cerh)  {
		super.cerhTipoUsoDAO.gerenciarCerhTipoUsoRemovido(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhTipUso(CerhTipoUso cerhTipoUso)  {
		super.cerhTipoUsoDAO.salvar(cerhTipoUso);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void verificarSeExisteCaracterizacaoParaRemocao(CerhAbaDadoGeraisDTO dto) throws Exception {
		if(!Util.isNull(dto.getCerh()) && !Util.isNull(dto.getCerh().getIdeCerh())){
			Collection<CerhTipoUso> cerhTipoUsoRemocao = super.cerhTipoUsoDAO.listarCerhTipoUsoRemocao(dto.getCerh());
			if(!Util.isNullOuVazio(cerhTipoUsoRemocao)){
				List<Integer> ideTipoUsoRecursoHidricoList = new ArrayList<Integer>();
				
				StringBuilder tipoRecursoHidrico = new StringBuilder();
				String virgula = ", ";
				Collection<Integer> ideCerhTipoUsoRemocao = new ArrayList<Integer>();
				
				for (CerhTipoUso cerhTipoUso : cerhTipoUsoRemocao) {
					ideCerhTipoUsoRemocao.add(cerhTipoUso.getIdeCerhTipoUso());
					if(!tipoRecursoHidrico.toString().contains(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getDscTipoUsoRecursoHidrico())){
						if(!Util.isNullOuVazio(tipoRecursoHidrico.toString())){
							tipoRecursoHidrico.append(virgula);
						}
						ideTipoUsoRecursoHidricoList.add(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico());
						tipoRecursoHidrico.append(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getDscTipoUsoRecursoHidrico());
					}
				}
				
				Collection<Integer> ideCerhLocalizacaoGeograficaRemocao = super.cerhLocalizacaoGeograficaDAO.listarCerhLocalizacaoGeografica(ideCerhTipoUsoRemocao);
				if(!Util.isNullOuVazio(ideCerhLocalizacaoGeograficaRemocao)){
					Collection<Integer> ideCerhCaptacaoCaracterizacaoRemocao = cerhCaptacaoCaracterizacaoDAO.listarByIdeCerhLocalizacaoGeografica(ideCerhLocalizacaoGeograficaRemocao);
					
					/*
					 *  mover essas mensagens para o local correto
					 *  */
					if(!Util.isNullOuVazio(ideCerhCaptacaoCaracterizacaoRemocao)){
						MensagemUtil.alerta("Existe(m) Caracterização(ões) de " + tipoRecursoHidrico + ". Por favor, exclua a(s) Caracterização(ões) ou associe-a(s) com um processo.");
						ajustarRespostas(ideTipoUsoRecursoHidricoList,dto);
						throw new SeiaValidacaoRuntimeException("Existe(m) Caracterização(ões) de " + tipoRecursoHidrico + ". Por favor, exclua a(s) Caracterização(ões) ou associe-a(s) com um processo.");
					} 
					else {
						MensagemUtil.alerta("Existe(m) Coordenada(s) de Caracterização(ões) de " + tipoRecursoHidrico + ". Por favor, exclua a(s) Coordenadas(s) ou associe-a(s) com um processo.");
						ajustarRespostas(ideTipoUsoRecursoHidricoList,dto);
						throw new SeiaValidacaoRuntimeException("Existe(m) Coordenada(s) de Caracterização(ões) de " + tipoRecursoHidrico + ". Por favor, exclua a(s) Coordenadas(s) ou associe-a(s) com um processo.");
					}
				}
			}
		}
	}
	
	private void ajustarRespostas(List<Integer> ideTipoUsoRecursoHidricoList,CerhAbaDadoGeraisDTO dto ){
		for (Integer ideTipoUsoRecursoHidrico : ideTipoUsoRecursoHidricoList) {
			if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId()) || ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())){
				dto.getResposta4().setIndResposta(true);
				
				if(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado()==null){
					dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionado(new ArrayList<TipoUsoRecursoHidrico>());
				}
				
				if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId())){
					dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado().add(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.BARRAGEM.getId()));
				}
				
				if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())){
					dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado().add(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId()));
				}
			}
			else if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId()) || ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId()) ){
				dto.getResposta5().setIndResposta(true);
				
				if(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado()==null){
					dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionado(new ArrayList<TipoUsoRecursoHidrico>());
				}
				if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())){
					dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado().add(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId()));
				}
				if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())){
					dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado().add(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId()));
				}
			}
			else if(ideTipoUsoRecursoHidrico.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())){
				dto.getResposta6().setIndResposta(true);
			}
		}
	}
 
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerenciarUpdateCerhProcesso(Cerh cerh)  {
		cerhProcessoListagemDAOImpl.gerenciarCerhProcessoRemovido(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerenciarUpdateCerhProcessoSuspensao(Cerh cerh) {
		if(!Util.isNullOuVazio(cerh.getCerhProcessoCollection())) {
			for(CerhProcesso cerhProcesso : cerh.getCerhProcessoCollection()) {
				if(!Util.isNull(cerhProcesso.getIdeCerhProcesso())) {
					cerhProcessoSuspensaoDAOImpl.gerenciarCerhProcessoSuspensaoRemovido(cerhProcesso);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorConveniado()  {
	    return !Util.isNullOuVazio(pessoaJuridicaDAOImpl.listarPessoaJuridicaConveniadaCERH(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isExisteCerhParaEmpreendimento(Empreendimento empreendimento)  {
		return !Util.isNullOuVazio(cerhDAOImpl.listarCerh(empreendimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh carregarCerhPor(Integer ideCerh) throws Exception {
		
		Cerh cerh = cerhDAOImpl.buscarCerhPor(ideCerh);
		cerh.setIdePessoaRequerente(carregarDadosRequerenteCerh(cerh.getIdePessoaRequerente()));
		
		cerh.setCerhRespostaDadosGeraisCollection(listarCerhRespostaDadosGerais(cerh));
		cerh.setCerhProcessoCollection(listarCerhProcesso(cerh));
		
		for (CerhProcesso cerhProcesso : cerh.getCerhProcessoCollection()) {
			cerhProcesso.setCerhTipoUsoCollection(listarCerhTipoUso(cerhProcesso));
			carregarTipoUsoRecursoHidrico(cerhProcesso.getCerhTipoUsoCollection());
		}
		
		cerh.setCerhTipoUsoCollection(listarCerhTipoUso(cerh));
		carregarTipoUsoRecursoHidrico(cerh.getCerhTipoUsoCollection(), cerh);
		
		return cerh;
	}
	
	private void carregarTipoUsoRecursoHidrico(Collection<CerhTipoUso> listaCerhTipoUso, Cerh... objCerh)  {
		Cerh cerh = objCerh.length > 0 ? objCerh[0] : null;
		for (CerhTipoUso cerhTipoUso : listaCerhTipoUso) {
			if(!Util.isNull(cerh)) {
				cerhTipoUso.setIdeCerh(cerh);
				cerhTipoUso.getIdeTipoUsoRecursoHidrico().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
				cerhTipoUso.getIdeTipoUsoRecursoHidrico().getCerhTipoUsoCollection().add(cerhTipoUso);
			}
			cerhTipoUso.setCerhLocalizacaoGeograficaCollection(super.cerhLocalizacaoGeograficaDAO.listar(cerhTipoUso));
			for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : cerhTipoUso.getCerhLocalizacaoGeograficaCollection()) {
				cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(super.localizacaoGeograficaServiceFacade.getDadoGeograficoCollection(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhProcesso> listarCerhProcesso(Cerh cerh) {
		Collection<CerhProcesso> listarCerhProcesso = cerhProcessoListagemDAOImpl.listarCerhProcessoPor(cerh);
		
		for(CerhProcesso cerhProcesso : listarCerhProcesso) {
			cerhProcesso.setCerhProcessoSuspensaoCollection(cerhProcessoSuspensaoDAOImpl.listar(Restrictions.eq("ideCerhProcesso", cerhProcesso), Order.asc("ideCerhProcessoSuspensao")));
		}
		
		return listarCerhProcesso;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerhTipoUso(Cerh cerh)  {
		return super.cerhTipoUsoDAO.listarCerhTipoUsoPor(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoUso carregarCerhTipoUso(Cerh cerh, TipoUsoRecursoHidrico tipoUsoRecursoHidrico)  {
		if(!Util.isNull(cerh.getIdeCerh())){
			return super.cerhTipoUsoDAO.carregarCerhTipoUso(cerh, tipoUsoRecursoHidrico);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoUso carregarCerhTipoUsoPorNumProcesso(String numProcesso, TipoUsoRecursoHidrico tipoUsoRecursoHidrico)  {
		if(numProcesso==null || tipoUsoRecursoHidrico==null){
			return null;
		}

		return super.cerhTipoUsoDAO.carregarCerhTipoUsoPorNumProcesso(numProcesso, tipoUsoRecursoHidrico);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa carregarDadosRequerenteCerh(Pessoa pessoa) throws Exception {
		Pessoa requerente = pessoaDAOImpl.carregarDadosRequerenteCerh(pessoa);
		requerente.setTelefoneCollection(telefoneService.listarTelefone(pessoa));
		return requerente;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhSituacaoRegularizacao> listarCerhSituacaoRegularizacao()  {
		return cerhSituacaoRegularizacaoDAOImpl.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoUsoRecursoHidrico> listarTipoUsoRecursoHidrico()  {
		return tipoUsoRecursoHidricoDAOImpl.listar(Order.asc("dscTipoUsoRecursoHidrico"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoAtoDispensa> listarCerhTipoAtoDispensa()  {
		return cerhTipoAtoDispensaDAOImpl.listar(Order.asc("dscTipoAtoDispensa"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoAutorizacaoOutorgado> listarCerhTipoAutorizacaoOutorgado()  {
		return cerhTipoAutorizacaoOutorgadoDAOImpl.listar(Order.asc("dscTipoAutorizacaoOutorgado"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarProcesso(String numProcesso)  {
		return processoDAOImpl.buscarProcessoPor(numProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarAtosProcesso(Processo processo)  {
		processo.setProcessoAtoCollection(processoAtoDAOImpl.listarAtosPor(processo));
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTipologiaDadosConcedidos(Processo processo)  {
		Collection<ProcessoAto> listaProcessoAto = processoAtoDAOImpl.listaProcessoAtoComDadosConcedidos(processo);
		if(Util.isNullOuVazio(listaProcessoAto)) {
			return null;
		}
		else {
			Collection<Tipologia> listaTipologia = new ArrayList<Tipologia>();
			for(ProcessoAto pa : listaProcessoAto) {

				Collection<LocalizacaoGeografica> listaLocalizacaoDadoConcedido = localizacaoGeograficaDAOImpl.listarTipologiaDadoConcedido(pa);
				
				if(listaLocalizacaoDadoConcedido!=null) {
					Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica = new ArrayList<CerhLocalizacaoGeografica>();
					
					for(LocalizacaoGeografica localizacaoGeografica : listaLocalizacaoDadoConcedido) {
						super.localizacaoGeograficaServiceFacade.tratarPonto(localizacaoGeografica);
						CerhLocalizacaoGeografica cerhLocalizacaoGeografica = new CerhLocalizacaoGeografica(localizacaoGeografica);

						if(tipologiaCorrespondenteAoDocumentoObrigatorio(pa.getTipologia(), new DocumentoObrigatorio(localizacaoGeografica.getIdeDocumentoObrigatorio()))){
							listaCerhLocalizacaoGeografica.add(cerhLocalizacaoGeografica);
						}
						
					}
					
					Tipologia tipologia = pa.getTipologia();
					tipologia.setListaCerhLocalizacaoGeografica(listaCerhLocalizacaoGeografica);
					listaTipologia.add(tipologia);
				}
				
			}
			return listaTipologia;
		}
	}
	
	private boolean tipologiaCorrespondenteAoDocumentoObrigatorio(Tipologia tipologia, DocumentoObrigatorio documentoObrigatorio){
		if(tipologia.getIdeTipologia().equals(TipologiaEnum.LANCAMENTO_EFLUENTES.getId())){
			if(documentoObrigatorio.getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES.getId())){
				return true;
			}
		}
		else if(tipologia.getIdeTipologia().equals(TipologiaEnum.CAPTACAO_SUBTERRANEA.getId())){
			if(documentoObrigatorio.getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUBTERRANEA.getId())){
				return true;
			}
		}
		else if(tipologia.getIdeTipologia().equals(TipologiaEnum.CAPTACAO_SUPERFICIAL.getId())){
			if(documentoObrigatorio.getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUPERFICIAL.getId())){
				return true;
			}
		}
		return false;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhPerguntaDadosGerais> listar()  {
		return cerhPerguntaDadosGeraisDAOImpl.listar(Order.asc("codPergunta"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhRespostaDadosGerais> listarCerhRespostaDadosGerais(Cerh cerh)  {
		return cerhRespostaDadosGeraisDAOImpl.listarCerhRespostaDadosGeraisPor(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarEnderecoEmpreendimento(Empreendimento empreendimento)  {
		empreendimento.setEndereco(enderecoDAOImpl.buscarEnderecoPor(empreendimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPor(Processo processo, Empreendimento empreendimento) {
		return empreendimentoDAOImpl.buscarEmpreendimentoPor(processo, empreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ContratoConvenio> listarContratoConvenio()  {
		return contratoConvenioDAOImpl.listar(Order.asc("nomContratoConvenio"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isUsuarioLogadoComContratoConvenio()  {
		return contratoConvenioDAOImpl.isPessoaComContratoConvenio(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerhTipoUso(CerhProcesso cerhProcesso) {
		return super.cerhTipoUsoDAO.listar(cerhProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhProcesso(CerhProcesso cerhProcesso) {
		cerhProcessoListagemDAOImpl.salvar(cerhProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarLocalizacaoGeograficaByProcessoAto(ProcessoAto processoAto) throws Exception{
		return super.localizacaoGeograficaServiceFacade.listarLocalizacaoGeograficaBy(processoAto.getProcesso(), ModalidadeOutorgaEnum.INTERVENCAO, TipoIntervencaoEnum.CONSTRUCAO_BARRAGEM);
	}
	
}

package br.gov.ba.seia.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoGeral;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TipoLocalAtividadeInexigivel;
import br.gov.ba.seia.entity.TipoRioIntervencao;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeService {

	@Inject
	private IDAO<DeclaracaoInexigibilidade> declaracaoInexigibilidadeDAO;
	
	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoGeral> declaracaoInfoGeralDAO;
	
	@Inject
	private IDAO<TipoLocalAtividadeInexigivel> tipoLocalAtividadeInexigivelDAO;
	
	@Inject
	private IDAO<Requerimento> requerimentoDAO;
	
	@Inject
	private IDAO<TipoRioIntervencao> tipoRioIntervencaoDAO;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private CertificadoService certificadoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidade obterDeclaracaoPorRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoInexigibilidade.class)
				.createAlias("requerimento", "requerimento")
				.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return declaracaoInexigibilidadeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade)  {
		this.declaracaoInexigibilidadeDAO.salvarOuAtualizar(declaracaoInexigibilidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoRealizacaoAtividade(DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral) {
		declaracaoInfoGeralDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoGeral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Enquadramento finalizar(Requerimento requerimento, DeclaracaoInexigibilidade declaracaoInexigibilidade)  {
		Enquadramento enquadramento = efetuarEnquadramentoAutomatico(requerimento, AtoAmbientalEnum.INEXIGIBILIDADE, StatusRequerimentoEnum.ENQUADRADO);
		
		requerimento.setDtcFinalizacao(new Date());
		
		this.requerimentoService.atualizarRequerimento(requerimento);
		
		this.salvarDeclaracaoInexigibilidade(declaracaoInexigibilidade);
		
		return enquadramento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void geraNumeroRequerimento(Requerimento requerimento)  {
		requerimento.setNumRequerimento(gerarNumero(requerimento));
		
		requerimentoDAO.salvarOuAtualizar(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumero(Requerimento requerimento)  {
		StringBuilder numeroRequerimento = new StringBuilder();
		
		int contador = 0 ;
		boolean primeiraConsulta = true;	
		
		while(primeiraConsulta || requerimentoService.isExisteNumRequerimento(requerimento)){	
			contador++;
		
			if(!primeiraConsulta){
				numeroRequerimento = new StringBuilder();
			}
			
			numeroRequerimento  
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');
			
			numeroRequerimento
				.append(Util.lpad(requerimento.getIdeOrgao().getCodOrgao().toString(), '0', 3))
			    .append('.');
		
			numeroRequerimento 
				.append(getNumByOrgaoByAno(requerimento, contador))
				.append('/');
				
			numeroRequerimento  
				.append(requerimento.getIdeOrgao().getDscSiglaOrgao())
				.append("/INEXIG");
			
			primeiraConsulta = false;
			requerimento.setNumRequerimento(numeroRequerimento.toString());
		}
		
		return numeroRequerimento.toString();
	}
	
	private String getNumByOrgaoByAno(Requerimento requerimento, int contador) {
		Requerimento r = this.getMaiorNumeroRequerimentoByOrgaoByAnoAtual(requerimento);
		String numRequerimento = null;	
		
		if (!Util.isNullOuVazio(r) && !Util.isNullOuVazio(r.getNumRequerimento())) {
			if (r.getNumRequerimento().length() >= 16) {
				numRequerimento = Util.lpad(Integer.toString(Integer.parseInt(r.getNumRequerimento().substring(9, 15)) + contador), '0', 6);
			}
		}else{
			numRequerimento =  Util.lpad(Integer.toString(contador) , '0', 6);			
		}
		
		return numRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Requerimento getMaiorNumeroRequerimentoByOrgaoByAnoAtual(Requerimento requerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Requerimento.class)
			.add(Restrictions.eq("ideOrgao", requerimento.getIdeOrgao()))
			.add(Restrictions.sqlRestriction("substring (num_requerimento, 0, 5) = '"+ Calendar.getInstance().get(Calendar.YEAR) + "'"));
		
			if(Calendar.getInstance().get(Calendar.YEAR) == 2016){
				detachedCriteria.add(Restrictions.sqlRestriction("((Substring (num_requerimento, 10, 6)) :: INTEGER ) < 28405 OR ((Substring (num_requerimento, 10, 6)) :: INTEGER ) > 29321"));
			}
	
			detachedCriteria.setProjection(Projections.projectionList()
					.add(Projections.max("numRequerimento"),"numRequerimento")

			).setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class));
		return requerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	private Enquadramento efetuarEnquadramentoAutomatico(Requerimento requerimento, AtoAmbientalEnum atoAmbientalEnum, StatusRequerimentoEnum statusRequerimentoEnum)  {
		
		AtoAmbiental atoAmbiental = this.atoAmbientalService.carregarById(atoAmbientalEnum.getId());

		Enquadramento enquadramento = gerarEnquadramento(requerimento, atoAmbiental);

		this.enquadramentoService.salvar(enquadramento);
		
		return enquadramento;
	}
	
	private Enquadramento gerarEnquadramento(Requerimento requerimento, AtoAmbiental atoAmbiental)  {
		Enquadramento enquadramento = new Enquadramento();
		enquadramento.setIdeRequerimento(requerimento);
		enquadramento.setIndEnquadramentoAprovado(true);
		enquadramento.setIndPassivelEiarima(false);
		enquadramento.setIdePessoa(this.pessoaService.carregarUsuarioSEIA());
		gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento);

		return enquadramento;
	}
	
	private void gerarEnquadramentoAtoAmbiental(AtoAmbiental atoAmbiental, Enquadramento enquadramento) {
		enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());

		EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental(enquadramento, atoAmbiental, null);

		enquadramento.getEnquadramentoAtoAmbientalCollection().add(enquadramentoAtoAmbiental);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoLocalAtividadeInexigivel> obterListaTipoLocalAtividadeInexigivel() {
		return this.tipoLocalAtividadeInexigivelDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoLocalAtividadeInexigivel obterTipoLocalAtividadePor(Integer ideTipoLocalAtividadeInexigivel)  {
		TipoLocalAtividadeInexigivel tipo =null;
		
		DetachedCriteria detached = DetachedCriteria.forClass(TipoLocalAtividadeInexigivel.class)
				.add(Restrictions.idEq(ideTipoLocalAtividadeInexigivel));
		
		tipo = tipoLocalAtividadeInexigivelDAO.buscarPorCriteria(detached);
		
		return tipo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoRioIntervencao> obterTodosTipoRioIntervencao() {
		return tipoRioIntervencaoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoRioIntervencao obterTipoRioIntervencaoPor(Integer ideTipoRioIntervencao)  {
		TipoRioIntervencao tipo = null;
		
		DetachedCriteria detached = DetachedCriteria.forClass(TipoRioIntervencao.class)
				.add(Restrictions.idEq(ideTipoRioIntervencao));
		
		tipo = tipoRioIntervencaoDAO.buscarPorCriteria(detached);
		
		return tipo;		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarRequerimento(Requerimento requerimento)  {
		Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());

		Collection<TramitacaoRequerimento> collTramitacaoRequerimento = requerimento.getTramitacaoRequerimentoCollection();
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		
		requerimentoService.inserirRequerimento(requerimento);
		requerimentoUnicoService.salvarTramitacaoRequerimento(requerimento, collTramitacaoRequerimento);
		requerimentoUnicoService.salvarPessoasRequerimento(requerimento, collRequerimentoPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideRequerimento)  {
		return this.certificadoService.exists(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificadoInexigibilidade(Certificado certificado)  {
		String numeroCertificao = this.certificadoService.gerarNumeroCertificadoInexigibilidade(certificado);
		certificado.setNumCertificado(numeroCertificao);
		this.certificadoService.salvar(certificado);
	}
	
	public boolean hasToken(Integer ideRequerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoService.hasToken(ideRequerimento, atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Certificado carregarCertificado(Integer ideRequerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoService.carregarByIdRequerimentoAndAto(ideRequerimento,atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTokenCertificado(Certificado certificado)  {
		this.certificadoService.gerarEValidarToken(certificado);
		this.certificadoService.atualizar(certificado);
	}
}

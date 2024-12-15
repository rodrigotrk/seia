package br.gov.ba.seia.service.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.CategoriaDocumentoDAOImpl;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.MotivoNotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoMotivoNotificacaoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.facade.ArquivoProcessoFacade;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MotivoNotificacaoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ApensarDocumentoServiceFacade {
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private ArquivoProcessoFacade arquivoProcessoFacade;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private CategoriaDocumentoDAOImpl categoriaDocumentoDAOImpl;
	@EJB
	private NotificacaoMotivoNotificacaoDAOImpl notificacaoMotivoNotificacaoDAOImpl;
	@EJB
	private AreaService areaService;	
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	@EJB
	private MotivoNotificacaoDAOImpl motivoNotificacaoDAOImpl;
	@EJB
	private ImovelDAOImpl imovelListagemDAOImpl;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarImoveis(Notificacao notificacao) throws Exception {
		Collection<Imovel> imoveis = imovelListagemDAOImpl.listarImoveisPor(notificacao);
		for(Imovel i : imoveis) {
			i.setMotivoNotificacaoImovelCollection(new ArrayList<MotivoNotificacaoImovel>());
			for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
				for(MotivoNotificacaoImovel mni : nmn.getMotivoNotificacaoImovelCollection()) {
					if(i.equals(mni.getIdeImovel())) {
						notificacao.setMotivoNotificacaoSelecionado(nmn.getIdeMotivoNotificacao());
						ArquivoProcesso shape = carregarArquivoShape(notificacao,mni.getIdeImovel());
						if(!Util.isNull(shape)) {
							mni.setVisualizarShape(verificaSeExisteTheGeomValido(shape.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
						}
						i.getMotivoNotificacaoImovelCollection().add(mni);
						notificacao.setMotivoNotificacaoSelecionado(null);
					}
				}
			}
		}
		return imoveis;
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso buscarVwCosultaProcesso(Notificacao notificacao) throws Exception {
		return vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(notificacao.getIdeProcesso().getIdeProcesso(), false);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificaSeExisteTheGeomValido(Integer ideLocalizacaoGeografica) throws Exception {
		return localizacaoGeograficaService.verificaSeExisteTheGeomValido(ideLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarExistenciaShape(Notificacao notificacao)  {
		return motivoNotificacaoService.verificarExistenciaShape(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacao(Notificacao notificacao) {
		return motivoNotificacaoDAOImpl.listarMotivoNotificacao(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario buscarFuncionarioPorPessoaFisica(PessoaFisica pessoaFisica) {
		return funcionarioService.buscarFuncionarioPorPessoaFisica(pessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CategoriaDocumento> listarCategoriaDocumentoPorArea(Area area) {
		return categoriaDocumentoDAOImpl.listarCategoriaDocumentoPorArea(area);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CategoriaDocumento> listarCategoriaDocumentoPorPerfil(Perfil perfil)  {
		return categoriaDocumentoDAOImpl.listarCategoriaDocumentoPorPerfil(perfil);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ArquivoProcesso> listaArquivoProcessoPor(Integer ideProcesso, Integer idePessoaFisica)  {
		return (List<ArquivoProcesso>) arquivoProcessoService.listaArquivoProcessoPorIdeProcessoAndIdePessoa(ideProcesso, idePessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CategoriaDocumento carregarCategoriaDocumento(Integer ideCategoriaDocumento)  {
		return categoriaDocumentoDAOImpl.carregarCategoriaDocumento(ideCategoriaDocumento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ArquivoProcesso carregarArquivoShape(Notificacao notificacao, Imovel imovel) {
		return arquivoProcessoService.carregarArquivoShape(notificacao,imovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ArquivoProcesso carregarArquivoShape(Notificacao notificacao, MotivoNotificacaoEnum motivoEnum, Imovel imovel) {
		return arquivoProcessoService.carregarArquivoShape(notificacao,imovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarMotivosNotificacao(Notificacao notificacao) {
		return notificacaoDAOImpl.carregarMotivosNotificacao(notificacao.getIdeNotificacao());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarNotificacaoMotivoNotificacao(Notificacao notificacao) throws Exception {
		notificacao.setNotificacaoMotivoNotificacaoCollection(notificacaoMotivoNotificacaoDAOImpl.listarNotificacaoMotivoNotificacaoPor(notificacao));
		return notificacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ArquivoProcesso> carregarListaArquivoShapeComDadoGeografico(Notificacao notificacao){
		return arquivoProcessoService.carregarListaArquivoShapeComDadoGeografico(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent getContentFile(ArquivoProcesso arquivoSelecionado) throws Exception {
		return gerenciaArquivoService.getContentFile(arquivoSelecionado.getDscCaminhoArquivo());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaArquivos(List<ArquivoProcesso> listaArquivosProcesso) {
		arquivoProcessoService.salvarListaArquivos(listaArquivosProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComNotificacao(List<ArquivoProcesso> listaArquivosProcesso, Notificacao notificacao) throws Exception {
		arquivoProcessoFacade.salvarComNotificacao(listaArquivosProcesso, notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarArquivo(ArquivoProcesso arquivoProcessoSelecionado) {
		arquivoProcessoService.deletarArquivo(arquivoProcessoSelecionado);
	}
}

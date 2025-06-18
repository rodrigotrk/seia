package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.BoletoDaeRequerimentoImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ParametroTaxaCertificado;
import br.gov.ba.seia.entity.ParametroTaxaVistoria;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoDaeRequerimentoService {

	@Inject
	private BoletoDaeRequerimentoImpl boletoDaeRequerimentoImpl;
	
	@Inject
	private IDAO<BoletoDaeRequerimento> boletoDaeRequerimentoDAO;

	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;

	@EJB
	private AtoAmbientalService atoAmbientalService;

	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	
	@EJB
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(BoletoDaeRequerimento boletoDaeRequerimento)  {
		boletoDaeRequerimentoImpl.salvarOuAtualizar(boletoDaeRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Double calcularValorTotalCertificado(BoletoDaeRequerimento boletoDaeRequerimento, List<ParametroTaxaCertificado> parametros) {
		Double valorTotalCertificado = 0D;
		AtoAmbiental atoAmbiental = new AtoAmbiental(AtoAmbientalEnum.ARLSF.getId());
		boolean existeReservaLegal = false;
		for (ParametroTaxaCertificado parametro : parametros) {
			if (!parametro.getAtoAmbiental().equals(atoAmbiental)) {
				valorTotalCertificado += parametro.getValTaxaCertificado().doubleValue();
			} else {
				existeReservaLegal = true;
			}
		}

		if (existeReservaLegal) {
			List<ImovelRural> listaImoveisRurais = this.imovelRuralServiceFacade.carregarImoveisRuraisByRequerimento(boletoDaeRequerimento.getIdeRequerimento()
					.getIdeRequerimento());

			for (ImovelRural imovelRural : listaImoveisRurais) {
				boolean maiorTaxa = imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECOND.getId())
						|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())
						|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.CDAUC.getId());

				ParametroTaxaCertificado taxaAserAplicada = this.atoAmbientalService.buscarValorDeTaxaReservaLegal(maiorTaxa);
				valorTotalCertificado += taxaAserAplicada.getValTaxaCertificado().doubleValue();
			}

		}

		return valorTotalCertificado;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ParametroTaxaVistoria getParametroTaxaVistoria(BoletoDaeRequerimento boletoDaeRequerimento) {
		return boletoDaeRequerimentoImpl.getParametroTaxaVistoria(boletoDaeRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal calcularValorTotalVistoria(BoletoDaeRequerimento boletoDaeRequerimento) {
		ParametroTaxaVistoria parametroTaxaVistoria = this.getParametroTaxaVistoria(boletoDaeRequerimento);
		BigDecimal valorTotalVistoria = null;
		if (!Util.isNull(parametroTaxaVistoria)) {
			valorTotalVistoria = parametroTaxaVistoria.getValTaxaVistoria();
		} else {
			valorTotalVistoria = new BigDecimal("0.00");
		}
		return valorTotalVistoria;
	}
	

	public BoletoDaeRequerimento carregarVistoriaByRequerimento(Integer ideRequerimento) {
		return boletoDaeRequerimentoImpl.carregarVistoriaByRequerimento(ideRequerimento);
	}

	public BoletoDaeRequerimento carregarCertificadoByRequerimento(Integer ideRequerimento)  {
		return boletoDaeRequerimentoImpl.carregarCertificadoByRequerimento(ideRequerimento);
	}

	public BoletoDaeRequerimento carregarCertificadoByProcessoRequerimento(Integer ideProcessoRequerimento)  {
		return boletoDaeRequerimentoImpl.carregarCertificadoByProcessoRequerimento(ideProcessoRequerimento);
	}

	public void atualizarComunicacaoRequerimento(BoletoDaeRequerimento dae,boolean certificado)   {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		
		Requerimento requerimento = dae.getIdeRequerimento();
		
		final String assunto = "[SEIA] - DAE do Requerimento de nº "+ requerimento.getNumRequerimento();
		
		final StringBuilder lMsg = new StringBuilder();
		lMsg.append("Prezado(a) "+requerimento.getRequerente().getNomeRazao());
		
		if(certificado)
			lMsg.append(", \n O DAE (Documento de Arrecadação Estadual) de Certificado do Requerimento de nº '");
		else
			lMsg.append(", \n O DAE (Documento de Arrecadação Estadual) de Vistoria do Requerimento de nº '");
		
		lMsg.append(requerimento.getNumRequerimento());
		lMsg.append("' já está disponível.\n");
		
		lMsg.append("A geração de DAE é feita a partir do site da SEFAZ (www.sefaz.ba.gov.br) no seguinte caminho: \"Inspetoria eletrônica - Pagamentos - Cálculo e geração DAE - Taxas. \n");
		
		
		lMsg.append("Utilize os dados abaixo para geração do DAE : \n");
		if(certificado){
			lMsg.append("Código: 2214 \n");
			lMsg.append("Valor: R$ "+df.format(dae.getVlrTotalCertificado()));
		}else{
			lMsg.append("Código: 2345 \n");
			lMsg.append("Valor: R$ "+df.format(dae.getVlrTotalVistoria()));
		}
		
		lMsg.append("Após o pagamento, favor acessar o SEIA para efetuar o envio do comprovante de pagamento do DAE.\n");
		
		lMsg.append("Atte.,\n");
		lMsg.append("Central de Atendimento/INEMA.\n");
		
		ComunicacaoRequerimento comunicacaoRequerimento = gerarComunicacao(requerimento, assunto, lMsg);
		this.comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		emailService.enviarEmailsAoRequerente(requerimento, assunto, lMsg.toString());
		
	}

	private ComunicacaoRequerimento gerarComunicacao(Requerimento requerimento, final String assunto,
			final StringBuilder lMsg) {
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(lMsg.toString());
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimento.setAssunto(assunto);
		return comunicacaoRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoDaeRequerimento carregarByRequerimento(Integer ideRequerimento) {
		return boletoDaeRequerimentoDAO.buscarPorCriteria(prepararCriteriaRestrictTo(ideRequerimento));
	}

	private DetachedCriteria prepararCriteriaRestrictTo(Integer ideRequerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req");
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoDaeRequerimento"),"ideBoletoDaeRequerimento")
		);
		
		detachedCriteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoDaeRequerimento.class));
		return detachedCriteria;
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoDaeRequerimento> listarByRequerimento(Integer ideRequerimento) {
		return boletoDaeRequerimentoDAO.listarPorCriteria(prepararCriteriaRestrictTo(ideRequerimento));
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoDaeRequerimento carregarById(Integer ideBoleto)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		
		detachedCriteria.createAlias("ideProcesso", "processo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcesso.ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideTipoBoletoPagamento", "tipoBoletoPagamento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoDaeHistorico", "boletoDaeHistorico", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoDaeHistorico.ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcessoReenquadramento", "reenquadramento", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("ideBoletoDaeRequerimento", ideBoleto));
		
		return boletoDaeRequerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
}
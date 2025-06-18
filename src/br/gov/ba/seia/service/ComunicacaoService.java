package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ComunicacaoDAOImpl;
import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.entity.ComunicacaoStatus;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoService {

	@EJB
	private ComunicacaoDAOImpl comunicacaoDAOimpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Comunicacao comunicacao) {
		Date dtAtual = DataUtil.getDataAtual();
		if (Util.isNullOuVazio(comunicacao.getDtcCriacao())) {
			comunicacao.setDtcCriacao(dtAtual);
		}
		Integer ideStatus = ComunicacaoStatusEnum.NOVO.getIdComunicacaoStatus();
		if (comunicacao.getDtcPeriodoInicio().equals(dtAtual) && comunicacao.isIndAtiva()) {
			ideStatus = ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus();
		}

		comunicacao.setIdeComunicacaoStatus(ideStatus);
		comunicacao.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado());

		comunicacaoDAOimpl.salvarOuAtualizarComunicacao(comunicacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void verificarCampos(Comunicacao comunicacao, List<Perfil> perfilL) throws SeiaException {
		if (Util.isNullOuVazio(comunicacao.getTpComunicacao())) {
			throw new SeiaException("O campo Tipo de Comunicação é Obrigatório");
		} else if (Util.isNullOuVazio(perfilL) || perfilL.isEmpty()) {
			throw new SeiaException("O campo Perfil é Obrigatório");
		} else if (Util.isNullOuVazio(comunicacao.getDtcPeriodoInicio())
				|| Util.isNullOuVazio(comunicacao.getDtcPeriodoFim())) {
			throw new SeiaException("O campo Período é Obrigatório");
		} else if (comunicacao.getDtcPeriodoInicio().after(comunicacao.getDtcPeriodoFim())) {
			throw new SeiaException("A Data Inicial não pode ser maior que a Data Final");
		} else if (comunicacao.getDscTitulo().trim().isEmpty()) {
			throw new SeiaException("O campo Título é Obrigatório");
		} else if (comunicacao.getTxtConteudo().trim().isEmpty()) {
			throw new SeiaException("O campo Texto da Comunicação é Obrigatório");
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarComunicacaoTemporaria(Comunicacao com, List<Perfil> perfilL) {
		boolean result = false;
		List<Integer> idPerfilL = new ArrayList<Integer>();
		for (Perfil p : perfilL) {
			idPerfilL.add(p.getIdePerfil());
		}
		Collection<Comunicacao> comList = comunicacaoDAOimpl.localizarComunicacaoTemporaria(com.getIdeComunicacao(),
				idPerfilL,
				Arrays.asList(ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus(),
						ComunicacaoStatusEnum.NOVO.getIdComunicacaoStatus()),
				true, "T", null, com.getDtcPeriodoInicio(), com.getDtcPeriodoFim());
		if (!comList.isEmpty()) {
			result = true;
		}
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComunicacao(Comunicacao comunicacao) {
		comunicacaoDAOimpl.atualizarComunicacao(comunicacao);
	}

	/**
	 * <p>
	 * Excluir ou Cancelar a comunicação verificando se está ativa e o período está
	 * vigente
	 * </p>
	 * 
	 * @param comunicacao
	 * @throws Exception
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean excluir(Comunicacao comunicacao) {
		comunicacao = find(comunicacao.getIdeComunicacao());
		boolean resultado = false;
		if (Util.isNullOuVazio(comunicacao)) {
			JsfUtil.addErrorMessage("O registro não existe");

		} else if (comunicacao.getIdeComunicacaoStatus().equals(ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus())
				&& comunicacao.isIndAtiva()) {
			comunicacao.setIdeComunicacaoStatus(ComunicacaoStatusEnum.CANCELADO.getIdComunicacaoStatus());
			comunicacaoDAOimpl.salvarOuAtualizarComunicacao(comunicacao);
			resultado = true;
		} else {
			comunicacaoDAOimpl.excluirComunicacao(comunicacao);
			resultado = true;
		}
		return resultado;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Comunicacao> listar() {

		return (List<Comunicacao>) comunicacaoDAOimpl.findAll();
	}

	public List<Comunicacao> findByFilterLast(Perfil perfil, Integer ideStatus, boolean indAtiva,
			String tpComunicacao) {
		Integer idePerfil = Util.isNullOuVazio(perfil) ? null : perfil.getIdePerfil();
		return (List<Comunicacao>) comunicacaoDAOimpl.findByFilterLast(idePerfil, ideStatus, indAtiva, tpComunicacao);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Comunicacao> listarByFiltroConsultarUsuario(String titulo, Perfil perfil, Date dtInicio) {
		Integer idePerfil = Util.isNullOuVazio(perfil) ? null : perfil.getIdePerfil();
		return comunicacaoDAOimpl.findAllByFiltro(idePerfil, titulo, dtInicio);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Comunicacao> buscarComunicacoesParaAtualizacaoDiaria() {
		return (List<Comunicacao>) comunicacaoDAOimpl.buscarComunicacoesParaAtualizacaoDiaria();

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Comunicacao find(Integer id) {
		return comunicacaoDAOimpl.find(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Comunicacao findTemporaria(Perfil perfil) {
		return comunicacaoDAOimpl.findComunicacaoTemporaria(perfil.getIdePerfil());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Comunicacao> listByFilterPaginator(String titulo, Perfil perfil, ComunicacaoStatus comunicacaoStatus,
			Boolean indAtiva, String tpComunicacao, Date dtInicio, Date dtFim, Integer first, Integer pageSize) {
		Integer idePerfil = Util.isNullOuVazio(perfil) ? null : perfil.getIdePerfil();
		Integer ideStatus = Util.isNullOuVazio(comunicacaoStatus) ? null : comunicacaoStatus.getIdeComunicacao();
		return (List<Comunicacao>) comunicacaoDAOimpl.findByFiltroPaginator(idePerfil, ideStatus, indAtiva,
				tpComunicacao, titulo, dtInicio, dtFim, first, pageSize);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(String descTitulo, Perfil perfil, ComunicacaoStatus comunicacaoStatus, Boolean indAtiva,
			String tpComunicacao, Date dtPeriodoInicial, Date dtPeriodoFinal) {
		Integer idePerfil = Util.isNullOuVazio(perfil) ? null : perfil.getIdePerfil();
		Integer ideStatus = Util.isNullOuVazio(comunicacaoStatus) ? null : comunicacaoStatus.getIdeComunicacao();
		return comunicacaoDAOimpl.count(descTitulo, idePerfil, ideStatus, indAtiva, tpComunicacao, dtPeriodoInicial,
				dtPeriodoFinal);
	}

}

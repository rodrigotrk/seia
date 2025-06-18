package br.gov.ba.seia.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralMudancaStatusJustificativa;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralMudancaStatusJustificativaService {

	@Inject
	private IDAO<ImovelRuralMudancaStatusJustificativa> imovelRuralMudancaStatusJustificativaDAO;
	@Inject
	private ImovelRuralService imovelRuralService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralMudancaStatusJustificativa iRmudancaStatusJustificativa) throws Exception {
		try {
			

		//ImovelRural imovelRural = getImovelRural(iRmudancaStatusJustificativa);
		ImovelRural imovelRural = iRmudancaStatusJustificativa.getIdeImovelRural();
	//	iRmudancaStatusJustificativa.setIdeImovelRural(imovelRural);
		
		if(imovelRural.getStatusCadastro() == null) {
			throw new Exception("Não é possível alterar o status deste cadastro porque o imóvel não possui status Cadastrado");
		}
		if(imovelRural.isCadastrado() || imovelRural.isRegistrado()) {

			if(iRmudancaStatusJustificativa.isIndAlterarProprietario()) {
				if(!Util.isNullOuVazio(iRmudancaStatusJustificativa.getIdeProprietario()) && !Util.isNullOuVazio(iRmudancaStatusJustificativa.getIdeProprietario().getIdePessoa())) {
					imovelRuralService.callTransferenciaFunction(imovelRural.getIdeImovelRural(), 
							imovelRural.getIdeRequerenteCadastro().getIdePessoa(),
							iRmudancaStatusJustificativa.getIdeProprietario().getIdePessoa());
				}
			}else {
				imovelRuralService.invalidarCertificadoTransferenciaTitularidade(imovelRural);
			}

			
			Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Salvando Mudança Status Justificativa Imovel Rural- 631");
			imovelRuralMudancaStatusJustificativaDAO.salvarOuAtualizar(iRmudancaStatusJustificativa);

			return;
		}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw new Exception(e);
		}		
		//throw new Exception("Não é possível alterar o status deste cadastro porque o imóvel não possui status Cadastrado");
	}

	private void permitirEdicaoImovel(ImovelRural imovelRural) {
		if(!Util.isNull(imovelRural.getImovelRuralSicar())) {
			if(imovelRural.getImovelRuralSicar().isIndSicronia()) {
				imovelRural.getImovelRuralSicar().setIndSicronia(false);
			}
		}
	}

	private void atualizarNovoRequerente(ImovelRuralMudancaStatusJustificativa iRmudancaStatusJustificativa, ImovelRural imovelRural)  {
		TipoVinculoImovel tipoVinculoImovel = new TipoVinculoImovel();
		List<PessoaImovel> listPessoaImovelExcluidos = new ArrayList<PessoaImovel>();
		
		for (PessoaImovel pessoaImovel : imovelRural.getImovel().getPessoaImovelCollection()) {
			if(pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(5) || pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(3)){
				imovelRuralService.removerPessoaImovel(pessoaImovel);
				listPessoaImovelExcluidos.add(pessoaImovel);
			}
			
			if(pessoaImovel.getIdePessoa().getIdePessoa().equals(imovelRural.getIdeRequerenteCadastro().getIdePessoa())) {
				tipoVinculoImovel = pessoaImovel.getIdeTipoVinculoImovel();
				imovelRuralService.removerPessoaImovel(pessoaImovel);
				listPessoaImovelExcluidos.add(pessoaImovel);
			}
		}
		
		for (PessoaImovel pessoaImovel : listPessoaImovelExcluidos) {
			imovelRural.getImovel().getPessoaImovelCollection().remove(pessoaImovel);
		}
		
		if(Util.isNullOuVazio(tipoVinculoImovel.getIdeTipoVinculoImovel())) {
			tipoVinculoImovel.setIdeTipoVinculoImovel(1);
		}
		
		PessoaImovel pessoaImovel = new PessoaImovel(null, new Date(), false);
		pessoaImovel.setIdePessoa(iRmudancaStatusJustificativa.getIdeProprietario());
		pessoaImovel.setIdeImovel(imovelRural.getImovel());
		pessoaImovel.setIdeTipoVinculoImovel(tipoVinculoImovel);
		imovelRuralService.salvarImovelPessoa(pessoaImovel);
		imovelRural.setIdeRequerenteCadastro(iRmudancaStatusJustificativa.getIdeProprietario());
	}

	private void excluirDocumentacaoDoImovel(ImovelRural imovelRural) {
		imovelRural.setIdeDocumentoProcuracao(null);
		imovelRural.getDocumentoImovelRuralPosse().setIdeDocumentoImovelRural(null);
	}

	private ImovelRural getImovelRural(ImovelRuralMudancaStatusJustificativa iRmudancaStatusJustificativa) throws CampoObrigatorioException {		
		return imovelRuralService.carregarTudo(iRmudancaStatusJustificativa.getIdeImovelRural());
	}

	public void validarSeCedeAreaParaCompensarReservaLegal(Integer ideImovelRural) throws Exception  {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM sp_get_imoveldebito(" + ideImovelRural + ", 'GEOMETRYCOLLECTION(EMPTY)')");

		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());
		
		if (!Util.isNullOuVazio(lQuery.getResultList())) {
			throw new Exception(
					"Não é possível alterar o status deste cadastro porque existe reserva legal sendo compensada na área do imóvel.");
		}
	}

	public void validarSeReservaAprovadaOuAverbada(ImovelRural imovelRural) throws Exception  {
		imovelRural = getImovelRural(new ImovelRuralMudancaStatusJustificativa(imovelRural));
		ReservaLegal reservaLegal = imovelRural.getReservaLegal();
		if (Util.isNull(reservaLegal) || Util.isNull(reservaLegal.getIdeReservaLegal())) {
			return; //throw new Exception("Reserva Nula");
		}
		
		boolean erro = false;
		if (!Util.isNull(reservaLegal.getIdeStatus())) { 
			if(reservaLegal.getIdeStatus().getIdeStatusReservaLegal() == 1) {//Aprovada
					erro = true;
			}
			if (reservaLegal.getIdeStatus().getIdeStatusReservaLegal() == 2) {//Averbada
				erro = true;
			}
		}

		if (erro) {
			throw new Exception(
					"Não é possível alterar o status deste cadastro porque a Reserva Legal está aprovada e/ou averbada.");
		}
	}

	public void validarPossuiVinculoComEmpreendimento(ImovelRural imovelRural) throws Exception  {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ");
		sql.append("FROM empreendimento e ");
		sql.append("JOIN imovel_empreendimento ie ON (e.ide_empreendimento = ie.ide_empreendimento) ");
		sql.append("WHERE ide_imovel = :ideImovelRural AND ");
		sql.append("e.ind_excluido = FALSE");

		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());

		lQuery.setParameter("ideImovelRural", imovelRural.getIdeImovelRural());

		@SuppressWarnings("unchecked")
		List<BigInteger> resultList = lQuery.getResultList();
		if (!Util.isNullOuVazio(resultList)) {
			if(resultList.get(0).intValue() != 0) {
				throw new Exception(
						"Não é possível alterar o status deste cadastro porque o  imóvel está vinculado a um empreendimento.");
			}
		}
	}

	public Integer buscarIdeStatusPorImovelRural(ImovelRural ideImovelRural) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ide_status_justificativa ");
		sql.append("from imovel_rural_mudanca_status_justificativa ");
		sql.append("where ide_imovel_rural = :ideImovelRural order by dtc_justificativa desc");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());

		lQuery.setParameter("ideImovelRural", ideImovelRural.getIdeImovelRural());

		@SuppressWarnings("unchecked")
		List<Integer> resultList = lQuery.getResultList();
		if (!Util.isNullOuVazio(resultList)) {
			if(resultList.get(0).intValue() != 0) {
				return resultList.get(0).intValue();
			}
		}
		return null;
	}
	
	public Collection<ImovelRuralMudancaStatusJustificativa> listarImovelRuralMudancaStatusJustificativaPorImovel(ImovelRural imovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralMudancaStatusJustificativa.class, "imovelRuralMudancaStatusJustificativa");		
		criteria.add(Restrictions.eq("imovelRuralMudancaStatusJustificativa.ideImovelRural",  imovelRural));
		
		return this.imovelRuralMudancaStatusJustificativaDAO.listarPorCriteria(criteria);		
	}
}

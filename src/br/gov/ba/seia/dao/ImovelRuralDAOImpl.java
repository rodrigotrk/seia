package br.gov.ba.seia.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralDesbloqueio;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.entity.auditoria.HistCampo;
import br.gov.ba.seia.entity.auditoria.HistTabela;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.enumerator.TipoCadastroEnum;
import br.gov.ba.seia.enumerator.TipoCadastroImovelRuralEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class ImovelRuralDAOImpl implements ImovelRuralDAOIf {

	@Inject
	private IDAO<ImovelRural> imovelRuralDAO;
	
	@Inject
	private IDAO<PessoaImovel> pessoaImovelDAO;
	
	@Inject
	private RepresentanteLegalService representanteLegalService;

	@Inject
	private HistoricoSuspensaoCadastroDAOImpl historicoSuspensaoCadastroDAOImpl;
	
	@EJB
	protected ImovelRuralFacade imovelRuralServiceFacade;
	
	@EJB
	protected LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	public Integer countConsultaPrincipal(ImovelRuralDTO dadosParaBusca, String denominacao,
			String imoveisListadosPorPontos)  {

		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT COUNT(DISTINCT i.ide_imovel) ");

		if(TipoCadastroEnum.PCT.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
			lSql.append(getJoinsConsultaImovelRuralPCT());
		}else {
			lSql.append(getJoinsConsultaImovelRural());
		}

		lSql.append(getFiltrosConsultaImovelRural(dadosParaBusca, denominacao, imoveisListadosPorPontos));

		return imovelRuralDAO.countNativeQuery(lSql.toString()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Imovel> listarConsultaPrincipal(ImovelRuralDTO dadosParaBusca, String denominacao,
			String imoveisListadosPorPontos, int first, int pageSize)  {

		StringBuilder lSql = new StringBuilder();

		lSql.append("SELECT DISTINCT " + "i.ide_imovel, " + "ir.des_denominacao, " + "ir.status_cadastro, ")
	
		.append("ir.sts_certificado, " + "ir.prazo_validade, " + "m.ide_municipio, " + "m.nom_municipio, ")
		
		.append("e.ide_estado, " + "pess.ide_pessoa as ide_proprietario, " + "pessF.nom_pessoa as nom_pessoa_fisica, "
				+ "pessJ.nom_razao_social as razao_pessoa_juridica, " + "re.ide_reserva_legal, "
				+ "sr.ide_status_reserva_legal, " + "sr.dsc_status, " + "ir.dtc_finalizacao, "
				+ "lg.ide_localizacao_geografica, " + "irs.url_recibo_inscricao, " + "irs.ind_sincronia, "
				+ "irs.num_protocolo, " + "irs.cod_retorno_sincronia, " + "irs.num_sicar, "
				+ "ir.ide_tipo_cadastro_imovel_rural, " + "ir.ide_requerente_cadastro, " + "ir.ind_suspensao," + "ir.ind_bloqueio_limite,")
		.append("scir.num_ordenacao");

		if(TipoCadastroEnum.PCT.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
			lSql.append(getJoinsConsultaImovelRuralPCT());
		}else {
			lSql.append(getJoinsConsultaImovelRural());
		}


		
		lSql.append(getFiltrosConsultaImovelRural(dadosParaBusca, denominacao, imoveisListadosPorPontos));

		lSql.append(" ORDER BY "
				+ "scir.num_ordenacao,"
				+ "i.ide_imovel DESC OFFSET " + first + " limit " + pageSize + ";");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
		System.out.println(lSql);

		List<Imovel> listaImoveis = new ArrayList<Imovel>();

		for (Object[] resultElement : (List<Object[]>) lQuery.getResultList()) {
			listaImoveis.add(new Imovel(resultElement));
		}

		return listaImoveis;
	}

	private StringBuilder getJoinsConsultaImovelRural() {
		return new StringBuilder(" FROM imovel i " + "INNER JOIN endereco en ON i.ide_endereco=en.ide_endereco "
				+ "INNER JOIN logradouro l ON en.ide_logradouro=l.ide_logradouro "
				+ "INNER JOIN municipio m ON l.ide_municipio=m.ide_municipio "
				+ "INNER JOIN estado e ON m.ide_estado=e.ide_estado "
				+ "INNER JOIN tipo_imovel ti ON i.ide_tipo_imovel=ti.ide_tipo_imovel "
				+ "INNER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel AND pi.ind_excluido = false) "
				+ "INNER JOIN pessoa p ON pi.ide_pessoa=p.ide_pessoa "
				+ "LEFT JOIN representante_legal rl on ide_pessoa_juridica =  p.ide_pessoa AND rl.ind_excluido=FALSE "
				+ "INNER JOIN imovel_rural ir ON i.ide_imovel=ir.ide_imovel_rural "
				+ "INNER JOIN status_cadastro_imovel_rural scir  ON scir.ide_status_cadastro =ir.status_cadastro "
				+ "LEFT JOIN localizacao_geografica lg ON ir.ide_localizacao_geografica = lg.ide_localizacao_geografica "
				+ "LEFT JOIN reserva_legal re ON (ir.ide_imovel_rural = re.ide_imovel_rural) "
				+ "LEFT JOIN status_reserva_legal sr ON (re.ide_status = sr.ide_status_reserva_legal) "
				+ "LEFT JOIN imovel_rural_sicar irs ON (ir.ide_imovel_rural = irs.ide_imovel_rural) "
				+ "LEFT JOIN requerimento_imovel ri ON i.ide_imovel=ri.ide_imovel "
				+ "LEFT JOIN requerimento r ON ri.ide_requerimento=r.ide_requerimento "
				+ "LEFT JOIN certificado c ON c.ide_requerimento=r.ide_requerimento "
				+ "LEFT JOIN contrato_convenio_cefir ccc on ccc.ide_contrato_convenio_cefir = ir.ide_contrato_convenio "
				+ "LEFT JOIN procurador_representante pr on pr.ide_pessoa_juridica = ccc.ide_pessoa_juridica "
				+ "LEFT JOIN pessoa pess on ir.ide_requerente_cadastro = pess.ide_pessoa "
				+ "LEFT JOIN pessoa_fisica pessF on ir.ide_requerente_cadastro = pessF.ide_pessoa_fisica "
				+ "LEFT JOIN pessoa_juridica pessJ on ir.ide_requerente_cadastro = pessJ.ide_pessoa_juridica "
				+ "LEFT JOIN responsavel_imovel_rural rir ON (ir.ide_imovel_rural = rir.ide_imovel_rural) "

				);
	}

	private StringBuilder getJoinsConsultaImovelRuralPCT() {
		return new StringBuilder(" FROM imovel i " + "INNER JOIN endereco en ON i.ide_endereco=en.ide_endereco "
				+ "INNER JOIN logradouro l ON en.ide_logradouro=l.ide_logradouro "
				+ "INNER JOIN municipio m ON l.ide_municipio=m.ide_municipio "
				+ "INNER JOIN estado e ON m.ide_estado=e.ide_estado "
				+ "INNER JOIN tipo_imovel ti ON i.ide_tipo_imovel=ti.ide_tipo_imovel "
				+ "INNER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel AND pi.ind_excluido = false) "
				+ "INNER JOIN pessoa p ON pi.ide_pessoa=p.ide_pessoa "
				+ "LEFT JOIN representante_legal rl on ide_pessoa_juridica =  p.ide_pessoa AND rl.ind_excluido=FALSE "
				+ "INNER JOIN imovel_rural ir ON i.ide_imovel=ir.ide_imovel_rural "
				+ "INNER JOIN status_cadastro_imovel_rural scir  ON scir.ide_status_cadastro =ir.status_cadastro "
				+ "LEFT JOIN localizacao_geografica lg ON ir.ide_localizacao_geografica = lg.ide_localizacao_geografica "
				+ "LEFT JOIN reserva_legal re ON (ir.ide_imovel_rural = re.ide_imovel_rural) "
				+ "LEFT JOIN status_reserva_legal sr ON (re.ide_status = sr.ide_status_reserva_legal) "
				+ "LEFT JOIN imovel_rural_sicar irs ON (ir.ide_imovel_rural = irs.ide_imovel_rural) "
				+ "LEFT JOIN requerimento_imovel ri ON i.ide_imovel=ri.ide_imovel "
				+ "LEFT JOIN requerimento r ON ri.ide_requerimento=r.ide_requerimento "
				+ "LEFT JOIN certificado c ON c.ide_requerimento=r.ide_requerimento "
				+ "LEFT JOIN contrato_convenio_cefir ccc on ccc.ide_contrato_convenio_cefir = ir.ide_contrato_convenio "
				+ "LEFT JOIN procurador_representante pr on pr.ide_pessoa_juridica = ccc.ide_pessoa_juridica "
				+ "LEFT JOIN pessoa pess on ir.ide_requerente_cadastro = pess.ide_pessoa "
				+ "LEFT JOIN pessoa_fisica pessF on ir.ide_requerente_cadastro = pessF.ide_pessoa_fisica "
				+ "LEFT JOIN pessoa_juridica pessJ on ir.ide_requerente_cadastro = pessJ.ide_pessoa_juridica "
				+ "LEFT JOIN pct_imovel_rural pir on pir.ide_imovel_rural = ir.ide_imovel_rural "
				+ "LEFT JOIN pessoa_fisica_contrato_convenio pfcc on pfcc.ide_contrato_convenio = pir.ide_contrato_convenio ");
	}
	
	private StringBuilder getFiltrosConsultaImovelRural(ImovelRuralDTO dadosParaBusca, String denominacao,
			String imoveisListadosPorPontos) {
		StringBuilder lSql = new StringBuilder();

		lSql.append(" WHERE i.ind_excluido=FALSE ");
		if (!Util.isNullOuVazio(imoveisListadosPorPontos)) {
			lSql.append("AND i.ide_imovel in (" + imoveisListadosPorPontos + ") ");
		}
		
		 if (!Util.isNullOuVazio(denominacao)) {
			lSql.append(
					"AND trim(remover_acentuacao_uppercase(ir.des_denominacao)) = trim(remover_acentuacao_uppercase('"
							+ denominacao.replace("'", "''") + "')) ");
		}

		if (!Util.isNullOuVazio(dadosParaBusca.getNumCertificado())) {
			lSql.append(
					"AND c.num_token is not null AND ((c.ide_tipo_certificado=2 OR c.ide_tipo_certificado=3) AND ri.ind_excluido=false AND c.num_certificado ilike '%"
							+ dadosParaBusca.getNumCertificado() + "%')");
		}

		if (!Util.isNull(dadosParaBusca.getImovelCDA()) && dadosParaBusca.getImovelCDA()) {
			lSql.append("AND ir.ide_tipo_cadastro_imovel_rural =  "
					+ TipoCadastroImovelRuralEnum.IMOVEL_RURAL_CDA.getTipo() + " ");

		} else if (!Util.isNull(dadosParaBusca.getImovelBNDES()) && dadosParaBusca.getImovelBNDES()) {
			lSql.append("AND ir.ide_tipo_cadastro_imovel_rural =  "
					+ TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo() + " ");
			lSql.append("AND pr.ind_excluido = false AND pr.ide_procurador =  "
					+ ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica() + " ");

		} else {
			// Se o usuario logado for usuario externo, provavelmente ele é proprietário,
			// estará aqui, ou se a ATEND selecionar um requerente ele tbm deve estar ai
			if (!Util.isNull(dadosParaBusca.getProprietarioOuProcurador())
					&& !Util.isNull(dadosParaBusca.getProprietarioOuProcurador().getIdePessoa())) {
				
				if(TipoCadastroEnum.PCT.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
					lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural =  " + TipoCadastroImovelRuralEnum.PCT.getTipo() + " ");
					lSql.append("AND ((pi.ide_pessoa = "+ dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + " and pfcc.ide_pessoa_fisica = " + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + " and pfcc.ind_ativo)");
					lSql.append(" OR ir.ide_requerente_cadastro in (select ide_pessoa_juridica from procurador_representante where ind_excluido = false and ide_procurador =" + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() +") ");
					lSql.append(" OR ir.ide_requerente_cadastro in (select ide_pessoa_fisica from procurador_pessoa_fisica where ind_excluido = false and ide_procurador =" + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + ") ");
					lSql.append(" OR (pfcc.ide_pessoa_fisica = " + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + " and pfcc.ind_ativo) ");
				}else {
					if(TipoCadastroEnum.IMOVEL_RURAL.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
						lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural in  (" + TipoCadastroImovelRuralEnum.COMUM.getTipo() + ", " 
								+ TipoCadastroImovelRuralEnum.IMOVEL_RURAL_CDA.getTipo() + ", " + TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo() + ") ");
					}else if(TipoCadastroEnum.ASSENTAMENTOS.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
						lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural =  " + TipoCadastroImovelRuralEnum.ASSENTAMENTO_INCRA.getTipo() + " ");	
					}
					
					lSql.append("AND (pi.ide_pessoa = " + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa());
					lSql.append(" OR ir.ide_requerente_cadastro in (select ide_pessoa_juridica from procurador_representante where ind_excluido = false and ide_procurador ="
									+ dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + ") ");
					lSql.append(" OR ir.ide_requerente_cadastro in (select ide_pessoa_fisica from procurador_pessoa_fisica where ind_excluido = false and ide_procurador ="
									+ dadosParaBusca.getProprietarioOuProcurador().getIdePessoa() + ") ");
				}
				
				// Ajuste Consulta
				lSql.append("or ir.ide_requerente_cadastro = "
						+ dadosParaBusca.getProprietarioOuProcurador().getIdePessoa());

				// Essa é a pessoa que está solicitando a consulta, se essa pessoa for usuario
				// externo o getSolicitante será nulo
				if (!Util.isNull(dadosParaBusca.getSolicitante())
						&& !Util.isNull(dadosParaBusca.getSolicitante().getIdePessoa())) {
					lSql.append(" OR pi.ide_pessoa = " + dadosParaBusca.getSolicitante().getIdePessoa());
				}

				lSql.append(" OR rl.ide_pessoa_fisica = " + dadosParaBusca.getSolicitante().getIdePessoa() + "  ");
				lSql.append(" OR rl.ide_pessoa_fisica = " + dadosParaBusca.getProprietarioOuProcurador().getIdePessoa()
						+ " ) ");
				
			}else {
				if(TipoCadastroEnum.PCT.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
					lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural =  " + TipoCadastroImovelRuralEnum.PCT.getTipo() + " ");
				}else if(TipoCadastroEnum.ASSENTAMENTOS.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
					lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural =  " + TipoCadastroImovelRuralEnum.ASSENTAMENTO_INCRA.getTipo() + " ");	
				}else if(TipoCadastroEnum.IMOVEL_RURAL.getTipo().equalsIgnoreCase(dadosParaBusca.getTipoCadastro())) {
					lSql.append(" AND ir.ide_tipo_cadastro_imovel_rural =  " + TipoCadastroImovelRuralEnum.COMUM.getTipo() + " ");
				}
			}

			if (!Util.isNullOuVazio(dadosParaBusca.getSolicitante()) && !Util.isNullOuVazio(dadosParaBusca.getSolicitante().getIdePessoa())) {
				lSql.append("AND (pi.ide_pessoa = " + dadosParaBusca.getSolicitante().getIdePessoa());
				lSql.append(") ");
			}
		}

		if (!Util.isNull(dadosParaBusca.getMunicipio())
				&& !Util.isNull(dadosParaBusca.getMunicipio().getIdeMunicipio())) {
			lSql.append("AND m.ide_municipio = " + dadosParaBusca.getMunicipio().getIdeMunicipio() + " ");
		}

		if (!Util.isNull(dadosParaBusca.getStatusReservaLegal())
				&& !Util.isNull(dadosParaBusca.getStatusReservaLegal().getIdeStatusReservaLegal())) {
			lSql.append("AND sr.ide_status_reserva_legal = "
					+ dadosParaBusca.getStatusReservaLegal().getIdeStatusReservaLegal() + " ");
		}

		if (!Util.isNullOuVazio(dadosParaBusca.getImoveisAValidar())) {
			if (dadosParaBusca.getImoveisAValidar().equalsIgnoreCase("RegPendente")) {
				lSql.append("AND (ir.status_cadastro = 0 or ir.status_cadastro is null) ");
				lSql.append("AND (ir.ind_suspensao = 'false' or ir.ind_suspensao is null) ");
			}

			if (dadosParaBusca.getImoveisAValidar().equalsIgnoreCase("Registrado")) {
				lSql.append("AND ir.status_cadastro = 1 ");
				lSql.append("AND (ir.ind_suspensao = 'false' or ir.ind_suspensao is null) ");
			}

			if (dadosParaBusca.getImoveisAValidar().equalsIgnoreCase("CadPendente")) {
				lSql.append("AND ir.status_cadastro = 2 ");
				lSql.append("AND (ir.ind_suspensao = 'false' or ir.ind_suspensao is null) ");
			}

			if (dadosParaBusca.getImoveisAValidar().equalsIgnoreCase("Cadastrado")) {
				lSql.append("AND ir.status_cadastro = 3 ");
				lSql.append("AND (ir.ind_suspensao = 'false' or ir.ind_suspensao is null) ");
			} else {
				lSql.append(" ");
			}
		} else if (!Util.isNullOuVazio(dadosParaBusca.getIndSuspensao()) && dadosParaBusca.getIndSuspensao()) {
			lSql.append("AND ir.ind_suspensao = 'true' ");
		}

		if (!Util.isNull(dadosParaBusca.getBloqueioAsv()) && dadosParaBusca.getBloqueioAsv() == true) {
			lSql.append("AND ir.ind_permissao_asv = 'false' ");
		}

		if (!Util.isNull(dadosParaBusca.getValArea())) {
			lSql.append("AND ir.val_area = " + dadosParaBusca.getValArea() + " ");
		}

		// Redmine 8381 - danilos.santos
		if (!Util.isNull(dadosParaBusca.getStsCertificado())) {
			lSql.append("AND ir.sts_certificado = " + dadosParaBusca.getStsCertificado() + " ");
		
		}if(!Util.isNullOuVazio(dadosParaBusca.getNumSicar())) {
			lSql.append(" AND irs.num_sicar = '" + dadosParaBusca.getNumSicar() + "' ");
		
		}	
		
		if(!Util.isNullOuVazio(dadosParaBusca.getNumeroMatricula())) {
			lSql.append(" AND ir.num_matricula like '%" + dadosParaBusca.getNumeroMatricula() + "%' ");
		}
				
		if(!Util.isNull(dadosParaBusca.getResponsavelTecnico())) {
			
			lSql.append(" AND rir.ide_pessoa_fisica = " + dadosParaBusca.getResponsavelTecnico().getPessoaFisica().getIdePessoaFisica());
			lSql.append(" AND rir.ind_excluido IS FALSE");
		}
		
		
		 if(!Util.isNull(dadosParaBusca.getProcurador())) {
			 lSql.append("  AND pi.ide_pessoa=" + dadosParaBusca.getProcurador().getPessoaFisica().getIdePessoaFisica());
		}
		System.out.println(lSql);	
		return lSql;
	}

	@Override
	public List<ImovelRural> listarImovelRuralByProprietario(Pessoa proprietario, int first, int pageSize)
			 {
		DetachedCriteria criteria = getCriteria();
		criteria.add(Restrictions.eq("pessoa.idePessoa", proprietario.getIdePessoa()));
		criteria.addOrder(Order.desc("imovelRural.ideImovelRural"));
		return imovelRuralDAO.listarPorCriteriaDemanda(criteria, first, pageSize);

	}

	@SuppressWarnings("unchecked")
	public List<ImovelRural> listarImoveisPorProprietarioCompensacao(Pessoa proprietario)  {
		List<Object[]> listaObj = null;

		StringBuilder lSql = new StringBuilder();
		lSql.append("select ir.ide_imovel_rural, ");
		lSql.append("ir.des_denominacao, ");
		lSql.append("ir.status_cadastro, ");
		lSql.append("ir.ide_localizacao_geografica, ");
		lSql.append("rl.ide_reserva_legal, ");
		lSql.append("rl.ide_tipo_arl, ");
		lSql.append("irs.num_sicar ");
		lSql.append("from imovel_rural ir ");
		lSql.append("inner join reserva_legal rl on ir.ide_imovel_rural = rl.ide_imovel_rural ");
		lSql.append("inner join imovel i on ir.ide_imovel_rural = i.ide_imovel ");
		lSql.append("inner join pessoa_imovel pi on i.ide_imovel = pi.ide_imovel ");
		lSql.append("left join imovel_rural_sicar irs on ir.ide_imovel_rural = irs.ide_imovel_rural ");
		lSql.append("where i.ind_excluido=false ");
		lSql.append("and pi.ide_pessoa= " + proprietario.getIdePessoa() + " ");
		lSql.append("and ir.status_cadastro = 3 ");
		lSql.append("order by ir.ide_imovel_rural ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		listaObj = lQuery.getResultList();

		List<ImovelRural> listaImoveis = new ArrayList<ImovelRural>();

		for (Object[] resultElement : listaObj) {
			ImovelRural imovel = new ImovelRural((Integer) resultElement[0]);
			imovel.setDesDenominacao((String) resultElement[1]);
			imovel.setStatusCadastro((Integer) resultElement[2]);
			imovel.setIdeLocalizacaoGeografica(new LocalizacaoGeografica((Integer) resultElement[3]));
			imovel.setReservaLegal(new ReservaLegal((Integer) resultElement[4]));
			imovel.getReservaLegal().setIdeTipoArl(new TipoArl((Integer) resultElement[5]));
			imovel.setImovelRuralSicar(new ImovelRuralSicar());
			if (!Util.isNullOuVazio(resultElement[6]))
				imovel.getImovelRuralSicar().setNumSicar((String) resultElement[6]);

			listaImoveis.add(imovel);
		}
		return listaImoveis;
	}

	@SuppressWarnings("unchecked")
	public List<Imovel> listarImoveisRuraisComInformacoesIguais(ImovelRural imovelRural) {
		List<Object[]> listaObj = null;

		StringBuilder lSql = new StringBuilder();
		lSql.append(
				"SELECT DISTINCT i.ide_imovel, ir.des_denominacao, ir.status_cadastro, ir.sts_certificado, ir.prazo_validade, m.ide_municipio, m.nom_municipio, e.ide_estado,");

		lSql.append(
				"(select pess.ide_pessoa from pessoa pess WHERE ir.ide_requerente_cadastro = pess.ide_pessoa ) as ide_proprietario, ");

		lSql.append("(select pessF.nom_pessoa from pessoa_fisica pessF ");
		lSql.append("WHERE ir.ide_requerente_cadastro = pessF.ide_pessoa_fisica) as nom_pessoa_fisica, ");
		lSql.append("(select pessJ.nom_razao_social from pessoa_juridica pessJ ");
		lSql.append("WHERE ir.ide_requerente_cadastro = pessJ.ide_pessoa_juridica) as razao_pessoa_juridica, ");

		lSql.append("re.ide_reserva_legal, ");
		lSql.append("sr.ide_status_reserva_legal, ");
		lSql.append("sr.dsc_status, ");
		lSql.append("ir.dtc_finalizacao, ");
		lSql.append("lg.ide_localizacao_geografica, ");
		lSql.append("irs.url_recibo_inscricao, ");
		lSql.append("irs.ind_sincronia, ");
		lSql.append("irs.num_protocolo, ");
		lSql.append("irs.cod_retorno_sincronia, ");
		lSql.append("irs.num_sicar, ");
		lSql.append("ir.ide_tipo_cadastro_imovel_rural, ");
		lSql.append("ir.ide_requerente_cadastro, ");
		lSql.append("ir.ind_suspensao, ");
		lSql.append("ir.ind_bloqueio_limite ");
		lSql.append("FROM imovel i ");
		lSql.append("LEFT OUTER JOIN endereco en ON i.ide_endereco=en.ide_endereco ");
		lSql.append("LEFT OUTER JOIN logradouro l ON en.ide_logradouro=l.ide_logradouro ");
		lSql.append("LEFT OUTER JOIN municipio m ON l.ide_municipio=m.ide_municipio ");
		lSql.append("LEFT OUTER JOIN estado e ON m.ide_estado=e.ide_estado ");
		lSql.append("INNER JOIN tipo_imovel ti ON i.ide_tipo_imovel=ti.ide_tipo_imovel ");
		lSql.append("INNER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel AND pi.ind_excluido = false) ");
		lSql.append("INNER JOIN pessoa p ON pi.ide_pessoa=p.ide_pessoa ");
		lSql.append(
				"LEFT JOIN representante_legal rl on ide_pessoa_juridica =  p.ide_pessoa AND rl.ind_excluido=FALSE ");
		lSql.append("INNER JOIN imovel_rural ir ON i.ide_imovel=ir.ide_imovel_rural ");
		lSql.append(
				"LEFT OUTER JOIN localizacao_geografica lg ON ir.ide_localizacao_geografica = lg.ide_localizacao_geografica ");
		lSql.append("LEFT OUTER JOIN reserva_legal re ON (ir.ide_imovel_rural = re.ide_imovel_rural) ");
		lSql.append("LEFT OUTER JOIN status_reserva_legal sr ON (re.ide_status = sr.ide_status_reserva_legal) ");
		lSql.append("LEFT OUTER JOIN imovel_rural_sicar irs ON (ir.ide_imovel_rural = irs.ide_imovel_rural) ");
		lSql.append("LEFT JOIN requerimento_imovel ri ON i.ide_imovel=ri.ide_imovel ");
		lSql.append("LEFT JOIN requerimento r ON ri.ide_requerimento=r.ide_requerimento ");
		lSql.append("LEFT JOIN certificado c ON c.ide_requerimento=r.ide_requerimento ");
		lSql.append("WHERE i.ind_excluido=FALSE ");
		lSql.append("AND (pi.ide_pessoa = " + imovelRural.getIdeRequerenteCadastro().getIdePessoa()
				+ " or rl.ide_pessoa_fisica = null or rl.ide_pessoa_fisica = "
				+ imovelRural.getIdeRequerenteCadastro().getIdePessoa() + " or ir.ide_requerente_cadastro = "
				+ imovelRural.getIdeRequerenteCadastro().getIdePessoa() + ") ");
		lSql.append("AND l.ide_municipio = "
				+ imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getIdeMunicipio()
				+ " ");
		lSql.append("AND ir.ide_imovel_rural <> " + imovelRural.getIdeImovelRural() + " ");
		
		if(ContextoUtil.getContexto().isPCT()) {
			final Integer TIPO_CADASTRO_PCT = 5;
			lSql.append("AND ir.ide_tipo_cadastro_imovel_rural =" + TIPO_CADASTRO_PCT + " ");
		}

		lSql.append("ORDER BY i.ide_imovel DESC ");

		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		listaObj = lQuery.getResultList();

		List<Imovel> listaImoveis = new ArrayList<Imovel>();

		for (Object[] resultElement : listaObj) {
			listaImoveis.add(new Imovel(resultElement));
		}
		
		return listaImoveis;
	}

	public Integer qtdImoveisPendentesPorRequerente(Integer idePessoa) {
		BigInteger qtdRegistros;

		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT COUNT(i.ide_imovel) ");
		lSql.append("FROM imovel i ");
		lSql.append("INNER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel AND pi.ind_excluido = false) ");
		lSql.append("INNER JOIN pessoa p ON pi.ide_pessoa=p.ide_pessoa ");
		lSql.append("LEFT JOIN representante_legal rl ON ide_pessoa_juridica = p.ide_pessoa ");
		lSql.append("INNER JOIN imovel_rural ir ON i.ide_imovel=ir.ide_imovel_rural ");
		lSql.append("WHERE i.ind_excluido=FALSE  ");
		lSql.append("AND (pi.ide_pessoa = " + idePessoa + " or rl.ide_pessoa_fisica = null or rl.ide_pessoa_fisica = "
				+ idePessoa + " ) ");
		lSql.append("AND ir.status_cadastro = 0 ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		qtdRegistros = (BigInteger) lQuery.getSingleResult();

		return qtdRegistros.intValue();
	}

	private DetachedCriteria getCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class, "imovelRural");
		criteria.createAlias("imovelRural.imovel", "imovel", JoinType.INNER_JOIN);
		criteria.createAlias("imovel.ideTipoImovel", "tipoImovel", JoinType.INNER_JOIN);
		criteria.createAlias("imovelRural.reservaLegal", "reservaLegal", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovelRural.supressaoVegetacao", "supressaoVegetacaoNastiva", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovel.pessoaImovelCollection", "pessoaImovel", JoinType.INNER_JOIN,
				Restrictions.eq("pessoaImovel.indExcluido", false));
		criteria.createAlias("pessoaImovel.idePessoa", "pessoa", JoinType.INNER_JOIN);
		criteria.createAlias("imovel.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideMunicipioCartorio", "municipioCartorio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideMunicipioCartorio.ideEstado", "estadoCartorio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideTipoLogradouro", "tipoLogradouro", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("imovelRural.documentoImovelRuralPosse", "DIRP", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("DIRP.ideEnderecoDeclarante", "DIRP_ED", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("DIRP_ED.ideLogradouro", "DIRP_ED_L", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("DIRP_ED_L.ideBairro", "DIRP_ED_L_B", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("imovel.indExcluido", false));

		return criteria;
	}

	public Integer countListarImovelByProprietario(Pessoa proprietario) {
		DetachedCriteria criteria = getCriteria();
		criteria.add(Restrictions.eq("pessoa.idePessoa", proprietario.getIdePessoa()));
		return imovelRuralDAO.count(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarImovelPorId(Integer ideImovelRural) {

		DetachedCriteria criteria = getCriteria();
		criteria.add(Restrictions.eq("ideImovelRural", ideImovelRural));

		ImovelRural imovelRural = imovelRuralDAO.buscarPorCriteria(criteria);

		if (!Util.isNullOuVazio(imovelRural)) {
			Hibernate.initialize(imovelRural.getIdeContratoConvenioCefir());

			if (!Util.isNullOuVazio(imovelRural.getImovel().getPessoaImovelCollection())) {
				List<PessoaImovel> listPessoaImovel = new ArrayList<PessoaImovel>();

				//Hibernate.initialize(imovelRural.getImovel().getPessoaImovelCollection());
				imovelRural.getImovel().setPessoaImovelCollection(filtrarPessoasPorImovel(imovelRural.getImovel())); 


				for (PessoaImovel pessoaImovel : imovelRural.getImovel().getPessoaImovelCollection()) {
					if (!pessoaImovel.getIndExcluido())
						listPessoaImovel.add(pessoaImovel);
				}

				imovelRural.getImovel().setPessoaImovelCollection(listPessoaImovel);
			}

			if (!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro())) {
				if (!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica())) {

					if (!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica())) {
						/*
						 * Hibernate.initialize(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica
						 * () .getRepresentanteLegalCollection());
						 */
						
						imovelRural.getIdeRequerenteCadastro().getPessoaJuridica().setRepresentanteLegalCollection(
								representanteLegalService.getListaRepresentanteLegalByPessoa(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica()));

						Collection<RepresentanteLegal> repCollection = new ArrayList<RepresentanteLegal>();

						repCollection.addAll(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica()
								.getRepresentanteLegalCollection());

						for (RepresentanteLegal representante : repCollection) {
							if (!representante.getIndExcluido()) {
								if(Util.isLazyInitExcepOuNull(representante.getIdePessoaFisica())) {
									Hibernate.initialize(representante.getIdePessoaFisica());
								}
							}else {
								imovelRural.getIdeRequerenteCadastro().getPessoaJuridica()
										.getRepresentanteLegalCollection().remove(representante);
							}	
						}
					}
				}
			}
		}

		return imovelRural;
	}

	
	public Collection<PessoaImovel> filtrarPessoasPorImovel(Imovel imovel)  {
		DetachedCriteria criteria = getCriteria(imovel);
		 		
		return pessoaImovelDAO.listarPorCriteria(criteria); 
	}

	private DetachedCriteria getCriteria(Imovel imovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class, "pessoaDoImovel");
		criteria.createAlias("pessoaDoImovel.idePessoa", "pessoa");
		criteria.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pj.ideNaturezaJuridica", "naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoaDoImovel.ideTipoVinculoImovel", "tipoVinculo", JoinType.LEFT_OUTER_JOIN);
		if(Util.isNullOuVazio(imovel)){
			criteria.add(Restrictions.eq("pessoaDoImovel.ideImovel.ideImovel",  null));
		}else{
			criteria.add(Restrictions.eq("pessoaDoImovel.ideImovel.ideImovel",  imovel.getIdeImovel()));
		}
		criteria.add(Restrictions.eq("pessoaDoImovel.indExcluido", false));
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Imovel> listarPorRequerimento(Integer ideRequerimento)  {
		List<Object[]> listaObj = null;

		StringBuilder lSql = new StringBuilder();
		lSql.append(
				"SELECT DISTINCT ir.ide_imovel_rural, ir.num_itr, ir.des_denominacao, ir.status_cadastro, m.ide_municipio, m.nom_municipio, e.ide_estado,");
		lSql.append(
				"(select pess.ide_pessoa from pessoa pess inner join pessoa_imovel pessi ON pessi.ide_pessoa = pess.ide_pessoa AND pi.ide_imovel = pessi.ide_imovel ");
		lSql.append("order by pessi.ide_tipo_vinculo_imovel asc limit 1) as ide_proprietario, ");
		lSql.append("(select pessF.nom_pessoa from pessoa_fisica pessF ");
		lSql.append(
				"inner join pessoa_imovel pessi ON pessi.ide_pessoa = pessF.ide_pessoa_fisica AND pi.ide_imovel = pessi.ide_imovel ");
		lSql.append("order by pessi.ide_tipo_vinculo_imovel asc limit 1) as nom_pessoa_fisica, ");
		lSql.append("(select pessJ.nom_razao_social from pessoa_juridica pessJ ");
		lSql.append(
				"inner join pessoa_imovel pessi ON pessi.ide_pessoa = pessJ.ide_pessoa_juridica AND pi.ide_imovel = pessi.ide_imovel ");
		lSql.append("order by pessi.ide_tipo_vinculo_imovel asc limit 1) as razao_pessoa_juridica, ");
		lSql.append("rl.ide_reserva_legal, ");
		lSql.append("sr.ide_status_reserva_legal, ");
		lSql.append("sr.dsc_status, ");
		lSql.append("rl.ide_localizacao_geografica, ");
		lSql.append("ir.ind_permissao_asv, ");
		lSql.append("ir.ind_suspensao, ");
		lSql.append("ir.ind_bloqueio_limite ");
		lSql.append("FROM imovel i ");
		lSql.append("INNER JOIN imovel_rural ir on (i.ide_imovel = ir.ide_imovel_rural) ");
		lSql.append("INNER JOIN pessoa_imovel pi on (i.ide_imovel = pi.ide_imovel AND pi.ind_excluido = false) ");
		lSql.append("INNER JOIN imovel_empreendimento ie on (ir.ide_imovel_rural = ie.ide_imovel) ");
		lSql.append("INNER JOIN empreendimento_requerimento er on (ie.ide_empreendimento = er.ide_empreendimento) ");
		lSql.append("INNER JOIN requerimento r on (er.ide_requerimento = r.ide_requerimento) ");
		lSql.append("INNER JOIN reserva_legal rl ON (ir.ide_imovel_rural = rl.ide_imovel_rural) ");
		lSql.append("INNER JOIN status_reserva_legal sr ON (rl.ide_status = sr.ide_status_reserva_legal) ");
		lSql.append("LEFT OUTER JOIN endereco en ON i.ide_endereco=en.ide_endereco ");
		lSql.append("LEFT OUTER JOIN logradouro l ON en.ide_logradouro=l.ide_logradouro ");
		lSql.append("LEFT OUTER JOIN municipio m ON l.ide_municipio=m.ide_municipio ");
		lSql.append("LEFT OUTER JOIN estado e ON m.ide_estado=e.ide_estado ");
		lSql.append("WHERE i.ind_excluido = false ");
		lSql.append("AND r.ind_excluido = false ");
		lSql.append("AND r.ide_requerimento = " + ideRequerimento);

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		listaObj = lQuery.getResultList();

		ArrayList<Imovel> listaImoveis = new ArrayList<Imovel>();

		for (Object[] resultElement : listaObj) {
			listaImoveis.add(montarImovel(resultElement));
		}
		return listaImoveis;
	}

	private Imovel montarImovel(Object[] resultElement) {
		Imovel imovel = new Imovel();
		imovel.setIdeImovel((Integer) resultElement[0]);
		imovel.setImovelRural(new ImovelRural((Integer) resultElement[0]));
		imovel.getImovelRural().setNumItr((String) resultElement[1]);
		imovel.getImovelRural().setDesDenominacao((String) resultElement[2]);
		imovel.getImovelRural().setStatusCadastro((Integer) resultElement[3]);
		imovel.setIdeEndereco(new Endereco());
		imovel.getIdeEndereco().setIdeLogradouro(new Logradouro());
		imovel.getIdeEndereco().getIdeLogradouro().setIdeMunicipio(new Municipio());
		imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().setIdeMunicipio((Integer) resultElement[4]);
		imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().setNomMunicipio((String) resultElement[5]);
		imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
				.setIdeEstado(new Estado((Integer) resultElement[6]));
		imovel.setProprietario(new Pessoa((Integer) resultElement[7]));
		String nome = (String) resultElement[8];
		if (!Util.isNullOuVazio(nome))
			imovel.getProprietario().setPessoaFisica(new PessoaFisica((Integer) resultElement[7], nome));
		String razao = (String) resultElement[9];
		if (!Util.isNullOuVazio(razao)) {
			imovel.getProprietario().setPessoaFisica(new PessoaFisica((Integer) resultElement[7], razao));
			imovel.getProprietario().setPessoaJuridica(new PessoaJuridica((Integer) resultElement[7], razao));
		}
		imovel.getImovelRural().setReservaLegal(new ReservaLegal((Integer) resultElement[10]));
		imovel.getImovelRural().getReservaLegal()
				.setIdeStatus(new StatusReservaLegal((Integer) resultElement[11], (String) resultElement[12]));
		imovel.getImovelRural().getReservaLegal()
				.setIdeLocalizacaoGeografica(new LocalizacaoGeografica((Integer) resultElement[13]));
		imovel.getImovelRural().setIndPermissaoASV((Boolean) resultElement[14]);
		imovel.getImovelRural().setIndSuspensao((Boolean) resultElement[15]);
		return imovel;
	}

	@SuppressWarnings("unchecked")
	private Integer retonarRegistroReservaLegalBloquioBy(Integer ideReservaLegal)  {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("FROM reserva_legal_bloqueio rlb ");
		sql.append("WHERE rlb.ide_reserva_legal = " + ideReservaLegal);

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());
		List<Object[]> resultList = lQuery.getResultList();
		for (Object object[] : resultList) {
			return (Integer) object[0];
		}
		return null;
	}

	public List<FiltroAuditoria> filtrarHistorico(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first,
			int pageSize, Integer... ides)  {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from (");
		sql.append(" select h.data_historico as dt_modificacao,");
		sql.append(
				" (pf.nom_pessoa || CASE WHEN area.nom_area IS NULL THEN '' ELSE ' - ' || area.nom_area END) as usuario,");
		sql.append(" c.desc_campo as campo,");
		sql.append(" (");
		sql.append("select vl.val_valor from hist_valor vl ");
		sql.append("join hist_historico hs on (vl.ide_historico = hs.ide_historico) ");
		sql.append("where vl.ide_registro = v.ide_registro ");
		sql.append("and vl.ide_campo = c.ide_campo ");
		sql.append("and hs.data_historico <= h.data_historico ");
		sql.append("order by hs.data_historico desc offset 1 limit 1 ");
		sql.append("	) as antigo, ");
		sql.append("	v.val_valor as novo, c.ide_campo, u.ide_pessoa_fisica, ");
		sql.append(" t.desc_tabela as tabela, v.ide_registro as registro ");
		sql.append(" from hist_valor v ");
		sql.append(" join hist_historico h on h.ide_historico = v.ide_historico ");
		sql.append(" join usuario u on u.ide_pessoa_fisica = h.ide_usuario ");
		sql.append(" join pessoa_fisica pf on u.ide_pessoa_fisica = pf.ide_pessoa_fisica ");
		sql.append(" left join funcionario func on func.ide_pessoa_fisica = pf.ide_pessoa_fisica ");
		sql.append(" left join area area on area.ide_area = func.ide_area ");
		sql.append(" join hist_campo c on c.ide_campo = v.ide_campo");
		sql.append(" join hist_tabela t on t.ide_tabela = c.ide_tabela");

		sql.append(" where 1=1 ");
		sql.append(" and t.ide_tabela in (2,22,21,25) ");
		sql.append(" and v.ide_registro in (" + ideImovelRural + ") ");
		if (!Util.isNullOuVazio(dataInicio)) {
			sql.append(" and cast(h.data_historico as date) >= cast('"
					+ new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "' as date) ");
		}
		if (!Util.isNullOuVazio(dataFim)) {
			sql.append(" and cast(h.data_historico as date) <= cast('"
					+ new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + "' as date) ");
		}

		if (!Util.isNullOuVazio(ideImovelRural) && !Util.isNullOuVazio(ideImovelRural.getIdeImovelRural())) {
			if (!Util.isNull(ideImovelRural.getReservaLegal())
					&& !Util.isNull(ideImovelRural.getReservaLegal().getIdeReservaLegal())) {
				String reservaLegal = selectUnionImovel(1,
						ideImovelRural.getReservaLegal().getIdeReservaLegal().toString(), dataInicio, dataFim);
				sql.append(reservaLegal);

				Integer ideReservaLegalBloqueio = retonarRegistroReservaLegalBloquioBy(
						ideImovelRural.getReservaLegal().getIdeReservaLegal());
				if (!Util.isNull(ideReservaLegalBloqueio)) {
					String reservaLegalBloqueio = selectUnionImovel(29, ideReservaLegalBloqueio.toString(), dataInicio,
							dataFim);
					sql.append(reservaLegalBloqueio);
				}
			}

			if (!Util.isNullOuVazio(ideImovelRural.getAppCollection())) {
				String apps = selectUnionImovel(9, ideImovelRural.getAppCollection().toString(), dataInicio, dataFim);
				sql.append(apps);
			}
			if (!Util.isNullOuVazio(ideImovelRural.getAreaProdutivaCollection())) {
				String aps = selectUnionImovel(12, ideImovelRural.getAreaProdutivaCollection().toString(), dataInicio,
						dataFim);
				sql.append(aps);
			}
			if (!Util.isNull(ideImovelRural.getVegetacaoNativa())
					&& !Util.isNull(ideImovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())) {
				String vegetacaoNativa = selectUnionImovel(7,
						ideImovelRural.getVegetacaoNativa().getIdeVegetacaoNativa().toString(), dataInicio, dataFim);
				sql.append(vegetacaoNativa);
			}
			if (ides != null && ides.length > 0 && ides[0] != null) {
				String imovelStatusModificacao = selectUnionImovel(25, String.valueOf(ides[0]), dataInicio, dataFim);
				sql.append(imovelStatusModificacao);
			}

			if (!Util.isNull(ideImovelRural.getIndSuspensao())) {
				List<HistoricoSuspensaoCadastro> list = historicoSuspensaoCadastroDAOImpl
						.obterListaHistoricoSuspensaoCadastroPorIdeImovelRural(ideImovelRural);
				StringBuilder hist = new StringBuilder();
				for (HistoricoSuspensaoCadastro histSuspCad : list) {
					if (hist.length() > 0)
						hist.append(", ");
					hist.append(histSuspCad.getIdeSuspensaoCadastro().toString());
				}
				String vegetacaoNativa = selectUnionImovel(30, hist.toString(), dataInicio, dataFim);
				sql.append(vegetacaoNativa);
			}
			
			if(!Util.isNullOuVazio(ideImovelRural.getIdePctImovelRural())) {
				String apps = selectUnionImovel(31, ideImovelRural.getIdePctImovelRural().toString(), dataInicio, dataFim);
				sql.append(apps);
			}
			
			Integer idImovelRuralDesbloqueio = this.imovelRuralServiceFacade.getIdImovelRuralDesbloqueio(ideImovelRural);
			if(!Util.isNullOuVazio(idImovelRuralDesbloqueio)) {
				String apps = selectUnionImovel(35, idImovelRuralDesbloqueio.toString(), dataInicio, dataFim);
				sql.append(apps);
			}
			

		}else {
			return null;
		}

		sql.append(" )T order by T.dt_modificacao desc, T.usuario, T.campo ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());

		lQuery.setFirstResult(first);
		if (pageSize > 0) {
			lQuery.setMaxResults(pageSize);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = lQuery.getResultList();
		List<FiltroAuditoria> listFiltro = new ArrayList<FiltroAuditoria>();
		for (Object object[] : resultList) {
			FiltroAuditoria filtro = new FiltroAuditoria();
			if (!Util.isNullOuVazio(object[0])) {
				filtro.setDataModificacao((Date) object[0]);
			}
			if (!Util.isNullOuVazio(object[1])) {
				Usuario usuario =  new Usuario(object[1].toString());
				filtro.setUsuarioAlteracao(usuario);
				filtro.setNomeUsuario(object[1].toString());
			}
			if (!Util.isNullOuVazio(object[2])) {
				HistCampo campo = new HistCampo();
				campo.setNomeCampo(object[2].toString());
				if (!Util.isNullOuVazio(object[7])) {
					HistTabela histTabela = new HistTabela();
					histTabela.setNomeTabela(object[7].toString());
					campo.setIdeTabela(histTabela);
				}
				filtro.setCampo(campo);
			}
			if (!Util.isNullOuVazio(object[3])) {
				if (!filtro.getCampo().getNomeCampo().equals("SHAPE")) {
					HistValor valor = new HistValor();
					valor.setValor(object[3].toString());
					filtro.setValorAntigo(valor);
				}
			} else {
				filtro.setValorAntigo(new HistValor());
			}
			if (!Util.isNullOuVazio(object[4])) {
				HistValor valor = new HistValor();
				valor.setValor(object[4].toString());
				filtro.setValorNovo(valor);
			} else {
				filtro.setValorNovo(new HistValor());
			}

			if (!Util.isNullOuVazio(object[8])) {
				filtro.getValorNovo().setIdeRegistro(Long.valueOf(object[8].toString()));
			}

			listFiltro.add(filtro);
		}
		return listFiltro;
	}

	private String selectUnionImovel(Integer ideTabela, String ideRegistro, Date dataInicio, Date dataFim) {
		StringBuilder sql = new StringBuilder();
		sql.append(" union ");
		sql.append(" select h.data_historico as dt_modificacao,");
		sql.append(" (pf.nom_pessoa || CASE WHEN area.nom_area IS NULL THEN '' ELSE ' - ' || area.nom_area END) as usuario,");
		sql.append(" c.desc_campo as campo,");
		sql.append(" (");
		sql.append("select vl.val_valor from hist_valor vl ");
		sql.append("join hist_historico hs on (vl.ide_historico = hs.ide_historico) ");
		sql.append("where vl.ide_registro = v.ide_registro ");
		sql.append("and vl.ide_campo = c.ide_campo ");
		sql.append("and hs.data_historico <= h.data_historico ");
		sql.append("order by hs.data_historico desc offset 1 limit 1 ");
		sql.append("	) as antigo, ");
		sql.append("	v.val_valor as novo, c.ide_campo, u.ide_pessoa_fisica, ");
		sql.append(" t.desc_tabela as tabela, v.ide_registro as registro ");
		sql.append(" from hist_valor v ");
		sql.append(" join hist_historico h on h.ide_historico = v.ide_historico ");
		sql.append(" join usuario u on u.ide_pessoa_fisica = h.ide_usuario ");
		sql.append(" join pessoa_fisica pf on u.ide_pessoa_fisica = pf.ide_pessoa_fisica ");
		sql.append(" left join funcionario func on func.ide_pessoa_fisica = pf.ide_pessoa_fisica ");
		sql.append(" left join area area on area.ide_area = func.ide_area ");
		sql.append(" join hist_campo c on c.ide_campo = v.ide_campo");
		sql.append(" join hist_tabela t on t.ide_tabela = c.ide_tabela");

		sql.append(" where 1=1 ");
		sql.append(" and t.ide_tabela in (" + ideTabela.toString() + ") ");
		if (ideRegistro != null) {
			sql.append(" and v.ide_registro in (" + ideRegistro.replaceAll("[^\\d^\\,]", "") + ") ");
		}
		if (!Util.isNullOuVazio(dataInicio)) {
			sql.append(" and cast(h.data_historico as date) >= cast('"
					+ new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "' as date) ");
		}
		if (!Util.isNullOuVazio(dataFim)) {
			sql.append(" and cast(h.data_historico as date) <= cast('"
					+ new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + "' as date) ");
		}
		return sql.toString();
	}

	public List<ImovelRural> imoveisEmCompensacaoDeReserva(Integer ideImovelRural) {

		String sql = "SELECT ic.ide_imovel_rural FROM sp_get_imovelcredito(" + ideImovelRural
				+ ", 'GEOMETRYCOLLECTION(EMPTY)') AS ic WHERE ic.ide_imovel_rural <> " + ideImovelRural
				+ " ORDER BY ic.ide_imovel_rural";

		@SuppressWarnings("rawtypes")
		List result;
		try {
			result = imovelRuralDAO.buscarPorNativeQuery(sql, null);

			List<ImovelRural> listImovelRural = new ArrayList<ImovelRural>();

			if (!Util.isNullOuVazio(result)) {
				for (Object ideImovel : result) {
					if (!Util.isNull(ideImovel)) {
						ImovelRural imovel = new ImovelRural();
						imovel.setIdeImovelRural((Integer) ideImovel);
						listImovelRural.add(imovel);
					}
				}
				return listImovelRural;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return null;
	}

	public void excluirSupressaoVegetacaoPorImovelRural(Integer ideImovelRural)  {
		String delSQL = "DELETE FROM supressao_vegetacao WHERE ide_imovel_rural = :ideImovelRural";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", ideImovelRural);
		imovelRuralDAO.executarNativeQuery(delSQL, params);
	}

	public void excluirImovelRuralTacPorImovelRural(Integer ideImovelRural)  {
		String delSQL = "DELETE FROM imovel_rural_tac WHERE ide_imovel_rural = :ideImovelRural";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", ideImovelRural);
		imovelRuralDAO.executarNativeQuery(delSQL, params);
	}

	public void excluirImovelRuralPradPorImovelRural(Integer ideImovelRural)  {
		String delSQL = "DELETE FROM imovel_rural_prad WHERE ide_imovel_rural = :ideImovelRural";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", ideImovelRural);
		imovelRuralDAO.executarNativeQuery(delSQL, params);
	}
	
	public Integer countImoveisCadastroIncompleto(ImovelRuralDTO dadosParaBusca) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("select distinct COUNT( i.ide_imovel)").append(" from imovel i ")
				.append("inner join pessoa_imovel pi on (i.ide_imovel = pi.ide_imovel	and pi.ind_excluido = false) ")
				.append("inner join pessoa p on	pi.ide_pessoa = p.ide_pessoa ")
				.append("left join representante_legal rl on ide_pessoa_juridica = p.ide_pessoa and rl.ind_excluido = false ")
				.append("inner join imovel_rural ir on i.ide_imovel = ir.ide_imovel_rural ")
				.append("left join reserva_legal re on (ir.ide_imovel_rural = re.ide_imovel_rural) ")
				.append("left join status_reserva_legal sr on (re.ide_status = sr.ide_status_reserva_legal) ")
				.append("left join imovel_rural_sicar irs on (ir.ide_imovel_rural = irs.ide_imovel_rural) ")
				.append("left join requerimento_imovel ri on i.ide_imovel = ri.ide_imovel ")
				.append("left join requerimento r on ri.ide_requerimento = r.ide_requerimento ")
				.append("left join certificado c on	c.ide_requerimento = r.ide_requerimento ")
				.append("left join contrato_convenio_cefir ccc on ccc.ide_contrato_convenio_cefir = ir.ide_contrato_convenio ")
				.append("left join procurador_representante pr on pr.ide_pessoa_juridica = ccc.ide_pessoa_juridica ")
				.append("left join pessoa pess on ir.ide_requerente_cadastro = pess.ide_pessoa ")
				.append("left join pessoa_fisica pessF on ir.ide_requerente_cadastro = pessF.ide_pessoa_fisica ")
				.append("left join pessoa_juridica pessJ on ir.ide_requerente_cadastro = pessJ.ide_pessoa_juridica ");
		lSql.append(getFiltrosConsultaImovelRural(dadosParaBusca, null, null));
		lSql.append(" AND ir.status_cadastro = 0 limit 1");
		return imovelRuralDAO.countNativeQuery(lSql.toString()).intValue();
		
	}

	public void callTransferenciaFunction(Integer ideImovelRural,Integer ideRequerenteAnterior,Integer ideRequerenteNovo ) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("select cast(fn_transferencia_titularidade_ir(")
		.append(ideImovelRural).append(",")
		.append(ideRequerenteAnterior).append(",")
		.append(ideRequerenteNovo).append(") as character varying)");
		imovelRuralDAO.executarFuncaoNativeQuery(lSql.toString(), null);
		
	}

}

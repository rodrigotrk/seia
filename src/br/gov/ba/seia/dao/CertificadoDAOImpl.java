package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogCertificado;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class CertificadoDAOImpl {

	@Inject
	IDAO<Certificado> certificadoDAO;

	@Inject
	IDAO<DeclaracaoParcialDae> declaracaoDAO;

	@Inject
	IDAO<CaepogCertificado> caepogCertificadoDAO;

	@Inject
	IDAO<Imovel> idaoDTO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Certificado certificado) {
		this.certificadoDAO.salvar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoParcialDae certificado) {
		this.declaracaoDAO.salvar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaepogCertificado(CaepogCertificado caepogCertificado) {
		caepogCertificadoDAO.salvar(caepogCertificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Certificado certificado) {
		this.certificadoDAO.atualizar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(DeclaracaoParcialDae certificado) {
		this.declaracaoDAO.atualizar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogCertificado getCertificadoByideCaepog(Caepog caepog) {

		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogCertificado.class)

				.createAlias("ideCaepog", "ideCaepog").createAlias("ideCertificado", "ideCertificado")
				.add(Restrictions.eq("ideCaepog.ideCaepog", caepog.getIdeCaepog()))

				.setProjection(Projections.projectionList()
						.add(Projections.property("ideCaepog.ideCaepog"), "ideCaepog.ideCaepog")
						.add(Projections.property("ideCertificado.ideCertificado"), "ideCertificado.ideCertificado"))

				.setResultTransformer(new AliasToNestedBeanResultTransformer(CaepogCertificado.class));

		return caepogCertificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificado(Integer ideAmbiental) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)

				.createAlias("ideAtoAmbiental", "ato")

				.add(Subqueries.propertyEq("ideCertificado",
						DetachedCriteria.forClass(Certificado.class).createAlias("ideAtoAmbiental", "ato")
								.setProjection(Projections.projectionList().add(Projections.max("ideCertificado")))
								.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class))
								.add(Restrictions.eq("ato.ideAtoAmbiental", ideAmbiental))))

				.setProjection(
						Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
								.add(Projections.property("numCertificado"), "numCertificado")
								.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
								.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
								.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"))

				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarById(Integer ideCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("ideOrgao", "orgao")
				.createAlias("ideAtoAmbiental", "ato");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")
						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
						.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		criteria.add(Restrictions.eq("this.ideCertificado", ideCertificado));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> carregarByIdRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("ideOrgao", "orgao")
				.createAlias("requerimento", "req").createAlias("ideAtoAmbiental", "ato", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")
						.add(Projections.property("numToken"), "numToken")

						.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")

						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
						.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		return this.certificadoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Certificado carregarByIdRequerimentoAndAto(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("ideOrgao", "orgao")
				.createAlias("requerimento", "req").createAlias("ideAtoAmbiental", "ato");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")
						.add(Projections.property("numToken"), "numToken")

						.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")

						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
						.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("ato.ideAtoAmbiental", AtoAmbientalEnum.LAC.getId()));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean validarToken(String token) {
		Certificado certificado = this.carregarByToken(token);
		return Util.isNull(certificado);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarByToken(String token) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("requerimento", "req",
				JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")
						.add(Projections.property("numToken"), "numToken")

						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
						.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		criteria.add(Restrictions.eq("this.numToken", token.toUpperCase()));
		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarByTokenDeclaracao(String token) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoParcialDae.class)
				.createAlias("requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideMemoriaCalculoDaeParcela", "parcela", JoinType.INNER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDeclaracaoParcialDae"), "ideDeclaracaoParcialDae")
				.add(Projections.property("numDeclaracaoParcialDae"), "numDeclaracaoParcialDae")
				.add(Projections.property("numToken"), "numToken")
				.add(Projections.property("parcela.ideMemoriaCalculoDaeParcela"),
						"ideMemoriaCalculoDaeParcela.ideMemoriaCalculoDaeParcela")
				.add(Projections.property("parcela.valor"), "ideMemoriaCalculoDaeParcela.valor")
				.add(Projections.property("parcela.numParcelaReferencia"),
						"ideMemoriaCalculoDaeParcela.numParcelaReferencia")
				.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
				.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoParcialDae.class));

		criteria.add(Restrictions.eq("this.numToken", token.toUpperCase()));
		return this.declaracaoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarInformacoesRequerimentoByCertificado(Integer ideCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)
				.createAlias("requerimento", "req",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAtoAmbiental", "ato",JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoCertificado", "tipo",JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoImovelCollection", "reqImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqImovel.imovel", "imovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovel.ideEndereco", "enderecoImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoImovel.ideLogradouro","logradouroImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroImovel.ideBairro","bairroImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroImovel.ideMunicipio","municipioImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipioImovel.ideEstado","estadoImovel",JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovel.imovelRural", "rural",JoinType.LEFT_OUTER_JOIN)
				.createAlias("rural.ideRequerenteCadastro", "ruralReqCadastro",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ruralReqCadastro.pessoaJuridica", "ruralpj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ruralReqCadastro.pessoaFisica", "ruralpf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoUnico", "reqUnico",JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqUnico.idePorte", "por",JoinType.LEFT_OUTER_JOIN)			
				.createAlias("req.ideOrgao", "orgao", JoinType.LEFT_OUTER_JOIN)
				
				//.createAlias("req.empreendimentoCollection", "emp",JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.empreendimentoRequerimentoCollection", "reqEmp",JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqEmp.ideEmpreendimento", "emp",JoinType.LEFT_OUTER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "endEmp",JoinType.LEFT_OUTER_JOIN)
				.createAlias("endEmp.ideTipoEndereco" , "tipoEnderecoEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("endEmp.ideEndereco", "enderecoEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoEmpreendimento.ideLogradouro", "logradouroEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroEmpreendimento.ideBairro", "bairroEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroEmpreendimento.ideMunicipio", "municipioEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipioEmpreendimento.ideEstado","estadoEmpreendimento",JoinType.LEFT_OUTER_JOIN)
			
				.createAlias("req.requerimentoPessoaCollection", "rpc",JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr",JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.pessoa", "p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);


			ProjectionList projecao = 
				Projections.projectionList()
					.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento")
					.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
					.add(Projections.property("req.dtcCriacao"), "requerimento.dtcCriacao")
					
					.add(Projections.property("por.idePorte"), "requerimento.requerimentoUnico.idePorte.idePorte")
					.add(Projections.property("por.nomPorte"), "requerimento.requerimentoUnico.idePorte.nomPorte")
					
					.add(Projections.property("orgao.ideOrgao"), "requerimento.ideOrgao.ideOrgao")
					.add(Projections.property("orgao.codOrgao"), "requerimento.ideOrgao.codOrgao")
					
					.add(Projections.property("imovel.ideImovel"), "requerimento.imovel.ideImovel")
					
					.add(Projections.property("rural.ideImovelRural"), "requerimento.imovel.imovelRural.ideImovelRural")
					.add(Projections.property("rural.ideRequerenteCadastro"), "requerimento.imovel.imovelRural.ideRequerenteCadastro")
					
					.add(Projections.property("ruralReqCadastro.idePessoa"), "requerimento.imovel.imovelRural.ideRequerenteCadastro.idePessoa")
					.add(Projections.property("ruralpf.nomPessoa"), "requerimento.imovel.imovelRural.ideRequerenteCadastro.pessoaFisica.nomPessoa")
					.add(Projections.property("ruralpf.numCpf"), "requerimento.imovel.imovelRural.ideRequerenteCadastro.pessoaFisica.numCpf")
					.add(Projections.property("ruralpj.nomRazaoSocial"), "requerimento.imovel.imovelRural.ideRequerenteCadastro.pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("ruralpj.numCnpj"), "requerimento.imovel.imovelRural.ideRequerenteCadastro.pessoaJuridica.numCnpj")
					
					
					.add(Projections.property("emp.ideEmpreendimento"), "requerimento.ultimoEmpreendimento.ideEmpreendimento")
					.add(Projections.property("emp.nomEmpreendimento"), "requerimento.ultimoEmpreendimento.nomEmpreendimento")

					.add(Projections.property("enderecoEmpreendimento.ideEndereco"),"requerimento.ultimoEmpreendimento.endereco.ideEndereco")
					.add(Projections.property("enderecoEmpreendimento.numEndereco"),"requerimento.ultimoEmpreendimento.endereco.numEndereco")
					.add(Projections.property("enderecoEmpreendimento.desComplemento"),"requerimento.ultimoEmpreendimento.endereco.desComplemento")
					
					.add(Projections.property("logradouroEmpreendimento.nomLogradouro"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.nomLogradouro")
					.add(Projections.property("logradouroEmpreendimento.numCep"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.numCep")
					
					.add(Projections.property("bairroEmpreendimento.ideBairro"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideBairro")
					.add(Projections.property("bairroEmpreendimento.nomBairro"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")
					
					.add(Projections.property("municipioEmpreendimento.ideMunicipio"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
					.add(Projections.property("municipioEmpreendimento.nomMunicipio"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
					
					.add(Projections.property("estadoEmpreendimento.ideEstado"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
					.add(Projections.property("estadoEmpreendimento.desSigla"),"requerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")
//					
					.add(Projections.property("p.idePessoa"), "requerimento.requerente.idePessoa")
					.add(Projections.property("pf.nomPessoa"), "requerimento.requerente.pessoaFisica.nomPessoa")
					.add(Projections.property("pf.numCpf"), "requerimento.requerente.pessoaFisica.numCpf")
					.add(Projections.property("pj.nomRazaoSocial"), "requerimento.requerente.pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("pj.numCnpj"), "requerimento.requerente.pessoaJuridica.numCnpj")
		
					.add(Projections.property("ideCertificado"), "ideCertificado")
					.add(Projections.property("numCertificado"), "numCertificado")
					.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
					.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
					
					.add(Projections.property("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado")
					
					.add(Projections.property("enderecoImovel.ideEndereco"),"requerimento.imovel.ideEndereco.ideEndereco")
					.add(Projections.property("enderecoImovel.numEndereco"),"requerimento.imovel.ideEndereco.numEndereco")
					.add(Projections.property("enderecoImovel.desComplemento"),"requerimento.imovel.ideEndereco.desComplemento")
					
					.add(Projections.property("logradouroImovel.nomLogradouro"),"requerimento.imovel.ideEndereco.ideLogradouro.nomLogradouro")
					.add(Projections.property("logradouroImovel.numCep"),"requerimento.imovel.ideEndereco.ideLogradouro.numCep")
					
					.add(Projections.property("bairroImovel.ideBairro"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.ideBairro")
					.add(Projections.property("bairroImovel.nomBairro"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.nomBairro")
					
					.add(Projections.property("municipioImovel.ideMunicipio"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
					.add(Projections.property("municipioImovel.nomMunicipio"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
					
					.add(Projections.property("estadoImovel.ideEstado"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
					.add(Projections.property("estadoImovel.desSigla"),"requerimento.imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla");
			
			criteria.setProjection(Projections.distinct(projecao));
			
			criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
			
			criteria.add(Restrictions.eq("this.ideCertificado", ideCertificado));

			criteria.add(Restrictions.eq("tipoEnderecoEmpreendimento.ideTipoEndereco", 4));
			
			criteria.add(Restrictions.or(
					Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()),
					Restrictions.isNull("tpr.ideTipoPessoaRequerimento")
			));
			
		
		return certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel carregarImovelByCertificado(Integer ideCertificado) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)
				.createAlias("requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAtoAmbiental", "ato", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoImovelCollection", "reqImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqImovel.imovel", "imovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovel.ideEndereco", "enderecoImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoImovel.ideLogradouro", "logradouroImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroImovel.ideBairro", "bairroImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouroImovel.ideMunicipio", "municipioImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipioImovel.ideEstado", "estadoImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovel.imovelRural", "rural", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rural.ideRequerenteCadastro", "ruralReqCadastro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ruralReqCadastro.pessoaJuridica", "ruralpj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ruralReqCadastro.pessoaFisica", "ruralpf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoUnico", "reqUnico", JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqUnico.idePorte", "por", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.ideOrgao", "orgao", JoinType.LEFT_OUTER_JOIN)

				// .createAlias("req.empreendimentoCollection", "emp",JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.empreendimentoRequerimentoCollection", "reqEmp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqEmp.ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)

				.createAlias("req.requerimentoPessoaCollection", "rpc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.pessoa", "p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "enderecoEmpreendimento", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoEmpreendimento.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoEmpreendimento.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN);

		ProjectionList projecao = Projections.projectionList().add(Projections.property("reqImovel.imovel.ideImovel"),
				"ideImovel")

		;

		criteria.setProjection(Projections.distinct(projecao));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class));

		criteria.add(Restrictions.eq("this.ideCertificado", ideCertificado));

		criteria.add(Restrictions.or(
				Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()),
				Restrictions.isNull("tpr.ideTipoPessoaRequerimento")));

		criteria.add(
				Restrictions.or(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()),
						Restrictions.isNull("tipoEndereco.ideTipoEndereco")));

		return idaoDTO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarInformacoesRequerimentoByDeclaracao(Integer ideCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoParcialDae.class)
				.createAlias("requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAtoAmbiental", "ato", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoImovelCollection", "reqImovel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoUnico", "reqUnico", JoinType.LEFT_OUTER_JOIN)
				.createAlias("reqUnico.idePorte", "por", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.ideOrgao", "orgao", JoinType.LEFT_OUTER_JOIN)

				.createAlias("req.requerimentoPessoaCollection", "rpc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.pessoa", "p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);

		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento")
				.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

				.add(Projections.property("por.idePorte"), "requerimento.requerimentoUnico.idePorte.idePorte")
				.add(Projections.property("por.nomPorte"), "requerimento.requerimentoUnico.idePorte.nomPorte")

				.add(Projections.property("req.dtcCriacao"), "requerimento.dtcCriacao")

				.add(Projections.property("orgao.ideOrgao"), "requerimento.ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "requerimento.ideOrgao.codOrgao")

				.add(Projections.property("p.idePessoa"), "requerimento.requerente.idePessoa")
				.add(Projections.property("pf.nomPessoa"), "requerimento.requerente.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.numCpf"), "requerimento.requerente.pessoaFisica.numCpf")
				.add(Projections.property("pj.nomRazaoSocial"), "requerimento.requerente.pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"), "requerimento.requerente.pessoaJuridica.numCnpj")

				.add(Projections.property("ideDeclaracaoParcialDae"), "ideDeclaracaoParcialDae")
				.add(Projections.property("numDeclaracaoParcialDae"), "numDeclaracaoParcialDae")
				.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("dtcEmissaoDeclaracaoParcialDae"), "dtcEmissaoDeclaracaoParcialDae")

				.add(Projections.property("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado");

		criteria.setProjection(Projections.distinct(projecao));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoParcialDae.class));

		criteria.add(Restrictions.eq("this.ideDeclaracaoParcialDae", ideCertificado));

		criteria.add(Restrictions.or(
				Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()),
				Restrictions.isNull("tpr.ideTipoPessoaRequerimento")));

		return this.declaracaoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasToken(Integer ideRequerimento) {
		Certificado certificado = this.carregarByIdRequerimentoAndAto(ideRequerimento);
		if (!Util.isNullOuVazio(certificado)) {
			return !Util.isNullOuVazio(certificado.getNumToken());
		} else {
			return false;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> carregarByIdeImovelAndTipo(Integer ideImovel, Integer ideTipo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("ideOrgao", "orgao")
				.createAlias("requerimento", "req").createAlias("tipoCertificado", "tipo")
				.createAlias("req.requerimentoImovelCollection", "ri");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")

						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")

						.add(Projections.property("numToken"), "numToken")

						.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")

						.add(Projections.property("tipoCertificado"), "tipoCertificado")

						.add(Projections.property("ideAtoAmbiental"), "ideAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		criteria.add(Restrictions.eq("ri.imovel.ideImovel", ideImovel));
		criteria.add(Restrictions.eq("tipo.ideTipoCertificado", ideTipo));

		criteria.addOrder(Order.desc("ideCertificado"));

		return this.certificadoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoByTipo(Integer ideTipo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class, "certificado")
				.createAlias("certificado.tipoCertificado", "tipo");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("certificado.ideCertificado"), "ideCertificado")
						.add(Projections.property("certificado.numCertificado"), "numCertificado")
						.add(Projections.property("certificado.dtcEmissaoCertificado"), "dtcEmissaoCertificado"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		criteria.add(Restrictions.eq("tipo.ideTipoCertificado", ideTipo));

		DetachedCriteria subCriteria = DetachedCriteria.forClass(Certificado.class, "ultimo")
				.createAlias("ultimo.tipoCertificado", "tipo");
		subCriteria.setProjection(Projections.projectionList().add(Projections.max("ultimo.dtcEmissaoCertificado")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		subCriteria.add(Restrictions.eq("tipo.ideTipoCertificado", ideTipo));

		criteria.add(Subqueries.propertyEq("certificado.dtcEmissaoCertificado", subCriteria));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoPorRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class, "certificado")
				.createAlias("certificado.tipoCertificado", "tipoCertificado", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("certificado.ideCertificado"), "ideCertificado")
				.add(Projections.property("certificado.numCertificado"), "numCertificado")
				.add(Projections.property("tipoCertificado.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		criteria.add(Restrictions.eq("certificado.requerimento.ideRequerimento", ideRequerimento));

		DetachedCriteria subCriteria = DetachedCriteria.forClass(Certificado.class, "ultimo");
		subCriteria.setProjection(Projections.projectionList().add(Projections.max("ultimo.ideCertificado")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		subCriteria.add(Restrictions.eq("ultimo.requerimento.ideRequerimento", ideRequerimento));

		criteria.add(Subqueries.propertyEq("certificado.ideCertificado", subCriteria));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoPorTipo(TipoCertificadoEnum tipoCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("tipoCertificado", "tipo")
				.add(Restrictions.eq("tipo.ideTipoCertificado", tipoCertificado.getId()))
				.setProjection(Projections.projectionList().add(Projections.max("numCertificado")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoByIde(Integer ideCertificado) {
		DetachedCriteria criteria = getCriteriaBasica(ideCertificado);

		Projection projection = getProjectionCertificado();

		criteria.setProjection(projection)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		return certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoByNumCertificado(String numeroCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("numCertificado", numeroCertificado));

		Projection projection = getProjectionCertificado();

		criteria.setProjection(projection)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		return certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarDeclaracaoByIde(Integer ideCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoParcialDae.class)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideMemoriaCalculoDaeParcela", "parcela", JoinType.INNER_JOIN)

				.add(Restrictions.eq("ideDeclaracaoParcialDae", ideCertificado));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDeclaracaoParcialDae"), "ideDeclaracaoParcialDae")
				.add(Projections.property("numDeclaracaoParcialDae"), "numDeclaracaoParcialDae")
				.add(Projections.property("numToken"), "numToken")
				.add(Projections.property("dtcEmissaoDeclaracaoParcialDae"), "dtcEmissaoDeclaracaoParcialDae")
				.add(Projections.property("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado")
				.add(Projections.property("parcela.ideMemoriaCalculoDaeParcela"),
						"ideMemoriaCalculoDaeParcela.ideMemoriaCalculoDaeParcela")
				.add(Projections.property("parcela.valor"), "ideMemoriaCalculoDaeParcela.valor")
				.add(Projections.property("parcela.numParcelaReferencia"),
						"ideMemoriaCalculoDaeParcela.numParcelaReferencia"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoParcialDae.class));
		return declaracaoDAO.buscarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteriaBasica(Integer ideCertificado) {
		return DetachedCriteria.forClass(Certificado.class)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideCertificado", ideCertificado));

	}

	private ProjectionList getProjectionCertificado() {
		return Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
				.add(Projections.property("numCertificado"), "numCertificado")
				.add(Projections.property("numToken"), "numToken")
				.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
				.add(Projections.property("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado");
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado buscarCertificadoParaCadastro(String numCertificado) {
		DetachedCriteria criteria = getCriteriaCertificadoCadastro(numCertificado);
		criteria.setProjection(getProjectionCertificadoCadastro())
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		return certificadoDAO.buscarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteriaCertificadoCadastro(String numCertificado) {
		return DetachedCriteria.forClass(Certificado.class)
				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("numCertificado", numCertificado))

				.createAlias("cadastro", "cad")
				.createAlias("cad.idePessoaFisicaCadastro", "pdCadastro", JoinType.INNER_JOIN)
				.createAlias("cad.idePessoaRequerente", "req", JoinType.INNER_JOIN)
				.createAlias("req.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)

				.createAlias("cad.tipoAtividadeNaoSujeitaLicenciamento", "atividade", JoinType.INNER_JOIN)
				.createAlias("cad.ideEmpreendimento", "emp", JoinType.INNER_JOIN)

				.createAlias("emp.enderecoEmpreendimentoCollection", "endem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endem.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("endem.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
				.add(Restrictions.eq("endereco.indExcluido", false));

	}

	private Projection getProjectionCertificadoCadastro() {
		return Projections.projectionList().add(Projections.groupProperty("ideCertificado"), "ideCertificado")
				.add(Projections.groupProperty("numCertificado"), "numCertificado")
				.add(Projections.groupProperty("numToken"), "numToken")
				.add(Projections.groupProperty("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
				.add(Projections.groupProperty("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado")

				.add(Projections.groupProperty("cad.ideCadastroAtividadeNaoSujeitaLic"),
						"cadastro.ideCadastroAtividadeNaoSujeitaLic")
				.add(Projections.groupProperty("cad.numCadastro"), "cadastro.numCadastro")

				.add(Projections.groupProperty("atividade.ideTipoAtividadeNaoSujeitaLicenciamento"),
						"cadastro.tipoAtividadeNaoSujeitaLicenciamento.ideTipoAtividadeNaoSujeitaLicenciamento")

				.add(Projections.groupProperty("emp.ideEmpreendimento"), "cadastro.ideEmpreendimento.ideEmpreendimento")
				.add(Projections.groupProperty("emp.nomEmpreendimento"), "cadastro.ideEmpreendimento.nomEmpreendimento")

				.add(Projections.groupProperty("endereco.ideEndereco"),
						"cadastro.ideEmpreendimento.endereco.ideEndereco")
				.add(Projections.groupProperty("endereco.numEndereco"),
						"cadastro.ideEmpreendimento.endereco.numEndereco")
				.add(Projections.groupProperty("endereco.desComplemento"),
						"cadastro.ideEmpreendimento.endereco.desComplemento")

				.add(Projections.groupProperty("logradouro.nomLogradouro"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.nomLogradouro")
				.add(Projections.groupProperty("logradouro.numCep"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.numCep")

				.add(Projections.groupProperty("bairro.ideBairro"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideBairro")
				.add(Projections.groupProperty("bairro.nomBairro"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")

				.add(Projections.groupProperty("municipio.ideMunicipio"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
				.add(Projections.groupProperty("municipio.nomMunicipio"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")

				.add(Projections.groupProperty("estado.ideEstado"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.groupProperty("estado.desSigla"),
						"cadastro.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")

				.add(Projections.sqlGroupProjection(
						"coalesce(pf5_.nom_pessoa,pj6_.nom_razao_social) as nom_requerente_", "nom_requerente_",
						new String[] { "nom_requerente_" }, new Type[] { StandardBasicTypes.STRING }),
						"cadastro.nomRequerente")
				.add(Projections.sqlGroupProjection("coalesce(pf5_.num_cpf,pj6_.num_cnpj) as num_cpf_cnpj_requerente_",
						"num_cpf_cnpj_requerente_", new String[] { "num_cpf_cnpj_requerente_" },
						new Type[] { StandardBasicTypes.STRING }), "cadastro.numCpfcnpjRequerente");

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean hasToken(Integer ideRequerimento, AtoAmbientalEnum atoEnum) {
		DeclaracaoParcialDae certificado = this.carregarByIdRequerimentoAndAtoDeclaracao(ideRequerimento, atoEnum);
		if (!Util.isNullOuVazio(certificado)) {
			return !Util.isNullOuVazio(certificado.getNumToken());
		} else {
			return false;
		}

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Certificado carregarByIdRequerimentoAndAto(Integer ideRequerimento, AtoAmbientalEnum atoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class).createAlias("ideOrgao", "orgao")
				.createAlias("requerimento", "req").createAlias("ideAtoAmbiental", "ato");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
						.add(Projections.property("numCertificado"), "numCertificado")
						.add(Projections.property("numToken"), "numToken")

						.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")

						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
						.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("ato.ideAtoAmbiental", atoEnum.getId()));

		return this.certificadoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarInformacoesCerhByCertificado(Integer ideCertificado) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)

				.createAlias("tipoCertificado", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerh", "c", JoinType.INNER_JOIN)
				.createAlias("c.ideCerhPai", "cp", JoinType.LEFT_OUTER_JOIN)

				.createAlias("c.idePessoaRequerente", "r", JoinType.INNER_JOIN)
				.createAlias("r.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("r.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)

				.createAlias("c.ideCerhStatus", "cs", JoinType.INNER_JOIN)

				.createAlias("c.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
				.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
				.createAlias("ee.ideEndereco", "end", JoinType.INNER_JOIN)
				.createAlias("end.ideLogradouro", "l", JoinType.INNER_JOIN)
				.createAlias("l.ideBairro", "b", JoinType.INNER_JOIN)
				.createAlias("l.ideMunicipio", "m", JoinType.INNER_JOIN)
				.createAlias("m.ideEstado", "est", JoinType.INNER_JOIN)

				.add(Restrictions.eq("ideCertificado", ideCertificado))
				.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
//				.add(Restrictions.isNull("c.indHistorico"))
				.add(Restrictions.or(Restrictions.isNull("c.indHistorico"),
						Restrictions.eq("c.indHistorico", Boolean.FALSE)))

				.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("ideCertificado"), "ideCertificado")
						.add(Projections.groupProperty("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
						.add(Projections.groupProperty("numCertificado"), "numCertificado")

						.add(Projections.groupProperty("tipo.ideTipoCertificado"), "tipoCertificado.ideTipoCertificado")

						.add(Projections.groupProperty("c.ideCerh"), "ideCerh.ideCerh")
						.add(Projections.groupProperty("c.numCadastro"), "ideCerh.numCadastro")

						.add(Projections.groupProperty("cs.ideCerhStatus"), "ideCerh.ideCerhStatus.ideCerhStatus")

						.add(Projections.groupProperty("pf.nomPessoa"),
								"ideCerh.idePessoaRequerente.pessoaFisica.nomPessoa")
						.add(Projections.groupProperty("pf.numCpf"), "ideCerh.idePessoaRequerente.pessoaFisica.numCpf")
						.add(Projections.groupProperty("pj.nomRazaoSocial"),
								"ideCerh.idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
						.add(Projections.groupProperty("pj.numCnpj"),
								"ideCerh.idePessoaRequerente.pessoaJuridica.numCnpj")

						.add(Projections.groupProperty("end.ideEndereco"),
								"ideCerh.ideEmpreendimento.endereco.ideEndereco")
						.add(Projections.groupProperty("end.numEndereco"),
								"ideCerh.ideEmpreendimento.endereco.numEndereco")
						.add(Projections.groupProperty("end.desComplemento"),
								"ideCerh.ideEmpreendimento.endereco.desComplemento")
						.add(Projections.groupProperty("l.ideLogradouro"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideLogradouro")
						.add(Projections.groupProperty("l.nomLogradouro"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.nomLogradouro")
						.add(Projections.groupProperty("l.numCep"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.numCep")
						.add(Projections.groupProperty("b.ideBairro"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideBairro")
						.add(Projections.groupProperty("b.nomBairro"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")
						.add(Projections.groupProperty("m.ideMunicipio"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
						.add(Projections.groupProperty("m.nomMunicipio"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
						.add(Projections.groupProperty("m.coordGeobahiaMunicipio"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.coordGeobahiaMunicipio")
						.add(Projections.groupProperty("est.ideEstado"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
						.add(Projections.groupProperty("est.nomEstado"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")
						.add(Projections.groupProperty("est.desSigla"),
								"ideCerh.ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")

				).setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));
		return certificadoDAO.buscarPorCriteria(criteria);
	}

	// #9330
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean existsCertificadoAnoPorTipo(TipoCertificadoEnum tipo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT max(substring(c.num_certificado, 10, 6))");
		sql.append(" FROM certificado c ");
		sql.append(" WHERE c.num_certificado IS NOT NULL AND c.num_certificado LIKE '%/");
		sql.append(tipo.getSigla());
		sql.append("' AND substring(c.num_certificado, 0, 5) = to_char(now(),'YYYY')");

		String retorno = String.valueOf(certificadoDAO.executarFuncaoNativeQuery(sql.toString(), params));

		return !Util.isNullOuVazio(retorno);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean existsDeclaracaoAnoPorTipo(TipoCertificadoEnum tipo) {

		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT max(substring(c.num_declaracao_parcial_dae, 10, 6))");
		sql.append(" FROM declaracao_parcial_dae c ");
		sql.append(" WHERE c.num_declaracao_parcial_dae IS NOT NULL AND c.num_declaracao_parcial_dae LIKE '%/");
		sql.append(tipo.getSigla());
		sql.append("' AND substring(c.num_declaracao_parcial_dae, 0, 5) = to_char(now(),'YYYY')");

		String retorno = String.valueOf(declaracaoDAO.executarFuncaoNativeQuery(sql.toString(), params));
		return !Util.isNullOuVazio(retorno);

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String obterProximaSequencePorTipoCertificado(String sequece) {

		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cast(nextval('").append(sequece).append("') as character varying)");
		return certificadoDAO.executarFuncaoNativeQuery(sql.toString(), params);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String resetSequencePorTipoCertificado(String sequece) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT setval('").append(sequece).append("', 1)");
		return certificadoDAO.executarFuncaoNativeQuery(sql.toString(), params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> listarPorNumeroCertificado(String numToken) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Certificado.class)

					.createAlias("requerimento", "req", JoinType.INNER_JOIN)
					.createAlias("req.processoCollection", "p", JoinType.INNER_JOIN)
					.createAlias("p.processoAtoCollection", "pa", JoinType.INNER_JOIN)
					.createAlias("pa.atoAmbiental", "a", JoinType.INNER_JOIN);

			criteria.add(Restrictions.eq("numToken", numToken))
					.add(Restrictions.or(Restrictions.eq("a.ideAtoAmbiental", 12),
							Restrictions.eq("a.ideAtoAmbiental", 15)))

					.setProjection(
							Projections.projectionList().add(Projections.property("ideCertificado"), "ideCertificado")
									.add(Projections.property("dtcEmissaoCertificado"), "dtcEmissaoCertificado")
									.add(Projections.property("numCertificado"), "numCertificado")
									.add(Projections.property("numToken"), "numToken"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Certificado.class));

			return certificadoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoParcialDae> carregarDeclaracaoByIdRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoParcialDae.class)
				.createAlias("ideOrgao", "orgao").createAlias("requerimento", "req")
				.createAlias("ideAtoAmbiental", "ato", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDeclaracaoParcialDae"), "ideDeclaracaoParcialDae")
				.add(Projections.property("numDeclaracaoParcialDae"), "numDeclaracaoParcialDae")
				.add(Projections.property("numToken"), "numToken")

				.add(Projections.property("dtcEmissaoDeclaracaoParcialDae"), "dtcEmissaoDeclaracaoParcialDae")

				.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

				.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
				.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoParcialDae.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		return this.declaracaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DeclaracaoParcialDae carregarByIdRequerimentoAndAtoDeclaracao(Integer ideRequerimento,
			AtoAmbientalEnum atoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoParcialDae.class)
				.createAlias("ideOrgao", "orgao").createAlias("requerimento", "req")
				.createAlias("ideAtoAmbiental", "ato");

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDeclaracaoParcialDae"), "ideDeclaracaoParcialDae")
				.add(Projections.property("numDeclaracaoParcialDae"), "numDeclaracaoParcialDae")
				.add(Projections.property("numToken"), "numToken")

				.add(Projections.property("dtcEmissaoDeclaracaoParcialDae"), "dtcEmissaoDeclaracaoParcialDae")

				.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")

				.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
				.add(Projections.property("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoParcialDae.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("ato.ideAtoAmbiental", atoEnum.getId()));

		return this.declaracaoDAO.buscarPorCriteria(criteria);
	}

}
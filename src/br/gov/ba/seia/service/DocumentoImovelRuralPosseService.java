package br.gov.ba.seia.service;


import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DocumentoImovelRuralPosseDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoImovelRuralPosseService {

	@Inject
	DocumentoImovelRuralPosseDAOImpl daoImpl;

	@Inject
	IDAO<DocumentoImovelRuralPosse> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRuralPosse filtrarById(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		return this.daoImpl.filtrarById(pDocumentoImovelRuralPosse);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRuralPosse carregarTudo(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoImovelRuralPosse.class)
			.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDocumentoImovelRural", "documentoImovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoDocumentoImovelRural", "tipoDocumentoImovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tipoDocumentoImovelRural.ideTipoVinculoImovel", "tipoVinculoImovel", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("ideEnderecoDeclarante", "enderecoDeclarante", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enderecoDeclarante.ideLogradouro", "logradouroDeclarante", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouroDeclarante.ideBairro", "bairroDeclarante", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouroDeclarante.ideMunicipio", "municipioDeclarante", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouroDeclarante.ideTipoLogradouro", "tipoLogradouroDeclarante", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideDocumentoImovelRuralPosse"), "ideDocumentoImovelRuralPosse")
				.add(Projections.groupProperty("dtcDocumento"), "dtcDocumento")
				.add(Projections.groupProperty("dscEmissorDocumento"), "dscEmissorDocumento")
				.add(Projections.groupProperty("nomVendedor"), "nomVendedor")
				.add(Projections.groupProperty("numCpfVendedor"), "numCpfVendedor")
				.add(Projections.groupProperty("nomDeclarante"), "nomDeclarante")
				.add(Projections.groupProperty("numCpfCnpjDeclarante"), "numCpfCnpjDeclarante")
				.add(Projections.groupProperty("indExcluido"), "indExcluido")
				.add(Projections.groupProperty("dtcCriacao"), "dtcCriacao")
				.add(Projections.groupProperty("dtcExclusao"), "dtcExclusao")
				
				.add(Projections.groupProperty("imovelRural.ideImovelRural"), "ideImovelRural.ideImovelRural")
				.add(Projections.groupProperty("imovelRural.desDenominacao"), "ideImovelRural.desDenominacao")

				.add(Projections.groupProperty("documentoImovelRural.ideDocumentoImovelRural"), "ideDocumentoImovelRural.ideDocumentoImovelRural")
				.add(Projections.groupProperty("documentoImovelRural.nomDocumentoObrigatorio"), "ideDocumentoImovelRural.nomDocumentoObrigatorio")
				.add(Projections.groupProperty("documentoImovelRural.dscCaminhoArquivo"), "ideDocumentoImovelRural.dscCaminhoArquivo")
				
				.add(Projections.groupProperty("tipoDocumentoImovelRural.ideTipoDocumentoImovelRural"), "ideTipoDocumentoImovelRural.ideTipoDocumentoImovelRural")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.sglTipoDocumentoImovelRural"), "ideTipoDocumentoImovelRural.sglTipoDocumentoImovelRural")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.dscTipoDocumentoImovelRural"), "ideTipoDocumentoImovelRural.dscTipoDocumentoImovelRural")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.numGrupoDocumento"), "ideTipoDocumentoImovelRural.numGrupoDocumento")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.indExcluido"), "ideTipoDocumentoImovelRural.indExcluido")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.dtcCriacao"), "ideTipoDocumentoImovelRural.dtcCriacao")
				.add(Projections.groupProperty("tipoDocumentoImovelRural.dtcExclusao"), "ideTipoDocumentoImovelRural.dtcExclusao")
				
				.add(Projections.groupProperty("tipoVinculoImovel.ideTipoVinculoImovel"), "ideTipoDocumentoImovelRural.ideTipoVinculoImovel.ideTipoVinculoImovel")
				.add(Projections.groupProperty("tipoVinculoImovel.nomTipoVinculoImovel"), "ideTipoDocumentoImovelRural.ideTipoVinculoImovel.nomTipoVinculoImovel")
				
				.add(Projections.groupProperty("enderecoDeclarante.ideEndereco"), "ideEnderecoDeclarante.ideEndereco")
				.add(Projections.groupProperty("enderecoDeclarante.numEndereco"), "ideEnderecoDeclarante.numEndereco")
				.add(Projections.groupProperty("enderecoDeclarante.dtcCriacao"), "ideEnderecoDeclarante.dtcCriacao")
				.add(Projections.groupProperty("enderecoDeclarante.indExcluido"), "ideEnderecoDeclarante.indExcluido")
				.add(Projections.groupProperty("enderecoDeclarante.dtcExclusao"), "ideEnderecoDeclarante.dtcExclusao")
				.add(Projections.groupProperty("enderecoDeclarante.desComplemento"), "ideEnderecoDeclarante.desComplemento")
				.add(Projections.groupProperty("enderecoDeclarante.desPontoReferencia"), "ideEnderecoDeclarante.desPontoReferencia")

				.add(Projections.groupProperty("logradouroDeclarante.ideLogradouro"), "ideEnderecoDeclarante.ideLogradouro.ideLogradouro")
				.add(Projections.groupProperty("logradouroDeclarante.nomLogradouro"), "ideEnderecoDeclarante.ideLogradouro.nomLogradouro")
				.add(Projections.groupProperty("logradouroDeclarante.numCep"), "ideEnderecoDeclarante.ideLogradouro.numCep")
				.add(Projections.groupProperty("logradouroDeclarante.indOrigemCorreio"), "ideEnderecoDeclarante.ideLogradouro.indOrigemCorreio")
				.add(Projections.groupProperty("logradouroDeclarante.indOrigemApi"), "ideEnderecoDeclarante.ideLogradouro.indOrigemApi")
				
				.add(Projections.groupProperty("bairroDeclarante.ideBairro"), "ideEnderecoDeclarante.ideLogradouro.ideBairro.ideBairro")
				.add(Projections.groupProperty("bairroDeclarante.nomBairro"), "ideEnderecoDeclarante.ideLogradouro.ideBairro.nomBairro")
				.add(Projections.groupProperty("bairroDeclarante.indOrigemCorreio"), "ideEnderecoDeclarante.ideLogradouro.ideBairro.indOrigemCorreio")
				.add(Projections.groupProperty("bairroDeclarante.indOrigemApi"), "ideEnderecoDeclarante.ideLogradouro.ideBairro.indOrigemApi")
				
				.add(Projections.groupProperty("municipioDeclarante.ideMunicipio"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.ideMunicipio")
				.add(Projections.groupProperty("municipioDeclarante.nomMunicipio"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.nomMunicipio")
				.add(Projections.groupProperty("municipioDeclarante.indEstadoEmergencia"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.indEstadoEmergencia")
				.add(Projections.groupProperty("municipioDeclarante.numCep"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.numCep")
				.add(Projections.groupProperty("municipioDeclarante.indBloqueioDQC"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.indBloqueioDQC")
				.add(Projections.groupProperty("municipioDeclarante.coordGeobahiaMunicipio"), "ideEnderecoDeclarante.ideLogradouro.ideMunicipio.coordGeobahiaMunicipio")
				
				.add(Projections.groupProperty("tipoLogradouroDeclarante.ideTipoLogradouro"), "ideEnderecoDeclarante.ideLogradouro.ideTipoLogradouro.ideTipoLogradouro")
				.add(Projections.groupProperty("tipoLogradouroDeclarante.sglTipoLogradouro"), "ideEnderecoDeclarante.ideLogradouro.ideTipoLogradouro.sglTipoLogradouro")
				.add(Projections.groupProperty("tipoLogradouroDeclarante.nomTipoLogradouro"), "ideEnderecoDeclarante.ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoImovelRuralPosse.class))
			
			.add(Restrictions.eq("ideDocumentoImovelRuralPosse", pDocumentoImovelRuralPosse.getIdeDocumentoImovelRuralPosse()))
			.add(Restrictions.eq("indExcluido", false));
		
		return dao.buscarPorCriteria(criteria);
	}

	public DocumentoImovelRuralPosse buscarByImovelRural(ImovelRural imovelRural) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoImovelRuralPosse.class, "documentoImovelRuralPosse")
			.createAlias("ideTipoDocumentoImovelRural", "tipoDocumentoImovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDocumentoImovelRural", "documentoImovelRural", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEnderecoDeclarante", "enderecoDeclarante", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideImovelRural", imovelRural))
			.add(Restrictions.eq("indExcluido", false))
			
			.addOrder(Order.asc("ideReservaLegal"));
		
		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		this.daoImpl.salvar(pDocumentoImovelRuralPosse);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)   {
		this.daoImpl.atualizar(pDocumentoImovelRuralPosse);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		this.daoImpl.remover(pDocumentoImovelRuralPosse);
	}
}

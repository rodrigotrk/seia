package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BiomaEnquadramentoAtoAmbientalDAOImpl;
import br.gov.ba.seia.dao.BiomaRequerimentoDAOImpl;
import br.gov.ba.seia.entity.BiomaEnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.service.BiomaRequerimentoService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BiomaRequerimentoServiceFacade {
	
	@EJB
	private BiomaRequerimentoService biomaRequerimentoService;
	@EJB
	private BiomaRequerimentoDAOImpl biomaRequerimentoDAOImpl;
	
	@Inject
	private BiomaEnquadramentoAtoAmbientalDAOImpl biomaEnquadramentoAtoAmbientalDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> carregarListaBiomaRequerimento(Requerimento req) throws Exception {
		biomaRequerimentoService.gerarListaBiomaRequerimento(req, true);
		return biomaRequerimentoDAOImpl.listarPor(req.getIdeRequerimento());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> somarAreasBiomaRequerimento(Collection<BiomaRequerimento> lista) {
		return biomaRequerimentoService.somarAreasBiomaRequerimento(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> carregarListaBiomaRequerimentoDetalhamento(Requerimento req) throws Exception {
		biomaRequerimentoService.gerarListaBiomaRequerimento(req, false);
		Collection<BiomaRequerimento> lista = biomaRequerimentoDAOImpl.listarPor(req.getIdeRequerimento());
		return somarAreasBiomaRequerimento(lista);
	}	
	
	public Double calcularValorTotalAreaBioma(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		return biomaRequerimentoService.calcularValorTotalAreaBioma(listaBiomaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPorRequerimento(Integer ideRequerimento) throws Exception {
		List<BiomaEnquadramentoAtoAmbiental> lista = biomaEnquadramentoAtoAmbientalDAOImpl.biomaEnquadramentoAtoAmbientalListarByIdeRequerimento(ideRequerimento);
		if (!Util.isNullOuVazio(lista)) {
			for (BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental : lista) {
				biomaEnquadramentoAtoAmbientalDAOImpl.removerPorBiomaRequerimento(biomaEnquadramentoAtoAmbiental.getIdeBiomaRequerimento().getIdeBiomaRequerimento());
			}
		}else {
			biomaRequerimentoDAOImpl.removerPor(ideRequerimento);
		}
	}
}

package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dto.CerhConsultarDaeDto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoRpga;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.service.CerhConsultarDaeGeradoService;
import br.gov.ba.seia.service.CerhSituacaoDaeService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GeoRpgaService;
import br.gov.ba.seia.service.PessoaFisicaService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhConsultarDaesGeradosFacade {
	
	@EJB
	private GeoRpgaService geoRpgaService ;
	
	@EJB
	private CerhSituacaoDaeService cerhSituacaoDaeService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService ;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private CerhConsultarDaeGeradoService cerhConsultarDaeGeradoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<GeoRpga> listarRpgaComPrecoPublicoUnitario() {
		
		return geoRpgaService.listarRpgaComPrecoPublicoUnitario();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoDae> listarCerhSituacaoDae() {
		
		return cerhSituacaoDaeService.listarCerhSituacaoDae();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisicaByIde(Integer idePessoaFisica) {
		
		return pessoaFisicaService.buscarPessoaFisicaByIde(idePessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Empreendimento> listarEmpreendimento(Pessoa pPessoa)  {
		
		return empreendimentoService.listarEmpreendimento(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarComLocalizacaoGeografica(Empreendimento pEmpreendimento)  {
		
		return empreendimentoService.carregarComLocalizacaoGeografica(pEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhConsultarDaeDto> consultarDaeGerado(Map<String, Object> params)  {
		
		return cerhConsultarDaeGeradoService.consultarDaeGerado(params);
	}
}

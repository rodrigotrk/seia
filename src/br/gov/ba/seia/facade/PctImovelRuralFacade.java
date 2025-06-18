package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.PctFamilia;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.entity.TipoTerritorioPct;
import br.gov.ba.seia.entity.TipoVinculoPCT;
import br.gov.ba.seia.enumerator.PaisEnum;
import br.gov.ba.seia.enumerator.TipoPessoaJuridicaPctEnum;
import br.gov.ba.seia.service.PctFamiliaService;
import br.gov.ba.seia.service.PctImovelRuralService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaPctService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoSeguimentoPctService;
import br.gov.ba.seia.service.TipoTerritorioPctService;
import br.gov.ba.seia.service.TipoVinculoPCTService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PctImovelRuralFacade {

	@EJB
	private TipoSeguimentoPctService tipoSeguimentoPctService;
	
	@EJB
	private TipoTerritorioPctService tipoTerritorioPctService;
	
	@EJB
	private PctImovelRuralService pctImovelRuralService;
	
	@EJB
	private TipoVinculoPCTService tipoVinculoPCTPctService;
	
	@EJB
	private PessoaJuridicaPctService pessoaJuridicaPctService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private PctFamiliaService pctFamiliaService;
	
	public List<TipoSeguimentoPct> listarTipoSeguimentoPct() throws Exception{
		return tipoSeguimentoPctService.listarTipoSeguimentoPct();
	}
	
	public List<TipoTerritorioPct> listarTipoTerritorioPct() throws Exception{
		return tipoTerritorioPctService.listarTipoTerritorioPct();
	}
	
	public PctImovelRural listarPctImovelRural(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralService.buscarPctImovelRural(imovelRural);
	}
	
	public List<TipoVinculoPCT> listarTipoVinculoPCT() throws Exception{
		return tipoVinculoPCTPctService.listarTipoVinculoPCT();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPctImovelRural(PctImovelRural pctImovelRural) throws Exception{
		pctImovelRuralService.salvarPctImovelRural(pctImovelRural);
	}
	
	public PctImovelRural buscarPctImovelRural(ImovelRural imovelRural) throws Exception{
		PctImovelRural pctImovelRural = pctImovelRuralService.buscarPctImovelRural(imovelRural);
		if (!Util.isNullOuVazio(pctImovelRural)) {
			carregarFamilia(pctImovelRural);
			carregarPJ(pctImovelRural);
		}
		return pctImovelRural;
	}

	private void carregarPJ(PctImovelRural pctImovelRural) throws Exception {
		pctImovelRural.setPessoaJuridicaPctList(listarPessoaJuridicaByPct(pctImovelRural));
		pctImovelRural.setListarPctRepresentanteFamiliaCollection(pctFamiliaService.listarPctRepresentanteFamilia(pctImovelRural));
		if(!Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())){
			/*for(PessoaJuridicaPct pessoaJuridica: pctImovelRural.getPessoaJuridicaPctList()){
				pessoaJuridica.getIdePessoaJuridica().setNumCnpj(Util.formatarCNPJ(pessoaJuridica.getIdePessoaJuridica().getNumCnpj()));
			}*/
			
			for(PessoaJuridicaPct pjPct : pctImovelRural.getPessoaJuridicaPctList()) {
				if(TipoPessoaJuridicaPctEnum.ASSOCIACAO.getId().equals(pjPct.getIdeTipoPessoaJuridicaPct().getIdeTipoPessoaJuridicaPct())) {
					if(Util.isNull(pctImovelRural.getPessoaJuridicaPctListaAssociacao())) {
						pctImovelRural.setPessoaJuridicaPctListaAssociacao(new ArrayList<PessoaJuridicaPct>());
					}
					pctImovelRural.getPessoaJuridicaPctListaAssociacao().add(pjPct);
				}
				else if(TipoPessoaJuridicaPctEnum.CONCEDENTE.getId().equals(pjPct.getIdeTipoPessoaJuridicaPct().getIdeTipoPessoaJuridicaPct())) {
					if(Util.isNull(pctImovelRural.getPessoaJuridicaPctListaConcedente())) {
						pctImovelRural.setPessoaJuridicaPctListaConcedente(new ArrayList<PessoaJuridicaPct>());
					}
					pctImovelRural.getPessoaJuridicaPctListaConcedente().add(pjPct);
				}
				else if(TipoPessoaJuridicaPctEnum.CONCESSIONARIO.getId().equals(pjPct.getIdeTipoPessoaJuridicaPct().getIdeTipoPessoaJuridicaPct())) {
					if(Util.isNull(pctImovelRural.getPessoaJuridicaPctListaConcessionario())) {
						pctImovelRural.setPessoaJuridicaPctListaConcessionario(new ArrayList<PessoaJuridicaPct>());
					}
					pctImovelRural.getPessoaJuridicaPctListaConcessionario().add(pjPct);
				}
			}
		}
		
	}

	private void carregarFamilia(PctImovelRural pctImovelRural) throws Exception {
		pctImovelRural.setPctFamiliaCollection(pctFamiliaService.listarPctRepresentanteFamilia(pctImovelRural));
		Collection<PctFamilia> listaMembros = pctFamiliaService.listarPctFamilia(pctImovelRural);
		for(PctFamilia representanteFamilia:pctImovelRural.getPctFamiliaCollection()){
			if(Util.isNullOuVazio(representanteFamilia.getIdePessoaAssociada())){
				representanteFamilia.setMembrosFamiliaCollection(new ArrayList<PctFamilia>());
				for(PctFamilia membroFamilia:listaMembros){
					if(!Util.isNullOuVazio(membroFamilia.getIdePessoaAssociada()) && membroFamilia.getIdePessoaAssociada().equals(representanteFamilia.getIdePessoa())) {
						representanteFamilia.getMembrosFamiliaCollection().add(membroFamilia);
					}
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoDocumentoImovelRural> findByIdeTipoVinculoImovelPorTerritorio(Integer ideTipoTerritorioPct) throws Exception {
		return tipoTerritorioPctService.findByIdeTipoVinculoImovelPorTerritorio(ideTipoTerritorioPct);
	}
	
	public boolean existePessoaJuridicaAssociadaComunidade(PctImovelRural pctImovelRural, PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		return pessoaJuridicaPctService.existePessoaJuridicaAssociadaComunidade(pctImovelRural, pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		pessoaJuridicaPctService.salvarPessoaJuridicarPct(pessoaJuridicaPct);
	}
	
	public List<PessoaJuridicaPct> listarPessoaJuridicaByPct(PctImovelRural pctImovelRural) throws Exception{
		return pessoaJuridicaPctService.listarPessoaJuridicaByPct(pctImovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaJuridica(PessoaJuridica pessoaJuridica) throws Exception {
		Pessoa pessoa = pessoaJuridica.getPessoa();
		
		if (Util.isNullOuVazio(pessoa)) {
			pessoa = new Pessoa();
			pessoa.setDtcCriacao(new Date());
			pessoa.setIndExcluido(false);
			pessoaJuridica.setPessoa(pessoa);
		}
		
		pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRepresentanteLegal(RepresentanteLegal representanteLegal) throws Exception {
		representanteLegalService.salvarRepresentanteLegal(representanteLegal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaFisicaRepresentante(PessoaFisica pctPessoaFisica) throws Exception {
		Pessoa pessoa = pctPessoaFisica.getPessoa();
		String email = pessoa.getDesEmail();
		if (Util.isNullOuVazio(pessoa)) {
			pessoa = new Pessoa();
			pessoa.setDtcCriacao(new Date());
			pessoa.setIndExcluido(false);
			pessoa.setDesEmail(email);
			pctPessoaFisica.setPessoa(pessoa);
		}
		
		pctPessoaFisica.setIdePais(new Pais(PaisEnum.BRASIL.getId()));
		pessoaFisicaService.salvarOuAtualizarPessoaFisica(pctPessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaJuridicaPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		pessoaJuridicaPct.setIndExcluido(true);
		pessoaJuridicaPct.setDtcExclusao(new Date());
		salvarPessoaJuridicarPct(pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTelefone(PessoaJuridicaPct pessoaJuridicaPct) throws Exception {
		
		if(!Util.isNull(pessoaJuridicaPct.getIdeTelefone()) && !Util.isNull(pessoaJuridicaPct.getIdeTelefone().getNumTelefone())){
			Telefone telefone = pessoaJuridicaPct.getIdeTelefone();
			telefone.setPessoaCollection(new ArrayList<Pessoa>());
			telefone.getPessoaCollection().add(pessoaJuridicaPct.getIdePessoaFisicaRepresentanteLegal().getPessoa());
			telefone.setIdeTipoTelefone(new TipoTelefone(1,"Residencial"));
			telefoneService.salvarTelefone(telefone);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Telefone buscarTelefonesPorPessoa(PessoaFisica pessoaFisica) throws Exception {
		
		Pessoa pessoa = new Pessoa();
		
		Telefone telefone = new Telefone();
		
		pessoa.setIdePessoa(pessoaFisica.getIdePessoaFisica());
		List<Telefone> telefonesList=  telefoneService.buscarTelefonesPorPessoa(pessoa);
		
		for (Telefone telefoneItem : telefonesList) {
			
			if(telefoneItem.getIdeTipoTelefone().getIdeTipoTelefone() == 1){
				telefone = telefoneItem;
				break;
			}
		}
		
		return telefone;
	}
	
	public boolean isPCT(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralService.isPCT(imovelRural);
	}
}

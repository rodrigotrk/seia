package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhPondClasCorpoHidrico;
import br.gov.ba.seia.entity.CerhPondGestao;
import br.gov.ba.seia.entity.CerhPondVolConsumido;
import br.gov.ba.seia.entity.CerhPrecoPubUnitario;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.CerhCobrancaService;
import br.gov.ba.seia.service.CerhCodigoReceitaService;
import br.gov.ba.seia.service.CerhDaeService;
import br.gov.ba.seia.service.CerhPondClasCorpoHidricoService;
import br.gov.ba.seia.service.CerhPondGestaoService;
import br.gov.ba.seia.service.CerhPondVolConsumidoService;
import br.gov.ba.seia.service.CerhPrecoPubUnitarioService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCobrancaFacade {
	
	@EJB
	private CerhCobrancaService cerhCobrancaService; 
	@EJB
	private EmpreendimentoService empreendimentoService; 
	
	@EJB
	private CerhPondClasCorpoHidricoService kClasseService;
	
	@EJB
	private CerhPrecoPubUnitarioService ppuService;
	
	@EJB
	private CerhPondVolConsumidoService k0Service;
	
	@EJB
	private CerhPondGestaoService kgService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@EJB
	private CerhCodigoReceitaService cerhCodigoReceitaService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private EnderecoService enderecoService;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private LogradouroService logradouroService; 
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;

	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private CerhDaeService daeService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarCerh(Empreendimento empreendimento) {
		return cerhCobrancaService.buscarCerh(empreendimento);
	}
	/**
	 * retorna os empreendimentos com suas localizações geograficas
	 * @param pessoa
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Empreendimento> listarEmpreendimentos(Pessoa pessoa) {
		List<Empreendimento> empreendimentos = new ArrayList<Empreendimento>(empreendimentoService.listarEmpreendimento(pessoa));
		for(Empreendimento emp : empreendimentos){
			//carrega a localização do Empreendimento
			emp.setIdeLocalizacaoGeografica(empreendimentoService.carregarComLocalizacaoGeografica(emp).getIdeLocalizacaoGeografica());
		}
		return empreendimentos;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondClasCorpoHidrico getCerhClasseCorpoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		return kClasseService.getCerhPondClasCorpoHidricoByTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPrecoPubUnitario getPPU(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		return ppuService.getCerhPrecoPubUnitarioByTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPrecoPubUnitario getPPUConsumo(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		return ppuService.getCerhPrecoPubUnitarioByTipoUsoRecursoHidricoConsumo(tipoUsoRecursoHidrico);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondVolConsumido getK0() {
		return k0Service.getCerhPondVolConsumido();
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondGestao getKg() {
		return kgService.getCerhPondGestao();
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvar(CerhCobranca cobranca) {
		cerhCobrancaService.salvarCerhCobranca(cobranca);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getRpga(LocalizacaoGeografica localizacaoGeografica) {
		return localizacaoGeograficaServiceFacade.getRPGA(localizacaoGeografica);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SefazCodigoReceita getSefazCodigoReceita(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		return cerhCodigoReceitaService.getCodigoReceita(tipoUsoRecursoHidrico);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisica(Integer idePessoa) {
		return pessoaFisicaService.buscarPessoaFisicaByIde(idePessoa);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private EnderecoEmpreendimento buscarEnderecoEmpreendimento(Empreendimento empreendimento) {
		return enderecoEmpreendimentoService.obterEnderecoEmpreendimento(empreendimento.getIdeEmpreendimento());
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco buscarEndereco(Empreendimento empreendimento) {
		EnderecoEmpreendimento enderecoEmpreendimento = buscarEnderecoEmpreendimento(empreendimento);
		return enderecoService.carregar(enderecoEmpreendimento.getIdeEndereco().getIdeEndereco());
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRepresentanateLegal(PessoaFisica pessoaValidar, PessoaJuridica pessoaJuridica) {
		return 1 == representanteLegalService.verificaRepresentanteLegal(pessoaValidar, pessoaJuridica);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcurador(PessoaFisica pessoaValidar, Pessoa pessoa) {
		if(pessoa.isPF()){
			ProcuradorPessoaFisica procurador = procuradorPessoaFisicaService.buscarProcuradorPessoaFisica(pessoaValidar, pessoa.getPessoaFisica());
			return procurador != null;
		}else{
			ProcuradorRepresentante procurador = procuradorRepresentanteService.buscarProcuradorRepresentante(pessoaValidar, pessoa.getPessoaJuridica());
			return procurador != null;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCobrancaJaEmitida(CerhCobranca cobranca) {
		return cerhCobrancaService.isCobrancaJaEmitida(cobranca);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void atualizarDae(Dae dae, Usuario usuario) {
		HistSituacaoDae historico = new HistSituacaoDae();
		historico.setDtAlteracao(Calendar.getInstance().getTime());
		historico.setIdeDae(dae);
		historico.setIdeSituacaoDae(new SituacaoDae(SituacaoDaeEnum.EM_ABERTO.getId()));
		historico.setIdeUsuario(usuario);
		dae.getHistSituacaoDae().add(historico);
		daeService.atualizar(dae);
	}
	
	
}

package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoFacade {
	
	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@EJB
	private EnderecoService enderecoService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private ImovelService imovelService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;

	@EJB
	private EstadoService estadoService;
	
	@Inject
	IDAO<Endereco> enderecoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEndereco(Endereco endereco) throws Exception{
		enderecoService.removerEndereco(endereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLogradouro(Logradouro logradouro) throws Exception {
		logradouroService.salvarLogradouro(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByCepSemIndCorreio(Logradouro logradouro) throws Exception {
		return logradouroService.filtrarLogradouroByCepSemIndCorreio(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByCep(Logradouro logradouro) throws Exception {
		return logradouroService.filtrarLogradouroByCep(logradouro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Bairro> filtrarBairroByCep(Logradouro logradouro) throws Exception {
		return bairroService.listarBairroByLogradouro(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Logradouro filtrarLogradouroById(Logradouro logradouro) throws Exception {
		return logradouroService.filtrarLogradouroById(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByBairro(Bairro bairro, Integer numCep) throws Exception {
		return logradouroService.filtrarLogradouroByBairro(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByBairroAndApi(Bairro bairro, Integer numCep) throws Exception {
		return logradouroService.filtrarLogradouroByBairroAndApi(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByBairroSemIndCorreio(Bairro bairro, Integer numCep) throws Exception {
		return logradouroService.filtrarLogradouroByBairroSemIndCorreio(bairro, numCep);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBairro(Bairro bairro) throws Exception {
		bairroService.salvarBairro(bairro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Bairro filtrarBairroById(Bairro bairro) throws Exception {
		return bairroService.filtrarBairroById(bairro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoPessoa(EnderecoPessoa enderecoPessoa) throws Exception {
		enderecoPessoaService.salvarEnderecoPessoa(enderecoPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoPessoa filtrarEnderecoByPessoa(Pessoa pessoa) throws Exception {
		return enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento) throws Exception {
		enderecoEmpreendimentoService.salvarEnderecoEmpreendimento(enderecoEmpreendimento);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco filtrarEnderecoByEnderecoEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento) throws Exception {
		return enderecoEmpreendimentoService.filtrarEnderecoByEnderecoEmpreendimento(enderecoEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpreendimento> filtrarEnderecoByEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento) throws Exception {
		return enderecoEmpreendimentoService.filtrarEnderecoByEmpreendimento(enderecoEmpreendimento);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpreendimento> filtrarEnderecoByEmpreendimentoCriteria(EnderecoEmpreendimento enderecoEmpreendimento) throws Exception {
		return enderecoEmpreendimentoService.filtraEnderecoEmpreendimentoByEmpreendimento(enderecoEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void salvarEndereco(Endereco endereco) throws Exception {
		
		enderecoService.salvarEndereco(endereco);
		
//		if(Util.isNullOuVazio(endereco.getIdeEndereco())) {
//			enderecoService.salvarEndereco(endereco);
//		} else {
//			enderecoService.atualizarEnderecoSQL(endereco);
//		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Endereco carregar(Integer ideEndereco) throws Exception {
		return enderecoService.carregar(ideEndereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoImovel(Endereco endereco, Logradouro logradouro, Bairro bairro,Imovel imovel) throws Exception{ 
		
		salvarEnderecoCompleto(endereco, logradouro, bairro);
		imovel.setIdeEndereco(endereco);
		imovelService.salvarAtualizarImovel(imovel);

		
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void salvarEnderecoCompleto(Endereco endereco, Logradouro logradouro, Bairro bairro) throws Exception {
		
		salvarBairro(bairro);
		
		Collection<Endereco> endCollection = logradouro.getEnderecoCollection();
		logradouro.setEnderecoCollection(null);
		
		salvarLogradouro(logradouro);
		
		endereco.setIdeLogradouro(logradouro);
		
		salvarEndereco(endereco);
		
		//logradouro.setEnderecoCollection(endCollection);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoPF(Endereco endereco, Logradouro logradouro, Bairro bairro, EnderecoPessoa enderecoPessoa) throws Exception {
		
		salvarEnderecoCompleto(endereco, logradouro, bairro);
		
		if(!Util.isNullOuVazio(enderecoPessoa)){
			salvarEnderecoPessoa(enderecoPessoa);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoEmpreendimento(Endereco endereco, Logradouro logradouro, Bairro bairro, Empreendimento empreendimento) throws Exception {
		//salvar enderecoEmpreendimento
		
		salvarEnderecoCompleto(endereco,logradouro,bairro);
		
		EnderecoEmpreendimento endEmp = new EnderecoEmpreendimento();
		if(!Util.isNullOuVazio(empreendimento)){
			EnderecoEmpreendimento endereceoEmp = new EnderecoEmpreendimento();
			List<EnderecoEmpreendimento> listaEndEmpreendimento = new ArrayList<EnderecoEmpreendimento>();
			
			endereceoEmp.setIdeEmpreendimento(empreendimento);

			listaEndEmpreendimento = filtrarEnderecoByEmpreendimentoCriteria(endereceoEmp);
			
			if (!listaEndEmpreendimento.isEmpty()) {
				for (EnderecoEmpreendimento ee : listaEndEmpreendimento) {
					if(ee.getIdeTipoEndereco().getIdeTipoEndereco() == TipoEnderecoEnum.LOCALIZACAO.getId()) {
						endEmp = ee;
					}
				}
			}
			
			endEmp.setIdeEndereco(endereco);
			endEmp.setIdeEmpreendimento(empreendimento);
			endEmp.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId()));
			
			salvarEnderecoEmpreendimento(endEmp);
		}
		
		if(!Util.isNullOuVazio(empreendimento.getEmpreendimentoRequerimentoCollection())) {
			empreendimento.getEnderecoEmpreendimentoCollection().clear();
			empreendimento.getEnderecoEmpreendimentoCollection().add(endEmp);
		}
		
		//atualizar os imoveis urbanos
		atualizarImoveis(empreendimento, endereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void atualizarImoveis(Empreendimento empreendimento,Endereco endereco){
		if (!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getImovelCollection())) {

			for (Imovel lImovel : empreendimento.getImovelCollection()) {

				ImovelUrbano lImovelUrbano = lImovel.getImovelUrbano();

				try {
					if (!Util.isNullOuVazio(lImovelUrbano) && !Util.isNullOuVazio(lImovelUrbano.getNumInscricaoIptu())) {
						lImovel.setIdeEndereco(endereco);
						imovelService.salvarAtualizarImovel(lImovel);
					}else if(!Util.isNullOuVazio(lImovel) && empreendimento.getIndCessionario()){
						lImovel.setIdeEndereco(endereco);
						lImovel.setIdeTipoImovel(new TipoImovel(3));
						lImovel.setIndExcluido(false);
						imovelService.salvarAtualizarImovel(lImovel);
					}
						
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoCompletoDTRP(Endereco endereco, Bairro bairro, Logradouro logradouro) throws Exception {
		salvarEnderecoCompleto(endereco, logradouro, bairro);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Endereco carregarTudo(Endereco endereco) throws Exception {
		return enderecoService.carregar(endereco.getIdeEndereco());
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<TipoLogradouro> listarTipoLogradouro() throws Exception {
		return (List<TipoLogradouro>) tipoLogradouroService.listarTipoLogradouro();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Estado> listarEstados() throws Exception {
		return (List<Estado>) estadoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Municipio> listarMunicipios() throws Exception {
		return (List<Municipio>) municipioService.listarMunicipio();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> filtrarMunicipioByCep(Logradouro logradouro) throws Exception {
		return municipioService.listarMunicipioByCep(logradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> filtrarMunicipiosByEstado(Estado estado) throws Exception {
		return municipioService.filtrarListaMunicipiosPorEstado(estado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoCompletoDeclaracaoInexigibilidade(Endereco endereco, Bairro bairro, Logradouro logradouro) throws Exception {
		salvarEnderecoCompleto(endereco, logradouro, bairro);
	}
}

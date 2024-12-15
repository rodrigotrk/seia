package br.gov.ba.seia.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ImportacaoCdaCefirDAOImpl;
import br.gov.ba.seia.dao.ImportacaoCdaCefirProcessoDAOImpl;
import br.gov.ba.seia.dao.LogradouroDAOImpl;
import br.gov.ba.seia.dao.MunicipioDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.PessoaJuridicaDAOImpl;
import br.gov.ba.seia.dto.ImovelRuralCdaDTO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImportacaoCdaCefir;
import br.gov.ba.seia.entity.ImportacaoCdaCefirProcesso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.TipoCadastroImovelRural;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TipoCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.util.AcessarURL;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.XLSUtil;
import br.gov.ba.seia.validators.CnpjValidator;
import br.gov.ba.seia.validators.CpfValidator;

@Stateless
public class ImportacaoCdaCefirService {

	@Inject
	private ImportacaoCdaCefirDAOImpl daoImpl;
	@Inject
	private IDAO<LocalizacaoGeografica> localizacaoGeograficaDAO;
	@Inject
	private IDAO<DadoGeografico> dadoGeograficoDAO;	
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;
	@Inject
	private PessoaJuridicaDAOImpl pessoaJuridicaDAOImpl;
	@Inject
	private IDAO<ImovelRural> imovelRuralDAO;
	@Inject
	private IDAO<Imovel> imovelDAO;
	@Inject
	private IDAO<PessoaImovel> pessoaImovelDAO;
	@Inject
	private IDAO<Endereco> enderecoDAO;
	@Inject
	private MunicipioDAOImpl municipioDAOImpl;
	@Inject
	private LogradouroDAOImpl logradouroDAOImpl;
	@Inject
	private ImportacaoCdaCefirProcessoDAOImpl importacaoCdaCefirProcessoDAOImpl;
	
	private final int NOME_PROPRIETARIO = 0;
	private final int CPF_CNPJ = 1;
	private final int NUMERO_PROCESSO = 2;
	private final int NOME_IMOVEL = 3;
	private final int AREA_DECLARADA = 4;
	private final int CODIGO_IBGE = 5;
	private final int IDE_LOCALIZACAO = 6;
	private final int OBSERVACAO = 7;
	private final String PLANILHA_ENTRADA = "PLANILHA_ENTRADA";
	private final String SHAPE = "SHAPE";	
	private final String PLANILHA_SAIDA = "PLANILHA_SAIDA";
	
	
	public ImportacaoCdaCefir filtrarById(ImportacaoCdaCefir pImportacaoCdaCefir)  {
		return daoImpl.filtrarById(pImportacaoCdaCefir);
	}

	public ImportacaoCdaCefir salvar(Usuario usuarioLogado)  {
		ImportacaoCdaCefir importacaoCdaCefir = new ImportacaoCdaCefir();
		importacaoCdaCefir.setIdeUsuarioImportacao(usuarioLogado);
		importacaoCdaCefir.setDtcImportacao(new Date());		
		this.daoImpl.salvar(importacaoCdaCefir);
		return importacaoCdaCefir;
	}

	public void atualizar(ImportacaoCdaCefir pImportacaoCdaCefir)  {
		this.daoImpl.atualizar(pImportacaoCdaCefir);
	}
	
	public List<ImportacaoCdaCefir> listarImportacaoCdaCefir()  {		
		return daoImpl.listarImportacaoCdaCefir();		
	}
			
	private List<ImovelRuralCdaDTO> carregarCampos(List<Object[]> listValoresPorLinha) {
		List<ImovelRuralCdaDTO> listImovelRuralCdaDto = new ArrayList<ImovelRuralCdaDTO>();
		for (Object[] object : listValoresPorLinha) {
			ImovelRuralCdaDTO imovelRuralCdaDto = new ImovelRuralCdaDTO();
			
			if(!Util.isNullOuVazio(object[NOME_PROPRIETARIO]))
				imovelRuralCdaDto.setNomProprietarioRequerente(String.valueOf(object[NOME_PROPRIETARIO]));
			if(!Util.isNullOuVazio(object[CPF_CNPJ]))
				imovelRuralCdaDto.setCpfCnpj(Util.replaceString(String.valueOf(object[CPF_CNPJ]), new String[] { "/", ".", "-" }));
			if(!Util.isNullOuVazio(object[NUMERO_PROCESSO]))
				try{
					imovelRuralCdaDto.setNumProcesso(Integer.valueOf(String.valueOf(object[NUMERO_PROCESSO]).replace("-", "")));					
				}catch (Exception e) {
					imovelRuralCdaDto.setNumProcesso(null);
					imovelRuralCdaDto.setIndRejeitado(true);
					imovelRuralCdaDto.setDscObservacao("Dado inválido PROCESSO '" + String.valueOf(object[NUMERO_PROCESSO]) +"'");
				}				
			if(!Util.isNullOuVazio(object[NOME_IMOVEL]))
				imovelRuralCdaDto.setNomImovel(String.valueOf(object[NOME_IMOVEL]));
			if(!Util.isNullOuVazio(object[AREA_DECLARADA]))	
				try{
					imovelRuralCdaDto.setValAreaDeclarada(Double.valueOf(String.valueOf(object[AREA_DECLARADA]).replace(",", ".")));					
				}catch (Exception e) {
					imovelRuralCdaDto.setValAreaDeclarada(null);
					imovelRuralCdaDto.setIndRejeitado(true);
					imovelRuralCdaDto.setDscObservacao("Dado inválido ÁREA DECLARADA '" + String.valueOf(object[AREA_DECLARADA]) +"'");
				}
			if(!Util.isNullOuVazio(object[CODIGO_IBGE]))
				imovelRuralCdaDto.setCodIbgeMunicipio(Double.valueOf(String.valueOf(object[CODIGO_IBGE]).replace(",", ".")));
			if(!Util.isNullOuVazio(object[IDE_LOCALIZACAO]))
				imovelRuralCdaDto.setIdeLocalizacaoGeografica(Integer.valueOf(String.valueOf(object[IDE_LOCALIZACAO])));
			if(!Util.isNullOuVazio(object[OBSERVACAO]))
				imovelRuralCdaDto.setDscObservacao(String.valueOf(object[OBSERVACAO]));
			
			listImovelRuralCdaDto.add(imovelRuralCdaDto);
		}
		return listImovelRuralCdaDto;
	}
	
	private boolean validaCpfCnpj(ImovelRuralCdaDTO imovelRuralCdaDTO){
		try {
			boolean isValido = false;
			if(imovelRuralCdaDTO.getCpfCnpj().length() == 11){
				isValido = isCpfValido(imovelRuralCdaDTO.getCpfCnpj());
			} else if(imovelRuralCdaDTO.getCpfCnpj().length() == 14){
				isValido = isCnpjValido(imovelRuralCdaDTO.getCpfCnpj());				
			} 
			if(!isValido){
				if(!Util.isNullOuVazio(imovelRuralCdaDTO.getDscObservacao())) {
					imovelRuralCdaDTO.setDscObservacao(imovelRuralCdaDTO.getDscObservacao() + " / O CPF ou CNPJ não é válido");
				} else {
					imovelRuralCdaDTO.setDscObservacao("O CPF ou CNPJ não é válido");
				}
			}
			return isValido;
		} catch (Exception e) {
			if(!Util.isNullOuVazio(imovelRuralCdaDTO.getDscObservacao())) {
				imovelRuralCdaDTO.setDscObservacao(imovelRuralCdaDTO.getDscObservacao() + " / O CPF ou CNPJ não é válido");
			} else {
				imovelRuralCdaDTO.setDscObservacao("O CPF ou CNPJ não é válido");
			}
			return false;
		}
	}
	
	private boolean isCpfValido(String cpf)  {		
		if(!CpfValidator.validaCPF(cpf)) {
			return false;
		}
		return true;
	}
	
	private boolean isCnpjValido(String cnpj)  {		
		if(!CnpjValidator.validarCNPJ(cnpj)) {
			return false;
		}
		return true;
	}
	
	private void excluirLocalizacaoGeografica(Integer ideLocalizacaoGeografica)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", ideLocalizacaoGeografica);
		dadoGeograficoDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
		localizacaoGeograficaDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);
	}

	public void validarArquivosImportacao(UploadedFile planilha, UploadedFile dbf, UploadedFile shp, UploadedFile shx) throws Exception  {		
		if(!planilha.getFileName().isEmpty() && !dbf.getFileName().isEmpty() && !shp.getFileName().isEmpty() && !shx.getFileName().isEmpty()){					
			if(!planilha.getFileName().substring(planilha.getFileName().length() - 3).equals("xls")) {
				throw new Exception("Arquivo xls incorreto.");
			}
			if(!dbf.getFileName().substring(dbf.getFileName().length() - 3).equals("dbf")) {
				throw new Exception("Arquivo dbf incorreto.");
			}
			if(!shp.getFileName().substring(shp.getFileName().length() - 3).equals("shp")) {
				throw new Exception("Arquivo shp incorreto.");
			}
			if(!shx.getFileName().substring(shx.getFileName().length() - 3).equals("shx")) {
				throw new Exception("Arquivo shx incorreto.");
			}
		} else {
			throw new Exception("Favor preencher todos os campos obrigatórios.");
		}
	}
	
	public void uploadArquivosImportacao(ImportacaoCdaCefir importacaoCdaCefir, UploadedFile planilha, UploadedFile dbf, UploadedFile shp, UploadedFile shx) throws Exception  {
		importacaoCdaCefir.setCaminhoPlanilhaEntrada(uploadArquivo(importacaoCdaCefir, planilha, PLANILHA_ENTRADA));
		importacaoCdaCefir.setCaminhoDbf(uploadArquivo(importacaoCdaCefir, dbf, SHAPE));
		importacaoCdaCefir.setCaminhoShp(uploadArquivo(importacaoCdaCefir, shp, SHAPE));
		importacaoCdaCefir.setCaminhoShx(uploadArquivo(importacaoCdaCefir, shx, SHAPE));
		criarPlanilhaSaida(importacaoCdaCefir);
	}
	
	private String uploadArquivo(ImportacaoCdaCefir importacaoCdaCefir, UploadedFile arquivo, String nomArquivo){
		return FileUploadUtil.EnviarArquivo(arquivo, DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+importacaoCdaCefir.getIdeImportacaoCdaCefir().toString(),
				importacaoCdaCefir.getIdeImportacaoCdaCefir().toString()+"_"+nomArquivo);			
	}	
	
	public void removerArquivosImportacao(ImportacaoCdaCefir importacaoCdaCefir) {
		FileUploadUtil.removerPastasPorPrefixo(DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString(), importacaoCdaCefir.getIdeImportacaoCdaCefir().toString());		
	}

	public void inserirImoveisCda(ImportacaoCdaCefir importacaoCdaCefir) throws FileNotFoundException, IOException  {	
		List<ImportacaoCdaCefirProcesso> listaImportacaoCdaCefirProcesso = importacaoCdaCefirProcessoDAOImpl.listarImportacaoCdaCefirProcesso();
		List<ImovelRuralCdaDTO> listaImoveisCdaDto = carregarImoveisDaPlanilha(importacaoCdaCefir);
		List<ImovelRuralCdaDTO> listaImoveisCdaDtoRejeitados = new ArrayList<ImovelRuralCdaDTO>();
		for (ImovelRuralCdaDTO imovelRuralCdaDTO : listaImoveisCdaDto) {
			if(validaImovelRuralCdaDTO(imovelRuralCdaDTO, listaImportacaoCdaCefirProcesso)) {
				ImovelRural imovelRural = carregarImovelRuralCda(imovelRuralCdaDTO);
				inserirImovelRuralCda(imovelRural);				
				ImportacaoCdaCefirProcesso importacaoCdaCefirProcesso = new ImportacaoCdaCefirProcesso(imovelRuralCdaDTO.getNumProcesso());
				importacaoCdaCefirProcessoDAOImpl.salvar(importacaoCdaCefirProcesso);
			} else {
				if(!Util.isNullOuVazio(imovelRuralCdaDTO.getIdeLocalizacaoGeografica())) {
					excluirLocalizacaoGeografica(imovelRuralCdaDTO.getIdeLocalizacaoGeografica());
				}
				listaImoveisCdaDtoRejeitados.add(imovelRuralCdaDTO);
			}
		}
		carregarPlanilhaSaida(importacaoCdaCefir, listaImoveisCdaDtoRejeitados);		
	}
	
	private void inserirImovelRuralCda(ImovelRural imovelRural)  {
		 
		Imovel	imovel = imovelRural.getImovel();
		imovel.setImovelRural(null);
		enderecoDAO.salvarOuAtualizar(imovel.getIdeEndereco());
		imovelDAO.salvar(imovel);
		imovelRural.setImovel(imovel);
		imovelRuralDAO.salvar(imovelRural);
		pessoaImovelDAO.salvarOuAtualizar(imovelRural.getImovel().getPessoaImovelCollection().iterator().next());		
	}

	private ImovelRural carregarImovelRuralCda(ImovelRuralCdaDTO imovelRuralCdaDTO) {
		ImovelRural imovelRural = new ImovelRural();
		Imovel imovel = new Imovel(null, new Date(), false);
		imovel.setIdeTipoImovel(new TipoImovel(1));		
		PessoaImovel pessoaImovel = new PessoaImovel(null, new Date(), false);
		imovelRural.setIdeTipoCadastroImovelRural(new TipoCadastroImovelRural(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_CDA.getTipo()));
		imovelRural.setIdeRequerenteCadastro(inserirPessoaRequerenteImovelRuralCda(imovelRuralCdaDTO));
		imovelRural.setDesDenominacao(imovelRuralCdaDTO.getNomImovel());
		imovelRural.setValArea(imovelRuralCdaDTO.getValAreaDeclarada());
		imovelRural.setIdeLocalizacaoGeografica(new LocalizacaoGeografica(imovelRuralCdaDTO.getIdeLocalizacaoGeografica()));
		imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId());
		imovelRural.setIndImovelRuralCdaEditado(false);
		pessoaImovel.setIdeImovel(imovel);
		pessoaImovel.setIdePessoa(imovelRural.getIdeRequerenteCadastro());
		pessoaImovel.setIdeTipoVinculoImovel(new TipoVinculoImovel(1));
		imovel.setPessoaImovelCollection(new ArrayList<PessoaImovel>());
		imovel.getPessoaImovelCollection().add(pessoaImovel);
		imovel.setIdeEndereco(carregarEnderecoImovelRuralCda(imovelRuralCdaDTO));
		imovelRural.setImovel(imovel);
		return imovelRural;
	}
	private Endereco carregarEnderecoImovelRuralCda(ImovelRuralCdaDTO imovelRuralCdaDTO)  {
		Municipio municipio = municipioDAOImpl.buscarMunicipioByCodigoIbge(imovelRuralCdaDTO.getCodIbgeMunicipio());
		Logradouro logradouro = logradouroDAOImpl.listarLogradourosByMunicipio(municipio).iterator().next();
		Endereco endereco = new Endereco(null, new Date(), false);
		endereco.setIdeLogradouro(logradouro);
		return endereco;
	}

	private Pessoa inserirPessoaRequerenteImovelRuralCda(ImovelRuralCdaDTO imovelRuralCdaDTO)  {
		Pessoa pessoa = new Pessoa();
		if(imovelRuralCdaDTO.getCpfCnpj().length() == 11){			 
			PessoaFisica pessoaFisica = pessoaFisicaDAOImpl.filtrarPessoaFisicaByCpf(new PessoaFisica(imovelRuralCdaDTO.getCpfCnpj()));
			if(Util.isNullOuVazio(pessoaFisica)){			
				pessoaFisica = new PessoaFisica();
				pessoaFisica.setNomPessoa(imovelRuralCdaDTO.getNomProprietarioRequerente());
				pessoaFisica.setNumCpf(imovelRuralCdaDTO.getCpfCnpj());
				pessoa.setDtcCriacao(new Date());
				pessoa.setIndExcluido(Boolean.FALSE);
				pessoa.setPessoaFisica(pessoaFisica);
				pessoaFisica.setPessoa(pessoa);
				pessoaFisica = pessoaFisicaDAOImpl.salvarOuAtualizarPessoaFisica(pessoaFisica);				
			}
			pessoa = pessoaFisica.getPessoa();
			pessoa.setPessoaFisica(pessoaFisica);
		} else {
			PessoaJuridica pessoaJuridica = pessoaJuridicaDAOImpl.filtrarPessoaJuridicaByCnpj(new PessoaJuridica(imovelRuralCdaDTO.getCpfCnpj()));
			if(Util.isNullOuVazio(pessoaJuridica)){
				pessoaJuridica = new PessoaJuridica(); 
				pessoaJuridica.setNomRazaoSocial(imovelRuralCdaDTO.getNomProprietarioRequerente());
				pessoaJuridica.setNumCnpj(imovelRuralCdaDTO.getCpfCnpj());
				pessoa.setDtcCriacao(new Date());
				pessoa.setIndExcluido(Boolean.FALSE);
				pessoa.setPessoaJuridica(pessoaJuridica);
				pessoaJuridica.setPessoa(pessoa);
				pessoaJuridica = pessoaJuridicaDAOImpl.salvarOuAtualizarPessoaJuridica(pessoaJuridica);				
			}
			pessoa = pessoaJuridica.getPessoa();
			pessoa.setPessoaJuridica(pessoaJuridica);
		}
		return pessoa;
	}

	private boolean validaImovelRuralCdaDTO(ImovelRuralCdaDTO imovelRuralCdaDTO, List<ImportacaoCdaCefirProcesso> listaImportacaoCdaCefirProcesso) {
		if(validaDuplicacaoImoveiCda(imovelRuralCdaDTO, listaImportacaoCdaCefirProcesso)) {
			boolean validacaoCamposObrigatorios = validaCamposObrigatorios(imovelRuralCdaDTO);
			boolean validacaoCpfCnpj = validaCpfCnpj(imovelRuralCdaDTO);
			return validacaoCamposObrigatorios && validacaoCpfCnpj && !imovelRuralCdaDTO.isIndRejeitado();
		} else {
			return false;
		}		
	}

	private boolean validaDuplicacaoImoveiCda(ImovelRuralCdaDTO imovelRuralCdaDTO, List<ImportacaoCdaCefirProcesso> listaImportacaoCdaCefirProcesso) {
		for (ImportacaoCdaCefirProcesso importacaoCdaCefirProcesso : listaImportacaoCdaCefirProcesso) {
			if(importacaoCdaCefirProcesso.getNumProcessoImportacaoCdaCefir().equals(imovelRuralCdaDTO.getNumProcesso())) {
				if(!Util.isNullOuVazio(imovelRuralCdaDTO.getDscObservacao())) {
					imovelRuralCdaDTO.setDscObservacao(imovelRuralCdaDTO.getDscObservacao() + " / Processo já cadastrado");
				} else {
					imovelRuralCdaDTO.setDscObservacao("Processo já cadastrado");
				}
				
				return false;
			}
		}		
		return true;
	}

	private boolean validaCamposObrigatorios(ImovelRuralCdaDTO imovelRuralCdaDTO) {
		boolean isValido = true;
		String errorMensage = "";
		if(Util.isNullOuVazio(imovelRuralCdaDTO.getIdeLocalizacaoGeografica())) {
			errorMensage += "Falha na importação do shape"; 
			isValido = false;
		}		
		if(Util.isNullOuVazio(imovelRuralCdaDTO.getNomProprietarioRequerente()) || 
				Util.isNullOuVazio(imovelRuralCdaDTO.getCpfCnpj()) ||
				Util.isNullOuVazio(imovelRuralCdaDTO.getNumProcesso()) ||
				Util.isNullOuVazio(imovelRuralCdaDTO.getNomImovel()) ||
				Util.isNullOuVazio(imovelRuralCdaDTO.getValAreaDeclarada()) ||
				Util.isNullOuVazio(imovelRuralCdaDTO.getCodIbgeMunicipio())){
			if(!errorMensage.isEmpty()) {
				imovelRuralCdaDTO.setDscObservacao(" / ");
			}
			errorMensage += "Campo obrigatório não informado"; 
			isValido = false;
		}
		
		if(!Util.isNullOuVazio(imovelRuralCdaDTO.getDscObservacao())) {
			imovelRuralCdaDTO.setDscObservacao(imovelRuralCdaDTO.getDscObservacao() + " / " + errorMensage);
		} else {
			imovelRuralCdaDTO.setDscObservacao(errorMensage);
		}
		
		return isValido;
	}

	private List<ImovelRuralCdaDTO> carregarImoveisDaPlanilha(ImportacaoCdaCefir importacaoCdaCefir) throws IOException  {
		String pathNomeArquivo = importacaoCdaCefir.getCaminhoPlanilhaEntrada();
		List<Object[]> listValoresPorLinha = XLSUtil.getTodosValoresPorLinha(pathNomeArquivo, 0);
		return carregarCampos(listValoresPorLinha);
	}
	
	private void criarPlanilhaSaida(ImportacaoCdaCefir importacaoCdaCefir) throws Exception {
		File planilhaSaida = new File(DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+importacaoCdaCefir.getIdeImportacaoCdaCefir().toString()+File.separator+importacaoCdaCefir.getIdeImportacaoCdaCefir().toString()+"_"+PLANILHA_SAIDA+".xls");

		InputStream in = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/FormatoArquivoImportacaoCda.xls");
		FileOutputStream out = new FileOutputStream(planilhaSaida);
        
        FileUploadUtil.moverArquivo(in, out);
        
        if(!Util.isNull(in)){
			in.close();
			in = null;
		}
		
		if(!Util.isNull(out)){
			out.close();
			out = null;
		}
	}
	
	private void carregarPlanilhaSaida(ImportacaoCdaCefir importacaoCdaCefir, List<ImovelRuralCdaDTO> listaImoveisCdaDtoRejeitado) throws FileNotFoundException, IOException{
		List<Object[]> listaDados = new ArrayList<Object[]>();		
		for (ImovelRuralCdaDTO imovelRuralCdaDto : listaImoveisCdaDtoRejeitado) {
			Object[] dado = {
				imovelRuralCdaDto.getNomProprietarioRequerente(),
				imovelRuralCdaDto.getCpfCnpj(),
				imovelRuralCdaDto.getNumProcesso(),
				imovelRuralCdaDto.getNomImovel(),
				imovelRuralCdaDto.getValAreaDeclarada(),
				imovelRuralCdaDto.getCodIbgeMunicipio(),
				imovelRuralCdaDto.getIdeLocalizacaoGeografica(),
				imovelRuralCdaDto.getDscObservacao()
			};
			listaDados.add(dado);
		}		
		XLSUtil.inserirDadosPlanilhaExistente(DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+importacaoCdaCefir.getIdeImportacaoCdaCefir().toString()+File.separator+importacaoCdaCefir.getIdeImportacaoCdaCefir().toString()+"_"+PLANILHA_SAIDA+".xls", listaDados, 1, 0);
	}
	
	public void persistirShapeTheGeomImportacaoCdaCefir(Integer ideImportacaoCdaCefir) throws Exception  {		
		Map<String, String> parametros = null;
		//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
		parametros = new HashMap<String, String>();
		parametros.put("id", ideImportacaoCdaCefir.toString());
		String[] retorno = AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR_CDA.getUrl(parametros));
		if (retorno != null) {
			if (retorno.length > 0){
				if(retorno[0].equalsIgnoreCase("ERRO")){
					throw new Exception(retorno[2]+" ["+retorno[1]+"]");
				} else {
					JSONArray localizacoes = new JSONArray(retorno[1]);
					List<Object[]> dados = new ArrayList<Object[]>();
					for (int i = 0; i < localizacoes.length(); i++) {
						Object[] dado = {
							((JSONObject)localizacoes.get(i)).get("ide_loc"),
							((JSONObject)localizacoes.get(i)).get("cod")
						};
						dados.add(dado);
					}
					XLSUtil.atualizarColunaLocalizacaoObservacaoImportacaoCda(DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+ideImportacaoCdaCefir.toString()+File.separator+ideImportacaoCdaCefir.toString()+"_"+PLANILHA_ENTRADA+".xls", dados);
					
				}
			}
		}		 
	 }
	
}

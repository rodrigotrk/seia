package br.gov.ba.seia.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.PctFamiliaDAOImpl;
import br.gov.ba.seia.dto.PctDTO;
import br.gov.ba.seia.dto.PctPessoaFisicaNovosDTO;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.PctFamilia;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PaisEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.util.CSVUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.validators.CpfValidator;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PctFamiliaService {

	@EJB
	private ImovelRuralFacade imovelRuralFacade;

	@EJB
	private PessoaService pessoaService;

	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@Inject
	private PctFamiliaDAOImpl pctFamiliaDAOImpl;

	@EJB
	private AuditoriaFacade auditoria;

	@EJB
	private PessoaJuridicaPctService pessoaJuridicaPctService;

	@EJB
	private PctFamiliaService pctFamiliaService;

	private static final String CAMINHO_PLANILHA_PCT = "/opt/ARQUIVOS/PCT/PLANILHA/modelo_cadastro_familia_pct.csv";

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarPessoaFisicaPct(PessoaFisica pctPessoaFisica) throws Exception {
		Pessoa pessoa = pctPessoaFisica.getPessoa();

		if (Util.isNullOuVazio(pessoa.getIdePessoa())) {
			pessoa = new Pessoa();
			pessoa.setDtcCriacao(new Date());
			pessoa.setIndExcluido(false);
			pctPessoaFisica.setPessoa(pessoa);
		}

		pctPessoaFisica.setIdePais(new Pais(PaisEnum.BRASIL.getId()));
		pessoaFisicaService.salvarOuAtualizarPessoaFisica(pctPessoaFisica);
		if(Util.isNullOuVazio(pctPessoaFisica.getPessoa().getPessoaFisica())) {
			pctPessoaFisica.getPessoa().setPessoaFisica(pctPessoaFisica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void adicionarPctProprietarioPossuidor(PctImovelRural pctImovelRural, PessoaFisica pctPessoaFisica,
			boolean indRepresentanteFamilia) throws Exception {
		PctDTO pctDTO = new PctDTO();

		if (Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
			pctDTO.setPessoaFisicaRepresentantes(new ArrayList<PessoaFisica>());
		}

		if (Util.isNullOuVazio(pctDTO.getPessoaFisicas())) {
			pctDTO.setPessoaFisicas(new ArrayList<PessoaFisica>());
		}

		if (Util.isNull(pctImovelRural.getPctFamiliaCollection())) {
			pctImovelRural.setPctFamiliaCollection(new ArrayList<PctFamilia>());
		}

		for (PctFamilia pctFamilia : pctImovelRural.getPctFamiliaCollection()) {
			pctDTO.getPessoaFisicaRepresentantes().add(pctFamilia.getIdePessoa().getPessoaFisica());
		}

		if (Util.isNullOuVazio(pctPessoaFisica.getPessoa())) {
			Pessoa pessoa = new Pessoa();
			pessoa.setDtcCriacao(new Date());
			pessoa.setIndExcluido(false);
			pessoa.setPessoaFisica(pctPessoaFisica);
			pctPessoaFisica.setPessoa(pessoa);
		}

		if (indRepresentanteFamilia) {
			pctDTO.getPessoaFisicaRepresentantes().add(pctPessoaFisica);
		} else {
			pctPessoaFisica.setCpfRepresentante(pctImovelRural.getPessoaRepresentanteFamiliaSelecionada().getIdePessoa()
					.getPessoaFisica().getNumCpf());
			pctDTO.getPessoaFisicas().add(pctPessoaFisica);
		}

		Collection<PctFamilia> pctFamiliaLista = ajustarPctFamilia(pctDTO, pctImovelRural);

		pctImovelRural.setPctFamiliaCollection(pctFamiliaLista);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> listarPctFamilia(PctImovelRural pctImovelRural) throws Exception {
		return pctFamiliaDAOImpl.listarPctFamilia(pctImovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PctFamilia obterPctFamiliaPorCpf(PctImovelRural pctImovelRural, PessoaFisica pessoaFisica) throws Exception {
		return pctFamiliaDAOImpl.obterPctFamiliaPorCpf(pctImovelRural, pessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> ListarMembrosPorRepresentante(PctFamilia pctFamilia) throws Exception {
		return pctFamiliaDAOImpl.listarMembrosFamiliaPorRepresentante(pctFamilia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPctFamilia(PctFamilia pctFamilia) throws Exception {
		pctFamilia.setIndExcluido(true);
		pctFamilia.setDtcExclusao(new Date());
		pctFamiliaDAOImpl.salvarOuAtualizar(pctFamilia);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void adicionarPctFamiliaCSV(FileUploadEvent event, PctImovelRural pctImovelRural, List<String> lista)
			throws Exception {
		String caminho = null;
		if (Util.isNullOuVazio(pctImovelRural.getIdePctImovelRural())) {
			caminho = DiretorioArquivoEnum.PCT.toString() + File.separator + "TMP";
		} else {
			caminho = DiretorioArquivoEnum.PCT.toString() + File.separator + pctImovelRural.getIdePctImovelRural();
		}
		String desCaminhoArquivoDecreto = FileUploadUtil.Enviar(event, caminho);

		if (Util.isNullOuVazio(pctImovelRural.getListarPctRepresentanteFamiliaCollection())) {
			pctImovelRural.setListarPctRepresentanteFamiliaCollection(new ArrayList<PctFamilia>());
		}

		PctDTO pctDTO = separarRepresentantePessoaFisica(pctImovelRural, desCaminhoArquivoDecreto, lista);

		Collection<PctFamilia> pctFamiliaLista = ajustarPctFamilia(pctDTO, pctImovelRural);

		pctImovelRural.setPctFamiliaCollection(pctFamiliaLista);
	}	

	/**
	 * Ajustar a a lista de PctFamilia para exibição na grid
	 * 
	 * @param PctDTO         pctDTO
	 * @param PctImovelRural pctImovelRural
	 * @return List<PctFamilia>
	 * @throws Exception 
	 */
	private Collection<PctFamilia> ajustarPctFamilia(PctDTO pctDTO, PctImovelRural pctImovelRural) throws Exception {

		Collection<PctFamilia> pctFamiliaLista = pctImovelRural.getPctFamiliaCollection();
		PctPessoaFisicaNovosDTO pctPessoaFisicaNovosDTO = verificarPessoaFisicaCadastradaSemFamilia(pctDTO, pctImovelRural);

		if (!Util.isNullOuVazio(pctImovelRural.getListarPctRepresentanteFamiliaCollection())
				&& !Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
			for (PctFamilia pctFamilia : pctFamiliaLista) {
				if(!pctPessoaFisicaNovosDTO.getListaCpfArquivo().contains(pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf())) {
					pctPessoaFisicaNovosDTO.getPessoaFisicaRepsNovos().add(pctFamilia.getIdePessoa().getPessoaFisica());
				}
			}
		}
		
		if (Util.isNullOuVazio(pctFamiliaLista)) {
			pctFamiliaLista = new ArrayList<PctFamilia>();
		}
		
		if(!Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
			for (PessoaFisica pessoaFisicaRep : pctPessoaFisicaNovosDTO.getPessoaFisicaRepsNovos()) {
				PctFamilia pctFamiliaRep = new PctFamilia();

				if (Util.isNullOuVazio(pctFamiliaRep.getIdePctFamilia())) {
					pctFamiliaRep.setDtcCadastro(new Date());
					pctFamiliaRep.setIdePctImovelRural(pctImovelRural);
					pctFamiliaRep.setIndExcluido(false);
					pctFamiliaRep.setIdePessoa(pessoaFisicaRep.getPessoa());
				}

				if (Util.isNullOuVazio(pctFamiliaRep.getMembrosFamiliaCollection())) {
					pctFamiliaRep.setMembrosFamiliaCollection(new ArrayList<PctFamilia>());
				}

				ajustarMembrosFamiliaPct(pctDTO, pctImovelRural, pessoaFisicaRep, pctFamiliaRep, pctPessoaFisicaNovosDTO.getPessoaFisicaMembrosNovos());
				adicionarRepresentanteFamilia(pctImovelRural, pctFamiliaLista, pctFamiliaRep);
			}
			ajustarRepresentantesASeremExcluidos(pctDTO, pctFamiliaLista); //remover
		} else if(Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes()) && !Util.isNullOuVazio(pctDTO.getPessoaFisicas())) {
				PctFamilia pctFamiliaRep = new PctFamilia();
				Collection<PctFamilia> listaPctFamiliasAtualizadas = new ArrayList<PctFamilia>();
				for (PessoaFisica pessoaFisica : pctPessoaFisicaNovosDTO.getPessoaFisicaMembrosNovos()) {
					for(PctFamilia pctFamiliaRep2 : pctImovelRural.getListarPctRepresentanteFamiliaCollection()) {
						if(pctFamiliaRep2.getIdePessoa().getPessoaFisica().getNumCpf().equals(pessoaFisica.getCpfRepresentante())){
							Collection<PctFamilia> pctFamiliaComMembros = new ArrayList<PctFamilia>();
							try {
								pctFamiliaComMembros = pctFamiliaService.ListarMembrosPorRepresentante(pctFamiliaRep2);
							} catch (Exception e) {
								e.printStackTrace();
							}
							pctFamiliaRep2.setMembrosFamiliaCollection(pctFamiliaComMembros);
							pctFamiliaRep = pctFamiliaRep2;
						}
					}
					
					PctFamilia pctFamilia = new PctFamilia();
					
					pctFamilia.setDtcCadastro(new Date());
					pctFamilia.setIdePctImovelRural(pctImovelRural);
					pctFamilia.setIndExcluido(false);
					pctFamilia.setIdePessoa(pessoaFisica.getPessoa());
					
					if(Util.isNullOuVazio(pctFamiliaRep.getMembrosFamiliaCollection())) {
						pctFamiliaRep.setMembrosFamiliaCollection(new ArrayList<PctFamilia>());
					}
					
					if (isAdicionadoRepresentante(pctFamiliaRep.getMembrosFamiliaCollection(), pctFamilia)) {
						pctFamiliaRep.getMembrosFamiliaCollection().add(pctFamilia);
					}
					
					listaPctFamiliasAtualizadas.add(pctFamiliaRep);
					if(pctFamiliaLista.contains(pctFamiliaRep)) {
						pctFamiliaLista.remove(pctFamiliaRep);
						pctFamiliaLista.add(pctFamiliaRep);
					} else {
						pctFamiliaLista.add(pctFamiliaRep);
					}
				}
			}

		return pctFamiliaLista;
	}
	
	private void ajustarRepresentantesASeremExcluidos(PctDTO pctDTO, Collection<PctFamilia> pctFamiliaLista) {
		List <PessoaFisica> listaPessoasFisicas = pctDTO.getPessoaFisicas();
		PctFamilia pctFamiliaRepresentanteEncontrado = null;
		for(PessoaFisica pessoaFisica: listaPessoasFisicas) {
			for(PctFamilia pctFamiliaRepresentante : pctFamiliaLista) {
				if(pessoaFisica.getNumCpf().equals(pctFamiliaRepresentante.getIdePessoa().getPessoaFisica().getNumCpf())) {
					pctFamiliaRepresentanteEncontrado = pctFamiliaRepresentante;
				}
			}
			if(!Util.isNullOuVazio(pctFamiliaRepresentanteEncontrado)) {
				pctFamiliaLista.remove(pctFamiliaRepresentanteEncontrado);
				pctFamiliaRepresentanteEncontrado = null;
			}
		}
	}

	private void adicionarRepresentanteFamilia(PctImovelRural pctImovelRural, Collection<PctFamilia> pctFamiliaLista,
			PctFamilia pctFamiliaRep) {
		if (isAdicionadoRepresentante(pctFamiliaLista, pctFamiliaRep)) {
			pctFamiliaLista.add(pctFamiliaRep);
		} else {
			atualizarRepresentanteFamilia(pctFamiliaLista, pctFamiliaRep);
		}

		if (isAdicionadoRepresentante(pctImovelRural.getListarPctRepresentanteFamiliaCollection(),
				pctFamiliaRep)) {
			pctImovelRural.getListarPctRepresentanteFamiliaCollection().add(pctFamiliaRep);
		}
	}

	private void ajustarMembrosFamiliaPct(PctDTO pctDTO, PctImovelRural pctImovelRural, PessoaFisica pessoaFisicaRep,
			PctFamilia pctFamiliaRep, Collection<PessoaFisica> pessoaFisicaMembrosNovos) throws Exception {
		for (PessoaFisica pessoaFisica : pessoaFisicaMembrosNovos) {
			if (pessoaFisicaRep.getNumCpf().equals(pessoaFisica.getCpfRepresentante())) {
				
				PctFamilia pctFamilia = pctFamiliaDAOImpl.obterPessoaPctFamilia(pessoaFisica.getNumCpf());
				if(Util.isNullOuVazio(pctFamilia)) {
					pctFamilia = new PctFamilia();
					pctFamilia.setDtcCadastro(new Date());
					pctFamilia.setIdePctImovelRural(pctImovelRural);
					pctFamilia.setIndExcluido(false);
					pctFamilia.setIdePessoa(pessoaFisica.getPessoa());
				}

				if (isAdicionadoRepresentante(pctFamiliaRep.getMembrosFamiliaCollection(), pctFamilia)) {
					pctFamiliaRep.getMembrosFamiliaCollection().add(pctFamilia);
				} else {
					atualizarMembroFamilia(pctFamiliaRep.getMembrosFamiliaCollection(), pctFamilia);
				}
			}
		}
	}

	private void atualizarMembroFamilia(Collection<PctFamilia> membrosFamilia, PctFamilia pctFamilia) {
		for (PctFamilia pctFamiliaMembro : membrosFamilia) {
			if(pctFamiliaMembro.getIdePessoa().getPessoaFisica().getNumCpf().equals(pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf())) {
				pctFamiliaMembro = pctFamilia;
			}
		}
		
	}

	private boolean existePessoaPctFamiliaNaComunidade(PessoaFisica pctPessoaFisicaSelecionado, PctImovelRural pctImovelRural) {
		if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
			for (Iterator<PctFamilia> i = pctImovelRural.getPctFamiliaCollection().iterator(); i.hasNext();) {
				PctFamilia pctFamilia1 = i.next();
				if (pctPessoaFisicaSelecionado.getNumCpf()
						.equals(pctFamilia1.getIdePessoa().getPessoaFisica().getNumCpf())) {
					return true;
				} else {
					for (Iterator<PctFamilia> j = pctFamilia1.getMembrosFamiliaCollection().iterator(); j.hasNext();) {
						PctFamilia pctFamilia2 = j.next();
						if (pctPessoaFisicaSelecionado.getNumCpf()
								.equals(pctFamilia2.getIdePessoa().getPessoaFisica().getNumCpf())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private PctPessoaFisicaNovosDTO verificarPessoaFisicaCadastradaSemFamilia(PctDTO pctDTO, PctImovelRural pctImovelRural) throws Exception {
		
		PctPessoaFisicaNovosDTO pctPessoaFisicaNovosDTO = new PctPessoaFisicaNovosDTO();
		
		if (!Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
			for (PessoaFisica pessoaFisica : pctDTO.getPessoaFisicaRepresentantes()) {
				if (!existePessoaPctFamiliaNaComunidade(pessoaFisica, pctImovelRural)) {
					PessoaFisica pessoaFisicaNaoCadastradoFamilia = pessoaFisicaService.consultarPessoaFisicaByNumCpf(pessoaFisica.getNumCpf());
					pctPessoaFisicaNovosDTO.getListaCpfArquivo().add(pessoaFisica.getNumCpf());
					verificarRegistroFamiliaPct(pctPessoaFisicaNovosDTO.getPessoaFisicaRepsNovos(), pessoaFisica, pessoaFisicaNaoCadastradoFamilia);
				}
			}
		}
			
		if (!Util.isNullOuVazio(pctDTO.getPessoaFisicas())) {
			for (PessoaFisica pessoaFisica: pctDTO.getPessoaFisicas()) {
				if (!existePessoaPctFamiliaNaComunidade(pessoaFisica, pctImovelRural)) {
					PessoaFisica pessoaFisicaNaoCadastradoFamilia = pessoaFisicaService.consultarPessoaFisicaByNumCpf(pessoaFisica.getNumCpf());
					verificarRegistroFamiliaPct(pctPessoaFisicaNovosDTO.getPessoaFisicaMembrosNovos(), pessoaFisica, pessoaFisicaNaoCadastradoFamilia);
				}
			}
		}
		
		return pctPessoaFisicaNovosDTO;
	}

	private void verificarRegistroFamiliaPct(Collection<PessoaFisica> pessoaFisicaNovos, PessoaFisica pessoaFisica,
			PessoaFisica pessoaFisicaNaoCadastradoFamilia) {
		if(Util.isNullOuVazio(pessoaFisicaNaoCadastradoFamilia)) {
			pessoaFisicaNovos.add(pessoaFisica);
		} else {
			pessoaFisicaNaoCadastradoFamilia.setNomPessoa(pessoaFisica.getNomPessoa());
			pessoaFisicaNaoCadastradoFamilia.setNomMae(pessoaFisica.getNomMae());
			pessoaFisicaNaoCadastradoFamilia.setDtcNascimento(pessoaFisica.getDtcNascimento());
			pessoaFisicaNaoCadastradoFamilia.setCpfRepresentante(pessoaFisica.getCpfRepresentante());
			pessoaFisicaNovos.add(pessoaFisicaNaoCadastradoFamilia);
		}
	}

	private boolean isAdicionadoRepresentante(Collection<PctFamilia> pctFamiliaLista, PctFamilia pctFamiliaRep) {
			for (PctFamilia pctFamilia : pctFamiliaLista) {
				if (pctFamiliaRep.getIdePessoa().getPessoaFisica().getNumCpf()
						.equals(pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf())) {
					return false;
				}
			}
		return true;
	}

	private void ajustaMembrosFamilia(PctFamilia pctFamiliaRep, PctFamilia pctFamilia) {
		Collection<PctFamilia> pctFamiliaRepMembros = pctFamiliaRep.getMembrosFamiliaCollection();
		Collection<PctFamilia> pctFamiliaMembros = pctFamilia.getMembrosFamiliaCollection();		
		//Remove o membro que já pertence a família e está persistido no banco
		for(PctFamilia pctFamiliaRepMembro : pctFamiliaRepMembros) {
			boolean temMembro = false;
			for(PctFamilia pctFamiliaMembro : pctFamiliaMembros) {
				if(pctFamiliaRepMembro.getIdePessoa().equals(pctFamiliaMembro.getIdePessoa())){
					temMembro = true;
				}				
			}
			if(!temMembro) {
				pctFamilia.getMembrosFamiliaCollection().add(pctFamiliaRepMembro);
			}
		}		
	}
	
	private void atualizarRepresentanteFamilia(Collection<PctFamilia> pctFamiliaLista, PctFamilia pctFamiliaRep) {
		for (PctFamilia pctFamilia : pctFamiliaLista) {
			if (pctFamiliaRep.getIdePessoa().equals(pctFamilia.getIdePessoa())) {
				pctFamilia.setIdePessoa(pctFamiliaRep.getIdePessoa());
				ajustaMembrosFamilia(pctFamiliaRep, pctFamilia);
			}
		}
	}
	
	private String verificaETrataCpf(String cpf) {
		int tamanhoCpf = cpf.length();
		if(tamanhoCpf>11) {
			int quantidadeCaracteresAMais = tamanhoCpf - 11;
			return cpf.substring(quantidadeCaracteresAMais);
		}
		return cpf;
	}

	/**
	 * separa as pessoas fisicas que são representantes de pessoas que não são
	 * 
	 * @param PctImovelRural pctImovelRural
	 * @param String         desCaminhoArquivoDecreto
	 * @throws Exception
	 */
	private PctDTO separarRepresentantePessoaFisica(PctImovelRural pctImovelRural, String desCaminhoArquivoDecreto,
			List<String> lista) throws Exception {
		PctDTO pctDTO = new PctDTO();
		List<String[]> linhaCSV = CSVUtil.readFilePCT(desCaminhoArquivoDecreto);

		int i = 0;
		for (Iterator<String[]> iterator = linhaCSV.iterator(); iterator.hasNext();) {
			String[] colunas = (String[]) iterator.next();

			if (i != 0) {
				if (colunas.length >= 4) {

					String cpfCSV = StringUtils
							.leftPad(Util.replaceString(colunas[0].trim(), new String[] { ".", "-" }, ""), 11, "0");
					
					cpfCSV = verificaETrataCpf(cpfCSV);

					PessoaFisica pessoaFisicaCSV = new PessoaFisica();
					pessoaFisicaCSV.setNumCpf(cpfCSV);
					pessoaFisicaCSV.setNomPessoa(colunas[2]);
					pessoaFisicaCSV.setNomMae(colunas[3]);

					if (colunas.length == 5) {
						pessoaFisicaCSV.setCpfRepresentante(verificaETrataCpf(StringUtils.leftPad(
								Util.replaceString(colunas[4].trim(), new String[] { ".", "-" }, ""), 11, "0")));
						if(pessoaFisicaCSV.getNumCpf().equals(pessoaFisicaCSV.getCpfRepresentante())){
							pessoaFisicaCSV.setCpfRepresentante(new String());
						}
					}

					boolean retorno = validarDadosCsv(pctImovelRural, pessoaFisicaCSV, colunas[1], pctDTO, lista);

					if (!retorno) {
						i++;
						continue;
					}

					if (Util.isNullOuVazio(pessoaFisicaCSV.getPessoa())) {
						Pessoa pessoa = new Pessoa();
						pessoa.setDtcCriacao(new Date());
						pessoa.setIndExcluido(false);
						pessoa.setPessoaFisica(pessoaFisicaCSV);
						pessoaFisicaCSV.setPessoa(pessoa);
					}

					if (Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
						pctDTO.setPessoaFisicaRepresentantes(new ArrayList<PessoaFisica>());
					}

					if (Util.isNullOuVazio(pctDTO.getPessoaFisicas())) {
						pctDTO.setPessoaFisicas(new ArrayList<PessoaFisica>());
					}

					if (!Util.isNullOuVazio(pessoaFisicaCSV.getCpfRepresentante())) {
						pctDTO.getPessoaFisicas().add(pessoaFisicaCSV);
					} else {
						pctDTO.getPessoaFisicaRepresentantes().add(pessoaFisicaCSV);
					}
				} else {
					throw new SeiaValidacaoRuntimeException("O arquivo CSV não se encontra no padrão válido.");
				}
			}
			i++;
		}

		return pctDTO;
	}

	private boolean validarDadosCsv(PctImovelRural pctImovelRural, PessoaFisica pessoaFisicaCSV, String dataNascimento,
			PctDTO pctDTO, List<String> lista) throws Exception {
		boolean existePessoaPctAssociada = existePessoaPctFamilia(pessoaFisicaCSV.getNumCpf(), pctImovelRural.getIdePctImovelRural());

		boolean existePessoaFisicaInativa = consultarPessoaFisicaInativa(pessoaFisicaCSV);

		boolean existePessoaAdicionadaPlanilha = existePessoaPctFamilia(pessoaFisicaCSV, pctImovelRural, false);

		if (existePessoaPctAssociada) {
			String msg = pessoaFisicaCSV.getNumCpfFormatado()
					.concat(";membro já está associado a uma família de outra comunidade.");
			lista.add(msg);
			return false;
		}
		
		if (existePessoaFisicaInativa) {
			String msg = pessoaFisicaCSV.getNumCpfFormatado()
					.concat(";cpf com o cadastro inativado no sistema.");
			lista.add(msg);
			return false;
		}

		if (existePessoaAdicionadaPlanilha) {
			String msg = pessoaFisicaCSV.getNumCpfFormatado().concat(";membro já adicionado anteriormente.");
			lista.add(msg);
			return false;
		}

		if (!Util.isNullOuVazio(pessoaFisicaCSV.getCpfRepresentante())) {
			if (!CpfValidator.validaCPF(pessoaFisicaCSV.getCpfRepresentante())) {
				String msg = pessoaFisicaCSV.getNumCpfFormatado().concat(";O CPF ")
						.concat((pessoaFisicaCSV.getNumCpfFormatadoRepresentante().length() == 11)
								? pessoaFisicaCSV.getNumCpfFormatadoRepresentante()
								: pessoaFisicaCSV.getCpfRepresentante())
						.concat(" do representante indicado para esse membro não é válido.");
				lista.add(msg);
				return false;
			}

			boolean existePessoaPctFamilia = existePessoaPctFamilia(pessoaFisicaCSV, pctImovelRural, true);
			
			boolean existePessoaAdicionada = existePessoaAdicionada(pessoaFisicaCSV, pctDTO, true);
			
			if ((!(existePessoaPctFamilia || existePessoaAdicionada))) {
				String msg = pessoaFisicaCSV.getNumCpfFormatado().concat(";O CPF ")
						.concat(pessoaFisicaCSV.getNumCpfFormatadoRepresentante())
						.concat(" do representante indicado para esse membro não foi encontrado na lista de membros da comunidade.");
				lista.add(msg);
				return false;
			}
		}

		boolean retorno = validarPctPessoaFisicaPlanilha(pctImovelRural, pessoaFisicaCSV, dataNascimento, true, lista);

		if (!retorno) {
			return retorno;
		}

		return true;
	}

	private boolean consultarPessoaFisicaInativa(PessoaFisica pessoaFisicaCSV) {
		return !Util.isNullOuVazio(pessoaFisicaService.consultarPessoaFisicaInativaByNumCpf(pessoaFisicaCSV.getNumCpf()));
	}

	private boolean existePessoaPctFamilia(PessoaFisica pctPessoaFisicaSelecionado, PctImovelRural pctImovelRural,
			boolean isRepresentante) {
		if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
			for (Iterator<PctFamilia> i = pctImovelRural.getPctFamiliaCollection().iterator(); i.hasNext();) {
				PctFamilia pctFamilia1 = i.next();

				if (isRepresentante) {
					if (pctPessoaFisicaSelecionado.getCpfRepresentante()
							.equals(pctFamilia1.getIdePessoa().getPessoaFisica().getNumCpf())) {
						return true;
					}

				} else {

					if (pctPessoaFisicaSelecionado.getNumCpf()
							.equals(pctFamilia1.getIdePessoa().getPessoaFisica().getNumCpf())) {
						return true;
					} else {
						for (Iterator<PctFamilia> j = pctFamilia1.getMembrosFamiliaCollection().iterator(); j
								.hasNext();) {
							PctFamilia pctFamilia2 = j.next();
							if (pctPessoaFisicaSelecionado.getNumCpf()
									.equals(pctFamilia2.getIdePessoa().getPessoaFisica().getNumCpf())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean existePessoaAdicionada(PessoaFisica pctPessoaFisicaSelecionado, PctDTO pctDTO,
			boolean isRepresentante) {

		if (!Util.isNullOuVazio(pctDTO.getPessoaFisicaRepresentantes())) {
			for (PessoaFisica pessoaFisicaRepresentante : pctDTO.getPessoaFisicaRepresentantes()) {

				if (isRepresentante) {
					if (pessoaFisicaRepresentante.getNumCpf()
							.equals(pctPessoaFisicaSelecionado.getCpfRepresentante())) {
						return true;
					}
				} else if (pessoaFisicaRepresentante.getNumCpf().equals(pctPessoaFisicaSelecionado.getNumCpf())) {
					return true;
				}
			}
		}

		if (!isRepresentante && !Util.isNullOuVazio(pctDTO.getPessoaFisicas())) {
			for (PessoaFisica pessoaFisica : pctDTO.getPessoaFisicas()) {
				if (pessoaFisica.getNumCpf().equals(pctPessoaFisicaSelecionado.getNumCpf())) {
					return true;
				}
			}
		}

		return false;
	}

	public void validarPctPessoaFisica(PctImovelRural pctImovelRural, PessoaFisica pctPessoaFisicaSelecionado,
			String dataNascimento, Boolean indRepresentante) throws Exception {
		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNumCpf())) {
			throw new SeiaValidacaoRuntimeException(Util.getString("MSG-003", Util.getString("geral_lbl_cpf")));
		}

		if (!CpfValidator.validaCPF(pctPessoaFisicaSelecionado.getNumCpf())) {
			throw new SeiaValidacaoRuntimeException(
					Util.getString("cefir_msg_cpf_invalido", pctPessoaFisicaSelecionado.getNumCpfFormatado()));
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomPessoa())) {
			throw new SeiaValidacaoRuntimeException(Util.getString("MSG-003", Util.getString("lbl_nome")));
		}

		if (!Util.isNullOuVazio(dataNascimento)) {
			if (!DataUtil.isDataValida(dataNascimento)) {
				throw new SeiaValidacaoRuntimeException(
						Util.getString("cefir_msg_data_nascimento_invalida", dataNascimento));
			} else {
				pctPessoaFisicaSelecionado.setDtcNascimento(Util.formataData(dataNascimento));
			}
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getDtcNascimento())) {
			throw new SeiaValidacaoRuntimeException(Util.getString("MSG-003", Util.getString("lbl_data_nascimento")));
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomMae())) {
			throw new SeiaValidacaoRuntimeException(Util.getString("MSG-003", Util.getString("lbl_nome_mae")));
		}

		if ((Util.isNullOuVazio(indRepresentante) || !indRepresentante)
				&& Util.isNull(pctImovelRural.getPessoaRepresentanteFamiliaSelecionada())) {
			throw new SeiaValidacaoRuntimeException(
					"O campo Quem é o representante da família é de preenchimento obrigatório.");
		}
	}

	public boolean validarPctPessoaFisicaPlanilha(PctImovelRural pctImovelRural,
			PessoaFisica pctPessoaFisicaSelecionado, String dataNascimento, Boolean indRepresentante,
			List<String> lista) throws Exception {
		boolean retorno = true;

		StringBuffer camposValidados = new StringBuffer();

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNumCpf())) {
			retorno = false;
			camposValidados.append("CPF é obrigatório;");
		}

		if (!CpfValidator.validaCPF(pctPessoaFisicaSelecionado.getNumCpf())) {
			retorno = false;
			camposValidados.append("CPF não é válido;");
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomPessoa())) {
			retorno = false;
			camposValidados.append("Nome é obrigatório;");
		}

		if (!Util.isNullOuVazio(dataNascimento)) {
			if (!DataUtil.isDataValida(dataNascimento)) {
				retorno = false;
			} else {
				pctPessoaFisicaSelecionado.setDtcNascimento(Util.formataData(dataNascimento));
			}
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getDtcNascimento())) {
			retorno = false;
			camposValidados.append("Data de nascimento é obrigatório e deve obedecer o padrão dd/mm/yyyy;");
		}

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomMae())) {
			retorno = false;
			camposValidados.append("Nome da mãe é obrigatório;");
		}

		if ((Util.isNullOuVazio(indRepresentante) || !indRepresentante)
				&& Util.isNull(pctImovelRural.getPessoaRepresentanteFamiliaSelecionada())) {
			retorno = false;
			camposValidados.append("Quem é o representante da família é obrigatório;");
		}

		if (!retorno) {
			camposValidados.insert(0, pctPessoaFisicaSelecionado.getNumCpfFormatado().concat(";"));
			lista.add(camposValidados.toString());
		}

		return retorno;
	}

	public StreamedContent getDownloadPlanilhaPCT() throws Exception {
		File file = new File(CAMINHO_PLANILHA_PCT);
		InputStream stream = new FileInputStream(file);

		StreamedContent sc = new DefaultStreamedContent(stream,
				MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(CAMINHO_PLANILHA_PCT),
				FileUploadUtil.getFileName(CAMINHO_PLANILHA_PCT));
		return sc;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PctFamilia> listarPctRepresentanteFamilia(PctImovelRural pctImovelRural) throws Exception {
		return pctFamiliaDAOImpl.listarPctRepresentanteFamilia(pctImovelRural);
	}
	
	public boolean existePessoaPctFamilia(String cpf, Integer idePctImovelRural) throws Exception {
		if(!Util.isNullOuVazio(idePctImovelRural)) {
			PctFamilia pctFamilia = pctFamiliaDAOImpl.obterPessoaPctFamilia(cpf);
			if(!Util.isNullOuVazio(pctFamilia) && !pctFamilia.getIdePctImovelRural().getIdePctImovelRural().equals(idePctImovelRural)) {
				return true;
			} else {
				return false;
			}
		} else {
			return pctFamiliaDAOImpl.existePessoaPctFamilia(cpf);
		}
	}

	public boolean existePessoaPctFamiliaPctImovelRural(String cpf, PctImovelRural pctImovelRural, PctFamilia existePctFamilia) throws Exception {
		if(!Util.isNullOuVazio(pctImovelRural)) {
			PctFamilia pctFamilia = pctFamiliaDAOImpl.obterPessoaPctFamiliaPctImovelRural(cpf, pctImovelRural);
			if(!Util.isNullOuVazio(pctFamilia)) { 
				if(pctFamilia.getIdePctImovelRural().getIdePctImovelRural().equals(pctImovelRural.getIdePctImovelRural())) {
					existePctFamilia = pctFamilia;
					return true;
				}else {
					return false;
				}					
			} else {
				return false;
			}
		} else {
			return pctFamiliaDAOImpl.existePessoaPctFamilia(cpf);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPCT(PctImovelRural pctImovelRural) throws Exception {

		if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaExclusaoCollection())) {
			for (PctFamilia pctFamilia : pctImovelRural.getPctFamiliaExclusaoCollection()) {
				if (!Util.isNullOuVazio(pctFamilia.getIdePctFamilia())) {
					if (!Util.isNullOuVazio(pctFamilia.getMembrosFamiliaCollection())) {
						for (PctFamilia membros : pctFamilia.getMembrosFamiliaCollection()) {
							excluirPctFamilia(membros);
						}
					}
					excluirPctFamilia(pctFamilia);
				}
			}
		}

		if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
			for (PctFamilia pctFamilia : pctImovelRural.getPctFamiliaCollection()) {
				salvarPessoaFisicaPct(pctFamilia.getIdePessoa().getPessoaFisica());
				pctFamilia.setIdePessoa(pctFamilia.getIdePessoa().getPessoaFisica().getPessoa());
				if(existePessoaPctFamiliaPctImovelRural(pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf(), pctImovelRural, pctFamilia)) {
					pctFamiliaDAOImpl.atualizar(pctFamilia);
				}else {
					pctFamiliaDAOImpl.salvarOuAtualizar(pctFamilia);
				}				

				if (!Util.isNullOuVazio(pctFamilia.getMembrosFamiliaCollection())) {
					for (PctFamilia pctFamiliaMembro : pctFamilia.getMembrosFamiliaCollection()) {
						if (Util.isNullOuVazio(pctFamiliaMembro.getIdePctFamilia())) {
							pctFamiliaMembro.setIdePessoaAssociada(pctFamilia.getIdePessoa());

							if (Util.isNullOuVazio(pctFamiliaMembro.getIdePessoa().getPessoaFisica())) {
								salvarPessoaFisicaPct(pctFamiliaMembro.getIdePessoa().getPessoaFisica());
								pctFamiliaMembro
										.setIdePessoa(pctFamiliaMembro.getIdePessoa().getPessoaFisica().getPessoa());
							}

							if(existePessoaPctFamiliaPctImovelRural(pctFamiliaMembro.getIdePessoa().getPessoaFisica().getNumCpf(), pctImovelRural, pctFamilia)) {
								pctFamiliaDAOImpl.atualizar(pctFamiliaMembro);
							}else {
								pctFamiliaDAOImpl.salvarOuAtualizar(pctFamiliaMembro);
							}
						}
					}
				}
			}
		}

		PctImovelRural pctImovelRuralOld = (PctImovelRural) pctImovelRural.clone();
		List<PessoaJuridicaPct> pctImovelRuralListaOld = Util.deepCloneList(pctImovelRural.getPessoaJuridicaPctList());

		if (!Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctListaExclusao())) {
			for (Iterator<PessoaJuridicaPct> iterator = pctImovelRural.getPessoaJuridicaPctListaExclusao()
					.iterator(); iterator.hasNext();) {
				PessoaJuridicaPct pessoaJuridicaPct = iterator.next();
				if (!Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridicaPct())) {
					pessoaJuridicaPct.setIndExcluido(true);
					pessoaJuridicaPct.setDtcExclusao(new Date());
					pessoaJuridicaPctService.updatePessoaJuridicarPct(pessoaJuridicaPct);
				}
			}
		}

		if (!Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())) {
			for (PessoaJuridicaPct pessoaJuridicaPct : pctImovelRural.getPessoaJuridicaPctList()) {
				if (Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridicaPct())) {
					imovelRuralFacade.salvarPessoaJuridicarPct(pessoaJuridicaPct);
				}
			}

			List<PessoaJuridicaPct> pctImovelRuralLista = imovelRuralFacade.listarPessoaJuridicaByPct(pctImovelRural);

			pctImovelRural.setPessoaJuridicaPctList(pctImovelRuralLista);
			pctImovelRuralOld.setPessoaJuridicaPctList(pctImovelRuralListaOld);
		}

		auditoria.atualizarPCT(pctImovelRuralOld, pctImovelRural);
	}
}

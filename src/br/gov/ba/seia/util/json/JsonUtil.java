package br.gov.ba.seia.util.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.PctFamilia;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.enumerator.TipoTerritorioPctEnum;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.json.enums.TipoVinculoEnum;

public class JsonUtil {

	private static DateFormat dateHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final String NAO_INFORMADO = "Não Informado";
	
	public static JSONObject montarJsonImovelRuralSicar(ImovelRural imovelRural, ImovelRuralRppn imovelRuralRppn) throws Exception {
		JSONObject imovelRuralSicar = new JSONObject();
		JSONArray proprietariosPosseirosConcessionarios = montarObjetoProprietariosPosseiros(imovelRural);

		imovelRuralSicar.put("versao", "3.0");
		imovelRuralSicar.put("origem", montarObjetoOrigem(imovelRural));
		imovelRuralSicar.put("cadastrante", montarObjetoCadastrante(imovelRural));
		imovelRuralSicar.put("imovel", montarObjetoImovel(imovelRural));
		imovelRuralSicar.put("proprietariosPosseirosConcessionarios", proprietariosPosseirosConcessionarios);
		imovelRuralSicar.put("documentos", montarObjetoDocumentos(imovelRural, proprietariosPosseirosConcessionarios));
		imovelRuralSicar.put("informacoes", montarObjetoInformacoes(imovelRural, imovelRuralRppn));
		imovelRuralSicar.put("geo", montarObjetoGeo(imovelRural));

		return imovelRuralSicar;
	}

	private static JSONObject montarObjetoOrigem(ImovelRural imovelRural) throws JSONException {
		JSONObject origem = new JSONObject();

		origem.put("tipo", "EST");
		origem.put("codigoProtocolo", imovelRural.getImovelRuralSicar().getNumProtocolo());
		origem.put("dataProtocolo", dateHourFormat.format(new Date()));
		origem.put("status", "IN");

		return origem;
	}

	private static JSONObject montarObjetoCadastrante(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject cadastrante = new JSONObject();
			PessoaFisica pessoaFisica = null;
	
			if (!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro().getPessoaFisica())) {
				pessoaFisica = imovelRural.getIdeRequerenteCadastro().getPessoaFisica();
			} else if (!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro().getPessoaJuridica())) {
				for (PessoaImovel pessoaImovel : imovelRural.getImovel().getPessoaImovelCollection()) {
					if (pessoaImovel.getIdeTipoVinculoImovel().isProcurador() && !pessoaImovel.getIndExcluido()) {
						pessoaFisica = pessoaImovel.getIdePessoa().getPessoaFisica();
						break;
					}
				}
	
				if (Util.isNullOuVazio(pessoaFisica)) {
					for (RepresentanteLegal representante : imovelRural.getIdeRequerenteCadastro().getPessoaJuridica().getRepresentanteLegalCollection()) {
						if (!representante.getIndExcluido()) {
							pessoaFisica = representante.getIdePessoaFisica();
							break;
						}
					}
				}
			}

			if (!Util.isNullOuVazio(pessoaFisica)) {
				cadastrante.put("cpf", pessoaFisica.getNumCpf());
				cadastrante.put("dataNascimento", dateFormat.format(pessoaFisica.getDtcNascimento()));
				
				String nome = pessoaFisica.getNomPessoa();
				String nomeMae = pessoaFisica.getNomMae();
				
				cadastrante.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
	
				if (!Util.isNullOuVazio(nomeMae)) {
					cadastrante.put("nomeMae", (nomeMae.length() > 100 ? nomeMae.substring(0, 99) : nomeMae));
				} else {
					cadastrante.put("nomeMae", NAO_INFORMADO);
				}
				
				return cadastrante;
			} else {
				throw new JSONException("Pessoa Fisica não especificada.");
			}
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto Cadastrante: " + e.getMessage());
		}
	}

	@SuppressWarnings("null")
	private static JSONObject montarObjetoImovel(ImovelRural imovelRural) throws JSONException {
		
		try {
			JSONObject imovel = new JSONObject();
			PessoaFisica pessoaFisica = null;
	
			if (!Util.isNullOuVazio(imovelRural.getImovelRuralSicar().getNumSicar())) {
				imovel.put("idPai", imovelRural.getImovelRuralSicar().getNumSicar());
			}
			
			if (imovelRural.isImovelINCRA()) {
				imovel.put("tipo", "AST");
				imovel.put("fracaoIdeal", imovelRural.getValFracaoIdeal());
				imovel.put("codigoProjetoAssentamento", imovelRural.getCodSipra());
				imovel.put("dataCriacaoAssentamento", dateFormat.format(imovelRural.getDtcCriacaoAssentamento()));
			} else if(imovelRural.isImovelPCT()) {
				JSONArray seguimentos = new JSONArray();
				
				for (TipoSeguimentoPct tpSeguimento: imovelRural.getIdePctImovelRural().getTipoSeguimentoPctCollection()) {
					seguimentos.put(tpSeguimento.getSglTipoSeguimento());
				}
				
				imovel.put("tipo", "PCT");
				imovel.put("segmentosPct", seguimentos);
				
				if (!Util.isNullOuVazio(seguimentos) && seguimentos.toString().contains("OUTROS")){ 
					imovel.put("descricaoSegmentoOutros", imovelRural.getIdePctImovelRural().getDscTipoSeguimentoPctOutros());
				}
			} else {
				imovel.put("tipo", "IRU");
			}
	
			imovel.put("nome", imovelRural.getDesDenominacao());
			imovel.put("codigoMunicipio", (imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio().intValue()));
			imovel.put("cep", imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getNumCepString());
	
			if (!Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco().getDesPontoReferencia())) {
				imovel.put("descricaoAcesso", imovelRural.getImovel().getIdeEndereco().getDesPontoReferencia());
			} else {
				imovel.put("descricaoAcesso", NAO_INFORMADO);
			}				
			imovel.put("email", imovelRural.getIdeRequerenteCadastro().getDesEmail());		
			imovel.put("zonaLocalizacao", "RURAL");
			imovel.put("modulosFiscais", imovelRural.getQtdModuloFiscal());
			
			
			if (!Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco())) {
				JSONObject enderecoCorrespondencia = new JSONObject();
				
				String logradouro = imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getNomLogradouro();
				String complemento = imovelRural.getImovel().getIdeEndereco().getDesComplemento();
				
				if (!Util.isNullOuVazio(logradouro)) {
					enderecoCorrespondencia.put("logradouro", (logradouro.length() > 100) ? logradouro.substring(0, 99) : logradouro);
				} else {
					throw new JSONException("Logradouro não foi especificado.");
				}
				
				enderecoCorrespondencia.put("numero", imovelRural.getImovel().getIdeEndereco().getNumEndereco());
				enderecoCorrespondencia.put("complemento", (complemento.length() > 100 ? complemento.substring(0, 99) : complemento));
				enderecoCorrespondencia.put("bairro", imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro().getNomBairro());
				enderecoCorrespondencia.put("cep", imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getNumCepString());
				
				Double municipioImovel = imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(); 
				if (!Util.isNullOuVazio(municipioImovel)) {
					enderecoCorrespondencia.put("codigoMunicipio", municipioImovel.intValue());
				} else {
					throw new JSONException("Código IBGE do Município não foi especificado.");
				}
				
				imovel.put("enderecoCorrespondencia", enderecoCorrespondencia);
			} else {
				throw new JSONException("Endereço não foi especificado.");
			}
	
			return imovel;
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto Imovel");
		}
	}

	private static JSONArray montarObjetoProprietariosPosseiros(ImovelRural imovelRural) throws JSONException {
		try {
			JSONArray proprietariosPosseirosConcessionarios = new JSONArray();
	
			for (PessoaImovel pessoaImovel : imovelRural.getImovel().getPessoaImovelCollection()) {
				if (!pessoaImovel.getIndExcluido()) {
					JSONObject proprietarioPosseiroConcessionario = new JSONObject();
		
					if (!Util.isNullOuVazio(pessoaImovel.getIdePessoa().getPessoaFisica())) {
						proprietarioPosseiroConcessionario.put("tipo", "PF");
						proprietarioPosseiroConcessionario.put("cpfCnpj", pessoaImovel.getIdePessoa().getPessoaFisica().getNumCpf());
						
						String nome = pessoaImovel.getIdePessoa().getPessoaFisica().getNomPessoa();
						String nomeMae = pessoaImovel.getIdePessoa().getPessoaFisica().getNomMae();
						
						proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
						proprietarioPosseiroConcessionario.put("dataNascimento", dateFormat.format(pessoaImovel.getIdePessoa().getPessoaFisica().getDtcNascimento()));
		
						if (!Util.isNullOuVazio(nomeMae)) {
							proprietarioPosseiroConcessionario.put("nomeMae", (nomeMae.length() > 100 ? nomeMae.substring(0, 99) : nomeMae));
						} else {
							proprietarioPosseiroConcessionario.put("nomeMae", NAO_INFORMADO);
						}
					} else {
						proprietarioPosseiroConcessionario.put("tipo", "PJ");
						proprietarioPosseiroConcessionario.put("cpfCnpj", pessoaImovel.getIdePessoa().getPessoaJuridica().getNumCnpj());
						
						String nome = pessoaImovel.getIdePessoa().getPessoaJuridica().getNomRazaoSocial();
						String nomeFantasia = pessoaImovel.getIdePessoa().getPessoaJuridica().getNomeFantasia();
						
						proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
						proprietarioPosseiroConcessionario.put("nomeFantasia", (nomeFantasia.length() > 100 ? nomeFantasia.substring(0, 99) : nomeFantasia));
						
						if(!Util.isNullOuVazio(pessoaImovel.getIdePessoa().getRepresentanteLegalCollection())) {
							proprietarioPosseiroConcessionario.put("representantesLegais", montarRepresentantesLegais(pessoaImovel.getIdePessoa().getRepresentanteLegalCollection()));
						}
					}	
		
					proprietariosPosseirosConcessionarios.put(proprietarioPosseiroConcessionario);
				}
			}
	
			if (imovelRural.isImovelINCRA()) {
				for (AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()) {
					JSONObject proprietarioPosseiroConcessionario = new JSONObject();
	
					proprietarioPosseiroConcessionario.put("tipo", "PF"); 
					proprietarioPosseiroConcessionario.put("cpfCnpj", assentado.getIdeAssentadoIncra().getIdePessoaFisica().getNumCpf());
					
					String nome = assentado.getIdeAssentadoIncra().getIdePessoaFisica().getNomPessoa();
					String nomeMae = assentado.getIdeAssentadoIncra().getIdePessoaFisica().getNomMae();
					
					proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
					proprietarioPosseiroConcessionario.put("dataNascimento", dateFormat.format(assentado.getIdeAssentadoIncra().getIdePessoaFisica().getDtcNascimento()));
	
					if (!Util.isNullOuVazio(nomeMae)) {
						proprietarioPosseiroConcessionario.put("nomeMae", (nomeMae.length() > 100 ? nomeMae.substring(0, 99) : nomeMae));
					} else {
						proprietarioPosseiroConcessionario.put("nomeMae", NAO_INFORMADO);
					}
	
					proprietariosPosseirosConcessionarios.put(proprietarioPosseiroConcessionario);
				}
			} else if (imovelRural.isImovelPCT()) {
				for (PctFamilia membro : imovelRural.getIdePctImovelRural().getPctFamiliaCollection()) {
					if (!membro.isIndExcluido()) {
						JSONObject proprietarioPosseiroConcessionario = new JSONObject();
		
						if (!Util.isNullOuVazio(membro.getIdePessoa().getPessoaFisica())) {
							proprietarioPosseiroConcessionario.put("tipo", "PF");
							proprietarioPosseiroConcessionario.put("cpfCnpj", membro.getIdePessoa().getPessoaFisica().getNumCpf());
							
							String nome = membro.getIdePessoa().getPessoaFisica().getNomPessoa();
							String nomeMae = membro.getIdePessoa().getPessoaFisica().getNomMae();
							
							proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
							proprietarioPosseiroConcessionario.put("dataNascimento", dateFormat.format(membro.getIdePessoa().getPessoaFisica().getDtcNascimento()));
							
							if (!Util.isNullOuVazio(nomeMae)) {
								proprietarioPosseiroConcessionario.put("nomeMae", (nomeMae.length() > 100 ? nomeMae.substring(0, 99) : nomeMae));
							} else {
								proprietarioPosseiroConcessionario.put("nomeMae", NAO_INFORMADO);
							}
						} else {
							proprietarioPosseiroConcessionario.put("tipo", "PJ");
							proprietarioPosseiroConcessionario.put("cpfCnpj", membro.getIdePessoa().getPessoaJuridica().getNumCnpj());
							
							String nome = membro.getIdePessoa().getPessoaJuridica().getNomRazaoSocial();
							String nomeFantasia = membro.getIdePessoa().getPessoaJuridica().getNomeFantasia();
							
							proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
							proprietarioPosseiroConcessionario.put("nomeFantasia", (nomeFantasia.length() > 100 ? nomeFantasia.substring(0, 99) : nomeFantasia));
							
							if(!Util.isNullOuVazio(membro.getIdePessoa().getRepresentanteLegalCollection())) {
								proprietarioPosseiroConcessionario.put("representantesLegais", montarRepresentantesLegais(membro.getIdePessoa().getRepresentanteLegalCollection()));
							}
						}
		
						proprietariosPosseirosConcessionarios.put(proprietarioPosseiroConcessionario);
						
						//remover
						if(!membro.getMembrosFamiliaCollection().isEmpty()) {
							montarMembrosFamiliaImovelPCT(membro.getMembrosFamiliaCollection(), proprietariosPosseirosConcessionarios);
						}
					}
				}
				
				if(imovelRural.getIdePctImovelRural().isRenderedPessoaJuridicaPctListaAssociacao()) {
					montarAssociacoesImovelPCT(imovelRural.getIdePctImovelRural(), proprietariosPosseirosConcessionarios);
				}
			}
	
			return proprietariosPosseirosConcessionarios;
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto ProprietariosPosseirosConcessionarios.");
		}
	}
	
	private static JSONArray montarRepresentantesLegais(Collection<RepresentanteLegal> lRepresentanteLegal) throws JSONException {
		JSONArray representantesLegais = new JSONArray();
		for (RepresentanteLegal pRepresentanteLegal : lRepresentanteLegal) {
			JSONObject representanteLegal = new JSONObject();
			
			representanteLegal.put("cpfCnpj", pRepresentanteLegal.getIdePessoaFisica().getNumCpfFormatado());
			
			String nome = pRepresentanteLegal.getIdePessoaFisica().getNomPessoa();
			String nomeMae = pRepresentanteLegal.getIdePessoaFisica().getNomMae();
			
			representanteLegal.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
			representanteLegal.put("dataNascimento", dateFormat.format(pRepresentanteLegal.getIdePessoaFisica().getDtcNascimento()));
			
			if (!Util.isNullOuVazio(nomeMae)) {
				representanteLegal.put("nomeMae", (nomeMae.length() > 100 ? nomeMae.substring(0, 99) : nomeMae));
			} else {
				representanteLegal.put("nomeMae", NAO_INFORMADO);
			}
			
			representanteLegal.put("email", pRepresentanteLegal.getIdePessoaFisica().getPessoa().getDesEmail());
			
			for (Telefone telefone : pRepresentanteLegal.getIdePessoaFisica().getPessoa().getTelefoneCollection()) {
				if(telefone.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.RESIDENCIAL.getId()) ||
						telefone.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.CELULAR.getId()) ||
						telefone.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.COMERCIAL.getId()) ||
						telefone.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.FAX.getId())) {
					representanteLegal.put("telefone", telefone.getNumTelefone());
					break;
				}
			}
			
			Endereco endereco = pRepresentanteLegal.getIdePessoaFisica().getPessoa().getEndereco();
			
			if(!Util.isNullOuVazio(endereco)) {
				String logradouro = endereco.getIdeLogradouro().getNomLogradouro();
				
				representanteLegal.put("logradouro", (logradouro.length() > 100 ? logradouro.substring(0, 99) : logradouro));
				representanteLegal.put("numero", endereco.getNumEndereco());
				representanteLegal.put("bairro", endereco.getIdeLogradouro().getIdeBairro().getNomBairro());
				representanteLegal.put("cep", endereco.getIdeLogradouro().getNumCepString());
				representanteLegal.put("uf", endereco.getIdeLogradouro().getMunicipio().getIdeEstado().getCodIbgeEstado());
				representanteLegal.put("municipio", endereco.getIdeLogradouro().getMunicipio().getCoordGeobahiaMunicipio());
			}
	
			representantesLegais.put(representanteLegal);
		}
		
		return representantesLegais;
	}
	
	private static void montarMembrosFamiliaImovelPCT(Collection<PctFamilia> pMembrosFamilia, JSONArray proprietariosPosseirosConcessionarios) throws JSONException {
		for (PctFamilia pctFamilia : pMembrosFamilia) {
			JSONObject membroFamilia = new JSONObject();
			membroFamilia.put("tipo", "PF");
			membroFamilia.put("cpfCnpj", pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf());
			membroFamilia.put("nome", pctFamilia.getIdePessoa().getPessoaFisica().getNomPessoa());
			membroFamilia.put("dataNascimento", dateFormat.format(pctFamilia.getIdePessoa().getPessoaFisica().getDtcNascimento()));
			if (!Util.isNullOuVazio(pctFamilia.getIdePessoa().getPessoaFisica().getNomMae())) {
				membroFamilia.put("nomeMae", pctFamilia.getIdePessoa().getPessoaFisica().getNomMae());
			} else {
				membroFamilia.put("nomeMae", NAO_INFORMADO);
			}
			proprietariosPosseirosConcessionarios.put(membroFamilia);
		}
	}
	
	private static void montarAssociacoesImovelPCT(PctImovelRural idePctImovelRural, JSONArray proprietariosPosseirosConcessionarios) throws JSONException {
		JSONObject proprietarioPosseiroConcessionario = new JSONObject();
		
		for (PessoaJuridicaPct pessoaJuridicaPct : idePctImovelRural.getPessoaJuridicaPctListaAssociacao()) {
			if (!pessoaJuridicaPct.isIndExcluido()) {
				proprietarioPosseiroConcessionario.put("tipo", "PJ");
				proprietarioPosseiroConcessionario.put("cpfCnpj", pessoaJuridicaPct.getIdePessoaJuridica().getNumCnpj());
				
				String nome = pessoaJuridicaPct.getIdePessoaJuridica().getNomRazaoSocial();
				String nomeFantasia = pessoaJuridicaPct.getIdePessoaJuridica().getNomeFantasia();
				
				proprietarioPosseiroConcessionario.put("nome", (nome.length() > 100 ? nome.substring(0, 99) : nome));
				proprietarioPosseiroConcessionario.put("nomeFantasia", (nomeFantasia.length() > 100 ? nomeFantasia.substring(0, 99) : nomeFantasia));
				
				if(!Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection())) {
					proprietarioPosseiroConcessionario.put("representantesLegais", montarRepresentantesLegais(pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection()));
				}
				
				proprietariosPosseirosConcessionarios.put(proprietarioPosseiroConcessionario);
			}
		}
	}

	private static JSONArray montarObjetoDocumentos(ImovelRural imovelRural, JSONArray proprietariosPosseirosConcessionarios) throws JSONException {
		try {
			JSONArray documentos = new JSONArray();
			JSONObject documento = new JSONObject();
			JSONArray listaProprietariosPosseirosConcessionarios = new JSONArray();
	
			documento.put("denominacao", imovelRural.getDesDenominacao());
			documento.put("area", imovelRural.getValArea());
			
			for (int i=0; i < proprietariosPosseirosConcessionarios.length(); i++) {
				JSONObject proprietarioPosseiroConcessionario = proprietariosPosseirosConcessionarios.getJSONObject(i);
				listaProprietariosPosseirosConcessionarios.put(proprietarioPosseiroConcessionario.get("cpfCnpj").toString());
			}
			documento.put("proprietariosPosseirosConcessionarios", listaProprietariosPosseirosConcessionarios);
			
			String siglaTipoDocumentoSicar = null;
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse()) 
					&& !Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())) {
				siglaTipoDocumentoSicar = converterSiglaCefirParaSicar(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getSglTipoDocumentoImovelRural());
			} else {
				throw new JSONException("Documento ou Tipo de Documento do Imóvel Rural não foi especificado.");
			}
			
			if (imovelRural.isImovelPCT()) {				
				if (imovelRural.getIdePctImovelRural().getIdeTipoTerritorioPct().getIdeTipoTerritorioPct().equals(TipoTerritorioPctEnum.PROPRIEDADE.getId())) {
					documento.put("tipo", TipoVinculoEnum.PROPRIEDADE);
					documento.put("tipoDocumentoPropriedade", siglaTipoDocumentoSicar);
					documento.put("detalheDocumentoPropriedade", montarObjetoDetalheDocumentoPropriedade(imovelRural));
				} else if (imovelRural.getIdePctImovelRural().getIdeTipoTerritorioPct().getIdeTipoTerritorioPct().equals(TipoTerritorioPctEnum.POSSE.getId())) {
					documento.put("tipo", TipoVinculoEnum.POSSE);
					documento.put("tipoDocumentoPosse", siglaTipoDocumentoSicar);
					documento.put("detalheDocumentoPosse", montarObjetoDetalheDocumentoPosse(imovelRural, siglaTipoDocumentoSicar));
				} else if (imovelRural.getIdePctImovelRural().getIdeTipoTerritorioPct().getIdeTipoTerritorioPct().equals(TipoTerritorioPctEnum.CONCESSAO.getId())) {
					documento.put("tipo", TipoVinculoEnum.CONCESSAO);
					documento.put("tipoDocumentoConcessao", new JSONArray("["+siglaTipoDocumentoSicar+"]"));
				}
			} else {
				if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getIdeTipoVinculoImovel().isProprietario()) {
					documento.put("tipo", TipoVinculoEnum.PROPRIEDADE);
					documento.put("tipoDocumentoPropriedade", siglaTipoDocumentoSicar);
					documento.put("detalheDocumentoPropriedade", montarObjetoDetalheDocumentoPropriedade(imovelRural));
	
				} else if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getIdeTipoVinculoImovel().isJustoPossuidor()) {
					documento.put("tipo", TipoVinculoEnum.POSSE);
					documento.put("tipoDocumentoPosse", siglaTipoDocumentoSicar);
					documento.put("detalheDocumentoPosse", montarObjetoDetalheDocumentoPosse(imovelRural, siglaTipoDocumentoSicar));
				} else {
					documento.put("tipo", TipoVinculoEnum.OUTROS.getTipo());
					documento.put("tipoDocumentoOutros", siglaTipoDocumentoSicar);
				}
			}
			
			documento.put("reservaLegal", montarObjetoReservaLegal(imovelRural));
			documentos.put(documento);
	
			return documentos;
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto Documentos: " + e.getMessage());
		}
	}
	
	private static String converterSiglaCefirParaSicar(String sigla) {
		if(sigla.equals("CAAN")) {
			return "CANU";
		}else if(sigla.equals("CCDT")) {
			return "CCDP";
		}else if(sigla.equals("TDRL")) {
			return "TDRF";
		}else if(sigla.equals("TDSR")) {
			return "TDRR";
		}else if(sigla.equals("TDTA")) {
			return "TDOF";
		}else if(sigla.equals("DSRT")) {
			return "DSRU";
		}else if(sigla.equals("DESM")) {
			return "DAMU";
		}else if(sigla.equals("TERA")) {
			return "ADEC";
		}else if(sigla.equals("CERG")) {
			return "CREG";	
		}else{
			return sigla;
		}
	}
	
	private static JSONObject montarObjetoDetalheDocumentoPropriedade(ImovelRural imovelRural) throws JSONException {
		JSONObject detalheDocumentoPropriedade = new JSONObject();

		if (!Util.isNullOuVazio(imovelRural.getNumMatricula())) {
			detalheDocumentoPropriedade.put("numeroMatricula", imovelRural.getNumMatricula());
		} else {
			throw new JSONException("Matrícula não foi especificada.");
		}
		
		if (!Util.isNullOuVazio(dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()))) {
			detalheDocumentoPropriedade.put("dataRegistro", dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()));
		} else {
			throw new JSONException("Data de Registro não foi especificada.");
		}
		
		if (!Util.isNullOuVazio(imovelRural.getDesLivro())) {
			detalheDocumentoPropriedade.put("livro", imovelRural.getDesLivro());
		} else {
			throw new JSONException("Livro não foi especificado.");
		}
		
		if (!Util.isNullOuVazio(imovelRural.getNumFolha())) {
			detalheDocumentoPropriedade.put("folha", imovelRural.getNumFolha());
		} else {
			throw new JSONException("Folha não foi especificada.");
		}
		
		if (!Util.isNullOuVazio(imovelRural.getIdeMunicipioCartorio().getCoordGeobahiaMunicipio())) {
			detalheDocumentoPropriedade.put("municipioCartorio", imovelRural.getIdeMunicipioCartorio().getCoordGeobahiaMunicipio().intValue());
		} else {
			throw new JSONException("Código IBGE do Município do Cartório não foi especificado.");
		}

		return detalheDocumentoPropriedade;
	}

	private static JSONObject montarObjetoDetalheDocumentoPosse(ImovelRural imovelRural, String siglaTipoDocumentoSicar) throws JSONException {
		JSONObject detalheDocumentoPosse = new JSONObject();
		
		if (siglaTipoDocumentoSicar.matches("AOCP|CANU|CRDU|CATP|CCDP|CCTP|CTAF|LOCP|TDOA|TPCR|TDRF|TDRR|TDOF|TDOM|TRDO|TRAT")) {
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getDscEmissorDocumento())) {
				detalheDocumentoPosse.put("emissorDocumento", imovelRural.getDocumentoImovelRuralPosse().getDscEmissorDocumento());
			} else {
				throw new JSONException("Emissor do Documento não foi especificado.");
			}
			
			if (!Util.isNullOuVazio(dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()))) {
				detalheDocumentoPosse.put("dataDocumento", dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()));
			} else {
				throw new JSONException("Data do Documento não foi especificada.");
			}
		}else if (siglaTipoDocumentoSicar.matches("DSRU|DAMU|DCON")) {
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getNomDeclarante())) {
				detalheDocumentoPosse.put("nomeDeclarante", imovelRural.getDocumentoImovelRuralPosse().getNomDeclarante());
			} else {
				throw new JSONException("Nome do Declarante não foi especificado.");
			}
			
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getNumCpfCnpjDeclarante())) {
				detalheDocumentoPosse.put("cpfCnpjDeclarante", imovelRural.getDocumentoImovelRuralPosse().getNumCpfCnpjDeclarante());
			} else {
				throw new JSONException("CPF/CNPJ do Declarante não foi especificado.");
			}
	
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante())) {
				JSONObject enderecoDeclarante = new JSONObject();
				
				String logradouro = imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getNomLogradouro();
				String complemento = imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getDesComplemento();
				
				if (!Util.isNullOuVazio(logradouro)) {
					enderecoDeclarante.put("logradouro", (logradouro.length() > 100) ? logradouro.substring(0, 99) : logradouro);
				} else {
					throw new JSONException("Logradouro do Declarante não foi especificado.");
				}
				
				enderecoDeclarante.put("numero", imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getNumEndereco());
				enderecoDeclarante.put("complemento", (complemento.length() > 100) ? complemento.substring(0, 99) : complemento);
				enderecoDeclarante.put("bairro", imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro().getNomBairro());
				enderecoDeclarante.put("cep", imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getNumCepString());
				
				Double municipioDeclarante = imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(); 
				if (!Util.isNullOuVazio(municipioDeclarante)) {
					enderecoDeclarante.put("codigoMunicipio", municipioDeclarante.intValue());
				} else {
					throw new JSONException("Código IBGE do Município do Declarante não foi especificado.");
				}
				
				detalheDocumentoPosse.put("enderecoDeclarante", enderecoDeclarante);
			} else {
				throw new JSONException("Endereço do Declarante não foi especificado.");
			}
		} else if (siglaTipoDocumentoSicar.equals("CPCV")) {
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getNomVendedor())) {
				detalheDocumentoPosse.put("nomeVendedor", imovelRural.getDocumentoImovelRuralPosse().getNomVendedor());
			} else {
				throw new JSONException("Nome do Vendedor não foi especificado.");
			}
			
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getNumCpfVendedor())) {
				detalheDocumentoPosse.put("cpfVendedor", imovelRural.getDocumentoImovelRuralPosse().getNumCpfVendedor());
			} else {
				throw new JSONException("CPF do Vendedor não foi especificado.");
			}
			
			if (!Util.isNullOuVazio(dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()))) {
				detalheDocumentoPosse.put("dataDocumento", dateFormat.format(imovelRural.getDocumentoImovelRuralPosse().getDtcDocumento()));
			} else {
				throw new JSONException("Data do Documento não foi especificada.");
			}
		} else if (siglaTipoDocumentoSicar.equals("ADEC")) {
			if (!Util.isNullOuVazio(imovelRural.getDscTermoAutodeclaracao())) {
				detalheDocumentoPosse.put("autodeclaracao", imovelRural.getDscTermoAutodeclaracao());
			} else {
				throw new JSONException("Autodeclaração não foi especificada.");
			}
		}

		return detalheDocumentoPosse;
	}
	
	private static JSONObject montarObjetoDetalheDocumentoConcessao(ImovelRural imovelRural) {
		JSONObject detalheDocumentoConcessao = new JSONObject();
		JSONObject concedente = new JSONObject();
		JSONObject concessionario = new JSONObject();
		
		try {
			detalheDocumentoConcessao.put("dataRegistro",imovelRural.getDtcCriacaoAssentamento());
			detalheDocumentoConcessao.put("numeroMatricula", imovelRural.getNumMatricula());
			detalheDocumentoConcessao.put("livro",imovelRural.getDesLivro());
			detalheDocumentoConcessao.put("folha",imovelRural.getNumFolha());
			detalheDocumentoConcessao.put("municipioCartorio",imovelRural.getIdeMunicipioCartorio());
			
			if(!Util.isNullOuVazio(imovelRural.getIdeMunicipioCartorio()) && !Util.isNullOuVazio(imovelRural.getIdeMunicipioCartorio().getNomMunicipio())) {
				detalheDocumentoConcessao.put("nomeMuninipioCartorio",imovelRural.getIdeMunicipioCartorio().getNomMunicipio());
			} else {
				detalheDocumentoConcessao.put("nomeMuninipioCartorio","");
			}
			
			PctImovelRural pctImovelRural = imovelRural.getIdePctImovelRural();
			
			if(pctImovelRural.isRenderedPessoaJuridicaPctListaConcedente()) {
				PessoaJuridicaPct pjConcedente = pctImovelRural.getPessoaJuridicaPctListaConcedente().iterator().next();
				concedente.put("nome",pjConcedente.getIdePessoaJuridica().getNomRazaoSocial() );
				concedente.put("cnpj",pjConcedente.getIdePessoaJuridica().getNumCnpjFormatado() );
				detalheDocumentoConcessao.put("concedente", concedente);
			}
			
			if(pctImovelRural.isRenderedPessoaJuridicaPctListaConcessionario()) {
				PessoaJuridicaPct pjConcessionario = pctImovelRural.getPessoaJuridicaPctListaConcessionario().iterator().next();
				concessionario.put("nome",pjConcessionario.getIdePessoaJuridica().getNomRazaoSocial() );
				concessionario.put("cnpj",pjConcessionario.getIdePessoaJuridica().getNumCnpjFormatado() );
				detalheDocumentoConcessao.put("concessionario", concessionario);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return detalheDocumentoConcessao;
	}
	
	private static JSONObject montarObjetoReservaLegal(ImovelRural imovelRural) throws JSONException {
		JSONArray dadosReservas = new JSONArray();
		JSONObject reservaLegal = new JSONObject();

		if (!Util.isNull(imovelRural.getReservaLegal())
				&& imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2)
				&& imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3)
				&& ((!Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada())
						&& imovelRural.getReservaLegal().getIndAverbada())
						|| (!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal())
						|| imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(1)
						|| imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(2))) {
			reservaLegal.put("resposta", "Sim");

			JSONObject dadosReserva = new JSONObject();

			if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada()) && imovelRural.getReservaLegal().getIndAverbada()) {
				dadosReserva.put("numero", imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getNumAverbacao());
				dadosReserva.put("data", dateFormat.format(imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getDtcAverbacao()));
			} else if (!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal()) {
				dadosReserva.put("numero", imovelRural.getReservaLegal().getNumCertificado());
				dadosReserva.put("data", dateFormat.format(imovelRural.getReservaLegal().getDtcAprovacaoDeclarada()));
			} else if (imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(1)
					|| imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(2)) {
				dadosReserva.put("numero", imovelRural.getImovelRuralSicar().getNumCertificado());
				dadosReserva.put("data", dateFormat.format(imovelRural.getReservaLegal().getDtcAprovacao()));
			}

			dadosReserva.put("area", imovelRural.getReservaLegal().getValArea());

			if (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.NPI.getId())) {
				dadosReserva.put("reservaDentroImovel", "Sim");
			} else {
				dadosReserva.put("reservaDentroImovel", "Não");
				dadosReserva.put("numeroCAR", imovelRural.getReservaLegal().getNumSicarCompensacao());
			}

			dadosReservas.put(dadosReserva);

			reservaLegal.put("dadosReserva", dadosReservas);
		} else {
			reservaLegal.put("resposta", "Não");
		}

		return reservaLegal;
	}

	private static JSONArray montarObjetoInformacoes(ImovelRural imovelRural, ImovelRuralRppn imovelRuralRppn) throws JSONException {
		try {
			JSONArray informacoes = new JSONArray();
	
			informacoes.put(montarObjetoDesejaAderirPra(imovelRural));
			informacoes.put(montarObjetoPossuiDeficitRl(imovelRural));
			informacoes.put(montarObjetoExisteTac(imovelRural));
			
			if (!Util.isNull(imovelRural.getIndTac()) && imovelRural.getIndTac()) {
				informacoes.put(montarObjetoExisteTacOrgao(imovelRural));
				informacoes.put(montarObjetoExisteTacDataAssinatura(imovelRural));
				informacoes.put(montarObjetoExisteTacDataEncerramento(imovelRural));
			}
			
			informacoes.put(montarObjetoExistePrad(imovelRural));
			
			if (!Util.isNull(imovelRural.getIndPrad()) && imovelRural.getIndPrad()) {
				informacoes.put(montarObjetoExistePradOrgao(imovelRural));
				informacoes.put(montarObjetoExistePradDataAssinatura(imovelRural));
				informacoes.put(montarObjetoExistePradDataEncerramento(imovelRural));
			}
			
			informacoes.put(montarObjetoExisteInfracao(imovelRural));
			informacoes.put(montarObjetoPossuiExcedenteVegetacaoNativa(imovelRural));
			
			if (!Util.isNull(imovelRural.getIndVegetacaoNativa()) && imovelRural.getIndVegetacaoNativa()) {
				informacoes.put(montarObjetoPossuiExcedenteVegetacaoNativaFazer(imovelRural));
			}
			
			informacoes.put(montarObjetoExisteRppn(imovelRural));
			
			if (!Util.isNull(imovelRural.getIndRppn()) && imovelRural.getIndRppn()) {
				informacoes.put(montarObjetoExisteRppnArea(imovelRuralRppn));
				informacoes.put(montarObjetoExisteRppnDataPublicacao(imovelRuralRppn));
				informacoes.put(montarObjetoExisteRppnNumero(imovelRuralRppn));
			}
			
			informacoes.put(montarObjetoPossuiCrf(imovelRural));
			informacoes.put(montarObjetoRlTemporalidade(imovelRural));
			informacoes.put(montarObjetoTamanhoAlteradoApos2008(imovelRural));
			
			if (!Util.isNull(imovelRural.getIndAlteracaoTamanho()) && imovelRural.getIndAlteracaoTamanho()) {
				informacoes.put(montarObjetoTamanhoAlteradoApos2008Area(imovelRural));
			}
	
			return informacoes;
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto Informacoes.");
		}
	}

	private static JSONObject montarObjetoDesejaAderirPra(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "DESEJA_ADERIR_PRA");

		if (imovelRural.isImovelINCRA()) {
			if (!Util.isNullOuVazio(imovelRural.getIndDesejaAderirPra()) && imovelRural.getIndDesejaAderirPra()) {
				respostas.put("Sim");
			} else {
				respostas.put("Não");
			}
		} else {
			if (imovelRural.isTermoCompromisso()) {
				respostas.put("Sim");
			} else {
				respostas.put("Não");
			}
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoPossuiDeficitRl(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "POSSUI_DEFICIT_RL");

		respostas.put("Não");

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteTac(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_TAC");

		if (!Util.isNull(imovelRural.getIndTac()) && imovelRural.getIndTac()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteTacOrgao(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_TAC_ORGAO");

		respostas.put(imovelRural.getImovelRuralTac().getDscOrgaoEmissor());

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteTacDataAssinatura(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_TAC_DATA_ASSINATURA");

		respostas.put(dateFormat.format(imovelRural.getImovelRuralTac().getDtcAssinatura()));

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteTacDataEncerramento(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_TAC_DATA_ENCERRAMENTO");

		respostas.put(dateFormat.format(imovelRural.getImovelRuralTac().getDtcEncerramento()));

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExistePrad(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_PRAD");

		if (!Util.isNull(imovelRural.getIndPrad()) && imovelRural.getIndPrad()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExistePradOrgao(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_PRAD_ORGAO");

		respostas.put(imovelRural.getImovelRuralPrad().getDscOrgaoEmissor());

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExistePradDataAssinatura(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_PRAD_DATA_ASSINATURA");

		respostas.put(dateFormat.format(imovelRural.getImovelRuralPrad().getDtcAssinatura()));

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExistePradDataEncerramento(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_PRAD_DATA_ENCERRAMENTO");

		respostas.put(dateFormat.format(imovelRural.getImovelRuralPrad().getDtcEncerramento()));

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteInfracao(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_INFRACAO");

		if (!Util.isNull(imovelRural.getIndInfracaoSupressao()) && imovelRural.getIndInfracaoSupressao()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoPossuiExcedenteVegetacaoNativa(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "POSSUI_EXCEDENTE_VEGETACAO_NATIVA");

		if (!Util.isNull(imovelRural.getIndVegetacaoNativa()) && imovelRural.getIndVegetacaoNativa()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	@SuppressWarnings("deprecation")
	private static JSONObject montarObjetoPossuiExcedenteVegetacaoNativaFazer(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "POSSUI_EXCEDENTE_VEGETACAO_NATIVA_FAZER");

		Collection<VegetacaoNativaFinalidade> vegetacaoNativaFinalidades = imovelRural.getVegetacaoNativa()
				.getVegetacaoNativaFinalidadeCollection();

		if (!Util.isLazyInitExcepOuNull(vegetacaoNativaFinalidades)) {
			for (VegetacaoNativaFinalidade vegetacaoNativaFinalidade : vegetacaoNativaFinalidades) {
				respostas.put(vegetacaoNativaFinalidade.getIdeTipoFinalidadeVegetacaoNativa()
						.getDscTipoFinalidadeVegetacaoNativaSicar());
			}
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteRppn(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_RPPN");

		if (!Util.isNull(imovelRural.getIndRppn()) && imovelRural.getIndRppn()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteRppnArea(ImovelRuralRppn imovelRuralRppn) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_RPPN_AREA");

		respostas.put(imovelRuralRppn.getValArea());

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteRppnDataPublicacao(ImovelRuralRppn imovelRuralRppn)
			throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_RPPN_DATA_PUBLICACAO");

		respostas.put(dateFormat.format(imovelRuralRppn.getDtcPublicacao()));

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoExisteRppnNumero(ImovelRuralRppn imovelRuralRppn) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "EXISTE_RPPN_NUMERO");

		respostas.put(imovelRuralRppn.getDscNumeroDecreto());

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoPossuiCrf(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "POSSUI_CRF");

		if (!Util.isNull(imovelRural.getIndCrf()) && imovelRural.getIndCrf()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoRlTemporalidade(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "RL_TEMPORALIDADE");

		respostas.put("PERIODO_A_PARTIR_22_07_2008");

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoTamanhoAlteradoApos2008(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "TAMANHO_ALTERADO_APOS_2008");

		if (!Util.isNull(imovelRural.getIndAlteracaoTamanho()) && imovelRural.getIndAlteracaoTamanho()) {
			respostas.put("Sim");
		} else {
			respostas.put("Não");
		}

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONObject montarObjetoTamanhoAlteradoApos2008Area(ImovelRural imovelRural) throws JSONException {
		JSONObject informacao = new JSONObject();
		JSONArray respostas = new JSONArray();

		informacao.put("codigo", "TAMANHO_ALTERADO_APOS_2008_AREA");

		respostas.put(imovelRural.getValAlteracaoTamanho());

		informacao.put("respostas", respostas);

		return informacao;
	}

	private static JSONArray montarObjetoGeo(ImovelRural imovelRural) throws Exception {
		try {
			JSONArray geo = new JSONArray();
	
			geo.put(montarObjetoGeoImovel(imovelRural));
			
			if (imovelRural.isImovelPCT()) {
				for (JSONObject geoJson : montarObjetoGeoPct(imovelRural)) {
					geo.put(geoJson);
				}
			}
	
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
					&& (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2)
							|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))) {
				geo.put(montarObjetoGeoReservaLegal(imovelRural));
				geo.put(montarObjetoGeoReservaLegalTotal(imovelRural));
			}
			
			if (!Util.isNullOuVazio(imovelRural.getIndApp()) && imovelRural.getIndApp()) {
				for (App app : imovelRural.getAppCollection()) {
					geo.put(montarObjetoGeoApp(app));
				}
				geo.put(montarObjetoGeoAppTotal(imovelRural));
			}
			
			if (!Util.isNull(imovelRural.getIndVegetacaoNativa()) && imovelRural.getIndVegetacaoNativa()) {
				geo.put(montarObjetoGeoVegetacaoNativa(imovelRural));
			}
	
			if (!Util.isNullOuVazio(imovelRural.getIndAreaRuralConsolidada()) && imovelRural.getIndAreaRuralConsolidada()) {
				geo.put(montarObjetoGeoAreaConsolidada(imovelRural));
			}
	
			return geo;
		} catch (Exception e) {
			throw new JSONException("Falha ao montar o objeto Geo: " + e.getMessage());
		}
	}

	private static JSONObject montarObjetoGeoImovel(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoImovel = new JSONObject();
	
			geoImovel.put("tipo", "AREA_IMOVEL");
			geoImovel.put("geoJson", new JSONObject(imovelRural.getGeoJsonSicar().getGeoJson()));
			geoImovel.put("area", imovelRural.getGeoJsonSicar().getGeoArea());
	
			return geoImovel;
		} catch (Exception e) {
			throw new JSONException("Imovel.");
		}
	}

	private static java.util.List<JSONObject> montarObjetoGeoPct(ImovelRural imovelRural) throws Exception {
		try {
			JSONObject geoAreaTerritorioPct = new JSONObject();
			JSONObject geoSedeImovel = new JSONObject();
			java.util.List<JSONObject> geoPctCollection = new ArrayList<JSONObject>();
			
			/* Area Territorio Pct */
			geoAreaTerritorioPct.put("tipo", "AREA_TERRITORIO_PCT");
			geoAreaTerritorioPct.put("geoJson", new JSONObject(imovelRural.getGeoJsonSicarPctLimiteTerritorio().getGeoJson()));
			geoAreaTerritorioPct.put("area", imovelRural.getGeoJsonSicarPctLimiteTerritorio().getGeoArea());
			geoPctCollection.add(geoAreaTerritorioPct);
			
			/* Area Sede */
			geoSedeImovel.put("tipo", "SEDE_IMOVEL");
			geoSedeImovel.put("geoJson", new JSONObject(imovelRural.getGeoJsonSicarPct().getGeoJson().replace("MultiPoint", "Point")));
			geoSedeImovel.put("area", imovelRural.getGeoJsonSicarPct().getGeoArea());
			geoPctCollection.add(geoSedeImovel);
	
			return geoPctCollection;
		} catch (Exception e) {
			throw new JSONException("Pct.");
		}
	}

	private static JSONObject montarObjetoGeoReservaLegal(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoReservaLegal = new JSONObject();
	
			if ((!Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada())
					&& imovelRural.getReservaLegal().getIndAverbada())
					|| imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(2)) {
				geoReservaLegal.put("tipo", "ARL_AVERBADA");
			} else if ((!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal())
					|| imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(1)) {
				geoReservaLegal.put("tipo", "ARL_APROVADA_NAO_AVERBADA");
			} else {
				geoReservaLegal.put("tipo", "ARL_PROPOSTA");
			}
	
			geoReservaLegal.put("geoJson", new JSONObject(imovelRural.getReservaLegal().getGeoJsonSicar().getGeoJson()));
			geoReservaLegal.put("area", imovelRural.getReservaLegal().getGeoJsonSicar().getGeoArea());
	
			return geoReservaLegal;
		} catch (Exception e) {
			throw new JSONException("Reserva Legal.");
		}
	}

	private static JSONObject montarObjetoGeoReservaLegalTotal(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoReservaLegalTotal = new JSONObject();
	
			geoReservaLegalTotal.put("tipo", "ARL_TOTAL");
			geoReservaLegalTotal.put("geoJson", new JSONObject(imovelRural.getReservaLegal().getGeoJsonSicar().getGeoJson()));
			geoReservaLegalTotal.put("area", imovelRural.getReservaLegal().getGeoJsonSicar().getGeoArea());
	
			return geoReservaLegalTotal;
		} catch (Exception e) {
			throw new JSONException("Reserva Legal Total.");
		}
	}
	
	private static JSONObject montarObjetoGeoApp(App app) throws JSONException {
		try {
			JSONObject geoApp = new JSONObject();
			
			if (app.getIdeTipoApp().getIdeTipoApp() == 1) {
				geoApp.put("tipo", app.getIdeSubTipoApp().getDscSubTipoAppSicar());
			} else {
				geoApp.put("tipo", app.getIdeTipoApp().getDscTipoAppSicar());
			}
	
			geoApp.put("geoJson", new JSONObject(app.getGeoJsonSicar().getGeoJson()));
			geoApp.put("area", app.getGeoJsonSicar().getGeoArea());
	
			return geoApp;
		} catch (Exception e) {
			throw new JSONException("App.");
		}
	}

	private static JSONObject montarObjetoGeoAppTotal(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoAppTotal = new JSONObject();
			GeoJsonSicar geoJsonSicarAppTotal = imovelRural.getGeoJsonSicarAppTotal();
	
			geoAppTotal.put("tipo", "APP_TOTAL");
			geoAppTotal.put("geoJson", new JSONObject(geoJsonSicarAppTotal.getGeoJson()));
			geoAppTotal.put("area", geoJsonSicarAppTotal.getGeoArea());
	
			return geoAppTotal;
		} catch (Exception e) {
			throw new JSONException("App Total.");
		}
	}
	
	private static JSONObject montarObjetoGeoVegetacaoNativa(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoVegetacaoNativa = new JSONObject();
	
			geoVegetacaoNativa.put("tipo", "VEGETACAO_NATIVA");
			geoVegetacaoNativa.put("geoJson", new JSONObject(imovelRural.getVegetacaoNativa().getGeoJsonSicar().getGeoJson()));
			geoVegetacaoNativa.put("area", imovelRural.getVegetacaoNativa().getGeoJsonSicar().getGeoArea());
	
			return geoVegetacaoNativa;
		} catch (Exception e) {
			throw new JSONException("Vegetacao Nativa.");
		}
	}

	private static JSONObject montarObjetoGeoAreaConsolidada(ImovelRural imovelRural) throws JSONException {
		try {
			JSONObject geoAreaConsolidada = new JSONObject();
	
			geoAreaConsolidada.put("tipo", "AREA_CONSOLIDADA");
			geoAreaConsolidada.put("geoJson", new JSONObject(imovelRural.getIdeAreaRuralConsolidada().getGeoJsonSicar().getGeoJson()));
			geoAreaConsolidada.put("area", imovelRural.getIdeAreaRuralConsolidada().getGeoJsonSicar().getGeoArea());
	
			return geoAreaConsolidada;
		} catch (Exception e) {
			throw new JSONException("Area Rural Consolidada.");
		}
	}

}

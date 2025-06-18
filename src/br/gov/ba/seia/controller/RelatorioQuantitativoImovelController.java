package br.gov.ba.seia.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.RelatorioQuantitativoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named("relatorioQuantitativoImovelController")
@ViewScoped
public class RelatorioQuantitativoImovelController {

	private List<String> listArea = new ArrayList<String>();
	private String listStatus = "";
	private Date dtCadastroInicio;
	private Date dtCadastroFim;
	
	private List<GeoBahia> listMunicipio;
	private List<String> listaGidMunSelect = new ArrayList<String>();
	private List<GeoBahia> listaMunicipioSelect = new ArrayList<GeoBahia>();	
	private List<GeoBahia> listTerritorio;
	private List<String> listaGidTerSelect = new ArrayList<String>();
	private List<GeoBahia> listaTerritorioSelect = new ArrayList<GeoBahia>();
	private List<GeoBahia> listUnidade;
	private List<String> listaGidUniSelect = new ArrayList<String>();
	private List<GeoBahia> listaUnidadeSelect = new ArrayList<GeoBahia>();	
	private List<GeoBahia> listBioma;
	private List<String> listaGidBiomaSelect = new ArrayList<String>();
	private List<GeoBahia> listaBiomaSelect = new ArrayList<GeoBahia>();	
	private List<GeoBahia> listRpga;
	private List<String> listaGidRpgaSelect = new ArrayList<String>();
	private List<GeoBahia> listaRpgaSelect = new ArrayList<GeoBahia>();
	private List<GeoBahia> listBacia;
	private List<String> listaGidBaciaSelect = new ArrayList<String>();
	private List<GeoBahia> listaBaciaSelect = new ArrayList<GeoBahia>();
	private List<String> listExtra = new ArrayList<String>();
	private String tipoConsulta = "1";
	
	@EJB
	private RelatorioQuantitativoService relService;
	
	@PostConstruct
	public void init(){
		this.listArea.add("1");
		this.listArea.add("2");
		this.listStatus = "0";
	}

	public List<GeoBahia> getListaMunicipios() throws Exception {
		this.listMunicipio = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("municipio"));
		this.listMunicipio.add(0, new GeoBahia(-1, "Todos"));
		return listMunicipio;
	}

	public Collection<GeoBahia> getListaTerritorioIdentidade() throws Exception {
		this.listTerritorio = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("territorio_identidade"));
		this.listTerritorio.add(0, new GeoBahia(-1, "Todos"));
		return this.listTerritorio;
	}

	public Collection<GeoBahia> getListaUnidadeConservacao() throws Exception {
		this.listUnidade = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("unidade_conservacao"));
		this.listUnidade.add(0, new GeoBahia(-1, "Todos"));
		return this.listUnidade;
	}

	public Collection<GeoBahia> getListaBioma() throws Exception {
		this.listBioma = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("bioma"));
		this.listBioma.add(0, new GeoBahia(-1, "Todos"));
		return this.listBioma;
	}

	public Collection<GeoBahia> getListaRpga() throws Exception {
		this.listRpga = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("rpga"));
		this.listRpga.add(0, new GeoBahia(-1, "Todos"));
		return this.listRpga;
	}

	public Collection<GeoBahia> getListaBacia() throws Exception {
		this.listBacia = new ArrayList<GeoBahia>(relService.listaDadosGeoBahia("bacia"));
		this.listBacia.add(0, new GeoBahia(-1, "Todos"));
		return this.listBacia;
	}
	
	public StreamedContent getGerarRelatorioQuantitativo() {
		String sql;
		String join = "";
		String filtros = "WHERE i.ind_excluido IS FALSE ";
		String aux = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		String gidsMun = "";
		String municipios = "";
		String gidsTer = "";
		String territorios = "";
		String gidsUni = "";
		String unidades = "";
		String gidsBioma = "";
		String biomas = "";
		String gidsRpga = "";
		String rpgas = "";
		String gidsBacia = "";
		String bacias = "";
		boolean maiorQueCem = false;
		
		if(Util.isNullOuVazio(this.listArea) || this.listArea.contains("1")){
			listArea.add("1");
			this.setListArea(listArea);
		}

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			sql = "SELECT i.ide_imovel "
					+ "FROM imovel i "
					+ "JOIN imovel_rural ir ON i.ide_imovel = ir.ide_imovel_rural "
					+ "JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica ";

			if (!Util.isNullOuVazio(listStatus)) {
				if (listStatus.equals("0"))
					aux += "Registro incompleto, ";
				if (listStatus.equals("1"))
					aux += "Registrado, ";
				if (listStatus.equals("2")) {
					filtros += "AND ir.sts_certificado IS FALSE ";
					aux += "Cadastrado com TC, ";
				}
				if (listStatus.equals("3")) {
					filtros += "AND ir.sts_certificado IS TRUE ";
					aux += "Cadastrado com CI, ";
				}
				if (listStatus.equals("4")) {
					filtros += "AND ir.sts_certificado IS NOT NULL ";
					aux += "Cadastrado com TC/CI, ";
				}

				aux = aux.substring(0, aux.length() - 2) + ".";
				params.put("filtroStatus", "Status: " + aux);

				if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
					join = "JOIN reserva_legal rl ON rl.ide_imovel_rural = ir.ide_imovel_rural ";
					filtros += "AND ir.status_cadastro = 3 ";
					filtros += "AND rl.ide_tipo_arl IN (2,3) ";
				} else {
					filtros += "AND ir.status_cadastro = " + listStatus + " ";
				}
				
			} else {
				params.put("filtroStatus", null);
			}

			if (!Util.isNullOuVazio(listExtra)) {
				aux = "";
				if (listExtra.contains("3")) {
					filtros += "AND ir.ind_outros_passivos IS TRUE ";
					aux = "Outro Passivo Ambiental Fora de APP e RL, ";
				}

				if (listExtra.contains("4")) {
					filtros += "AND ir.ind_tem_prad IS TRUE ";
					aux += "PRAD Cadastrado, ";
				}

				aux = aux.substring(0, aux.length() - 2) + ".";
				params.put("filtroExtra", "Filtros Extras: " + aux);
			} else {
				params.put("filtroExtra", null);
			}

			if (!Util.isNullOuVazio(dtCadastroInicio) && !Util.isNullOuVazio(dtCadastroFim)) {
				filtros += "AND (i.dtc_criacao >= \'" + sdf.format(dtCadastroInicio) 
						+ "\' AND i.dtc_criacao <= \'" + sdf.format(dtCadastroFim) + "\') ";
				params.put("filtroPeriodoCadastro", "Período Cadastro: " + sdfr.format(dtCadastroInicio) + " a " + sdfr.format(dtCadastroFim));
			} else if (!Util.isNullOuVazio(dtCadastroInicio)) {
					Date dataAtual = new Date();
					filtros += "AND (i.dtc_criacao >= \'" + sdf.format(dtCadastroInicio) + "\') ";
					params.put("filtroPeriodoCadastro", "Período Cadastro: " + sdfr.format(dtCadastroInicio) + " a " + sdfr.format(dataAtual));
			} else if (!Util.isNullOuVazio(dtCadastroFim)) {
				filtros += "AND (i.dtc_criacao <= \'" + sdf.format(dtCadastroFim) + "\') ";
				params.put("filtroPeriodoCadastro", "Período Cadastro: até " + sdfr.format(dtCadastroFim));
			} else {
				params.put("filtroPeriodoCadastro", null);
			}

			filtros += "GROUP BY i.ide_imovel ORDER BY i.ide_imovel";

			List<Object> listImovel = new ArrayList<Object>();
			listImovel = relService.getListaObjeto(sql + join + filtros);

			// CONSULTA GEOBAHIA
			List<String> temaGeobahia = new ArrayList<String>();
			List<String> filtroGeobahia = new ArrayList<String>();

			if (!Util.isNullOuVazio(this.listaGidMunSelect)) {
				this.listaMunicipioSelect.clear();
				// SE A OPÇÃO "TODOS" FOI SELECIONADA
				if (this.listaGidMunSelect.contains("-1")) {
					this.listaMunicipioSelect = this.listMunicipio.subList(1,this.listMunicipio.size());
					municipios = "Todos.";
				} else {// PEGA OS gids DOS MUN SELECIONADOS E OBTEM O MUNICIPIO
						// DA LISTA
					for (String gid : this.listaGidMunSelect) {
						for (GeoBahia gb : this.listMunicipio) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaMunicipioSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaMunicipioSelect.size(); i++) {
					gidsMun += this.listaMunicipioSelect.get(i).getGid().toString()	+ ",";
					if (!municipios.equals("Todos.")) {
						if (i <= 100) {
							municipios += this.listaMunicipioSelect.get(i).getNome() + ", ";
						} else if (!maiorQueCem) {
							municipios = municipios.substring(0,municipios.length() - 2);
							municipios += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsMun = gidsMun.substring(0, gidsMun.length() - 1);
				if (!maiorQueCem && !municipios.equals("Todos."))
					municipios = municipios.substring(0,municipios.length() - 2);

				temaGeobahia.add("1");
				filtroGeobahia.add("'" + gidsMun.toString() + "'");
				params.put("filtroMunicipio", "Município(s): " + municipios);
			} else {
				params.put("filtroMunicipio", null);
			}
			if (!Util.isNullOuVazio(listaGidTerSelect)) {
				this.listaTerritorioSelect.clear();

				// SE A OPÇÃO "TODOS" FOI SELECIONADA
				if (this.listaGidTerSelect.contains("-1")) {
					this.listaTerritorioSelect = this.listTerritorio.subList(1,this.listTerritorio.size());
					territorios = "Todos.";
				} else {// PEGA OS gids DOS TERRITORIOS SELECIONADOS E OBTEM O
						// TERRITORIOS DA LISTA
					for (String gid : this.listaGidTerSelect) {
						for (GeoBahia gb : this.listTerritorio) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaTerritorioSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaTerritorioSelect.size(); i++) {
					gidsTer += this.listaTerritorioSelect.get(i).getGid().toString()+ ",";
					if (!territorios.equals("Todos.")) {
						if (i <= 100) {
							territorios += this.listaTerritorioSelect.get(i).getNome() + ", ";
						} else if (!maiorQueCem) {
							territorios = territorios.substring(0,territorios.length() - 2);
							territorios += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsTer = gidsTer.substring(0, gidsTer.length() - 1);
				if (!maiorQueCem && !territorios.equals("Todos."))
					territorios = territorios.substring(0,territorios.length() - 2);

				temaGeobahia.add("2");
				filtroGeobahia.add("'" + gidsTer.toString() + "'");
				params.put("filtroTerritorio", "Territorio(s) de Identidade: "+ territorios);

			} else {
				params.put("filtroTerritorio", null);
			}
			if (!Util.isNullOuVazio(listaGidUniSelect)) {
				this.listaUnidadeSelect.clear();
				if (this.listaGidUniSelect.contains("-1")) {
					this.listaUnidadeSelect = this.listUnidade.subList(1,this.listUnidade.size());
					unidades = "Todos.";
				} else {
					for (String gid : this.listaGidUniSelect) {
						for (GeoBahia gb : this.listUnidade) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaUnidadeSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaUnidadeSelect.size(); i++) {
					gidsUni += this.listaUnidadeSelect.get(i).getGid().toString()+ ",";
					if (!unidades.equals("Todos.")) {
						if (i <= 100) {
							unidades += this.listaUnidadeSelect.get(i).getNome() + ", ";
						} else if (!maiorQueCem) {
							unidades = unidades.substring(0,unidades.length() - 2);
							unidades += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsUni = gidsUni.substring(0, gidsUni.length() - 1);
				if (!maiorQueCem && !unidades.equals("Todos."))
					unidades = unidades.substring(0, unidades.length() - 2);

				temaGeobahia.add("3");
				filtroGeobahia.add("'" + gidsUni.toString() + "'");
				params.put("filtroUnidade", "Unidade(s) de Conservação: "+ unidades);

			} else {
				params.put("filtroUnidade", null);
			}
			if (!Util.isNullOuVazio(listaGidBiomaSelect)) {
				this.listaBiomaSelect.clear();
				if (this.listaGidBiomaSelect.contains("-1")) {
					this.listaBiomaSelect = this.listBioma.subList(1, this.listBioma.size());
					biomas = "Todos.";
				} else {
					for (String gid : this.listaGidBiomaSelect) {
						for (GeoBahia gb : this.listBioma) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaBiomaSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaBiomaSelect.size(); i++) {
					gidsBioma += this.listaBiomaSelect.get(i).getGid().toString() + ",";
					if (!biomas.equals("Todos.")) {
						if (i <= 100) {
							biomas += this.listaBiomaSelect.get(i).getNome() + ", ";
						} else if (!maiorQueCem) {
							biomas = biomas.substring(0, biomas.length() - 2);
							biomas += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsBioma = gidsBioma.substring(0, gidsBioma.length() - 1);
				if (!maiorQueCem && !biomas.equals("Todos."))
					biomas = biomas.substring(0, biomas.length() - 2);

				temaGeobahia.add("4");
				filtroGeobahia.add("'" + gidsBioma.toString() + "'");
				params.put("filtroBioma", "Bioma(s): " + biomas);

			} else {
				params.put("filtroBioma", null);
			}
			if (!Util.isNullOuVazio(listaGidRpgaSelect)) {
				this.listaRpgaSelect.clear();
				if (this.listaGidRpgaSelect.contains("-1")) {
					this.listaRpgaSelect = this.listRpga.subList(1,this.listRpga.size());
					rpgas = "Todos.";
				} else {
					for (String gid : this.listaGidRpgaSelect) {
						for (GeoBahia gb : this.listRpga) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaRpgaSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaRpgaSelect.size(); i++) {
					gidsRpga += this.listaRpgaSelect.get(i).getGid().toString()+ ",";
					if (!rpgas.equals("Todos.")) {
						if (i <= 100) {
							rpgas += this.listaRpgaSelect.get(i).getNome()+ ", ";
						} else if (!maiorQueCem) {
							rpgas = rpgas.substring(0, rpgas.length() - 2);
							rpgas += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsRpga = gidsRpga.substring(0, gidsRpga.length() - 1);
				if (!maiorQueCem && !rpgas.equals("Todos."))
					rpgas = rpgas.substring(0, rpgas.length() - 2);

				temaGeobahia.add("5");
				filtroGeobahia.add("'" + gidsRpga.toString() + "'");
				params.put("filtroRpga", "RPGA(s): " + rpgas);

			} else {
				params.put("filtroRpga", null);
			}
			if (!Util.isNullOuVazio(listaGidBaciaSelect)) {
				this.listaBaciaSelect.clear();
				if (this.listaGidBaciaSelect.contains("-1")) {
					this.listaBaciaSelect = this.listBacia.subList(1,this.listBacia.size());
					bacias = "Todos.";
				} else {
					for (String gid : this.listaGidBaciaSelect) {
						for (GeoBahia gb : this.listBacia) {
							if (gb.getGid().toString().equals(gid)) {
								this.listaBaciaSelect.add(gb);
							}
						}
					}
				}
				maiorQueCem = false;
				for (int i = 0; i < this.listaBaciaSelect.size(); i++) {
					gidsBacia += this.listaBaciaSelect.get(i).getGid().toString()+ ",";
					if (!bacias.equals("Todos.")) {
						if (i <= 100) {
							bacias += this.listaBaciaSelect.get(i).getNome()+ ", ";
						} else if (!maiorQueCem) {
							bacias = bacias.substring(0, bacias.length() - 2);
							bacias += " ...";
							maiorQueCem = true;
						}
					}
				}

				gidsBacia = gidsBacia.substring(0, gidsBacia.length() - 1);
				if (!maiorQueCem && !bacias.equals("Todos."))
					bacias = bacias.substring(0, bacias.length() - 2);

				temaGeobahia.add("6");
				filtroGeobahia.add("'" + gidsBacia.toString() + "'");
				params.put("filtroBacia", "Bacia(s) Hidrográfica: " + bacias);

			} else {
				params.put("filtroBacia", null);
			}

			if (listImovel.size() > 0) {
				String idesImovel = listImovel.toString().replace("[", "").replace("]", "");
				
				// FILTRO GEOBAHIA
				if ((Util.isNullOuVazio(listArea) || listArea.contains("1")) && temaGeobahia.size() > 0) {
					listImovel.clear();					
					if(tipoConsulta.equals("1")){	//se for interseção
						listImovel = relService.filtrarObjetosGeoBahia("imv",idesImovel, temaGeobahia.toString(), filtroGeobahia.toString());
					}else{
						for (int i = 0; i < temaGeobahia.size(); i++) {							
							listImovel.addAll(relService.filtrarObjetosGeoBahia("imv",idesImovel, "["+temaGeobahia.get(i)+"]","["+filtroGeobahia.get(i)+"]"));							
						}						
					}
					listImovel = new ArrayList<Object>(new HashSet<Object>(listImovel));
					idesImovel = listImovel.toString().replace("[", "").replace("]", "");					
				}

				if (!Util.isNullOuVazio(idesImovel)) {
					if (Util.isNullOuVazio(listArea) || listArea.contains("1")) {
						params.put("proQTD", listImovel.size());

						Double totalAreaImovel = Double.parseDouble(relService.getTotalArea("imovel_rural",	"ide_imovel_rural", idesImovel).get(0).toString());
						params.put("proAREA", df.format(totalAreaImovel));

						if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
							Double totalAreaImovelGeo = Double.parseDouble(relService.getTotalAreaGeorreferenciada("imovel_rural", "ir", "ide_imovel_rural", idesImovel).get(0).toString());
							params.put("proAREAGEO", df.format(totalAreaImovelGeo));
						} else {
							params.put("proAREAGEO", "---");
						}
					} else {
						params.put("proQTD", "");
						params.put("proAREA", "");
						params.put("proAREAGEO", "");
					}

					// RESERVA LEGAL
					if (Util.isNullOuVazio(listArea) || listArea.contains("2")) {
						List<Object> listReserva = new ArrayList<Object>();
						String idesReserva = "";

						aux = "SELECT ide_reserva_legal FROM reserva_legal WHERE ide_imovel_rural IN ("	+ idesImovel + ")";
						listReserva = relService.getListaObjeto(aux);
						
						if (!Util.isNullOuVazio(listReserva) && listReserva.size() > 0) {
							idesReserva = listReserva.toString().replace("[", "").replace("]", "");

							if (!listArea.contains("1")	&& temaGeobahia.size() > 0) {
								listReserva = relService.filtrarObjetosGeoBahia("rle", idesReserva,	temaGeobahia.toString(), filtroGeobahia.toString());
								idesReserva = listReserva.toString().replace("[", "").replace("]", "");
							}
							
							Double totalAreaReserva = idesReserva.equals("") ? 0 : Double.parseDouble(relService.getTotalArea("reserva_legal","ide_reserva_legal", idesReserva).get(0).toString());
							params.put("rlAREA", df.format(totalAreaReserva));
							
							if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
								Double totalAreaReservaGeo = idesReserva.equals("") ? 0 : Double.parseDouble(relService.getTotalAreaGeorreferenciada("reserva_legal","rl","ide_reserva_legal", idesReserva).get(0).toString());
								params.put("rlAREAGEO", df.format(totalAreaReservaGeo));
							} else {
								params.put("rlAREAGEO", "---");
							}
						}
						params.put("rlQTD", listReserva.size());
					} else {
						params.put("rlQTD", "");
						params.put("rlAREA", "");
						params.put("rlAREAGEO", "");
					}

					// APP
					if ((Util.isNullOuVazio(listArea) || listArea.contains("3")) && !listStatus.equals("1")) {
						List<Object> listApp = new ArrayList<Object>();
						String idesApp = "";

						aux = "SELECT ide_app FROM app WHERE ide_imovel_rural IN (" + idesImovel + ")";
						listApp = relService.getListaObjeto(aux);

						if (!Util.isNullOuVazio(listApp) && listApp.size() > 0) {
							idesApp = listApp.toString().replace("[", "").replace("]", "");

							if (!listArea.contains("1")
									&& temaGeobahia.size() > 0) {
								listApp = relService.filtrarObjetosGeoBahia("app", idesApp, temaGeobahia.toString(), filtroGeobahia.toString());
								idesApp = listApp.toString().replace("[", "").replace("]", "");
							}

							Double totalAreaApp = idesApp.equals("") ? 0 :Double.parseDouble(relService.getTotalArea("app", "ide_app", idesApp).get(0).toString());
							params.put("appAREA", df.format(totalAreaApp));

							if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
								Double totalAreaAppGeo = idesApp.equals("") ? 0 :Double.parseDouble(relService.getTotalAreaGeorreferenciada("app", "app", "ide_app", idesApp).get(0).toString());
								params.put("appAREAGEO", df.format(totalAreaAppGeo));
							} else {
								params.put("appAREAGEO", "---");
							}
						}
						params.put("appQTD", listApp.size());
					} else {
						params.put("appQTD", "");
						params.put("appAREA", "");
						params.put("appAREAGEO", "");
					}

					// ÁREA PRODUTIVA
					if ((Util.isNullOuVazio(listArea) || listArea.contains("4")) && !listStatus.equals("1")) {
						List<Object> listAreaProdutiva = new ArrayList<Object>();
						String idesAreaProdutiva = "";

						aux = "SELECT ide_area_produtiva FROM area_produtiva WHERE ide_imovel_rural IN (" + idesImovel + ")";
						listAreaProdutiva = relService.getListaObjeto(aux);

						if (!Util.isNullOuVazio(listAreaProdutiva) && listAreaProdutiva.size() > 0) {
							idesAreaProdutiva = listAreaProdutiva.toString().replace("[", "").replace("]", "");

							if (!listArea.contains("1") && temaGeobahia.size() > 0) {
								listAreaProdutiva = relService.filtrarObjetosGeoBahia("apr", idesAreaProdutiva, temaGeobahia.toString(), filtroGeobahia.toString());
								idesAreaProdutiva = listAreaProdutiva.toString().replace("[", "").replace("]", "");
							}

							Double totalAreaProdutiva = idesAreaProdutiva.equals("") ? 0 : Double.parseDouble(relService.getTotalArea("area_produtiva", "ide_area_produtiva", idesAreaProdutiva).get(0).toString());
							params.put("apAREA", df.format(totalAreaProdutiva));
							
							if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
								Double totalAreaProdutivaGeo = idesAreaProdutiva.equals("") ? 0 : Double.parseDouble(relService.getTotalAreaGeorreferenciada("area_produtiva", "ap", "ide_area_produtiva", idesAreaProdutiva).get(0).toString());
								params.put("apAREAGEO", df.format(totalAreaProdutivaGeo));
							} else {
								params.put("apAREAGEO", "---");
							}
						}
						params.put("apQTD", listAreaProdutiva.size());
					} else {
						params.put("apQTD", "");
						params.put("apAREA", "");
						params.put("apAREAGEO", "");
					}

					// VEGETAÇÃO NATIVA
					if ((Util.isNullOuVazio(listArea) || listArea.contains("5")) && !listStatus.equals("1")) {
						List<Object> listVegetacao = new ArrayList<Object>();
						String idesVegetacao = "";

						aux = "SELECT ide_vegetacao_nativa FROM vegetacao_nativa WHERE ide_imovel_rural IN (" + idesImovel + ")";
						listVegetacao = relService.getListaObjeto(aux);

						if (!Util.isNullOuVazio(listVegetacao) && listVegetacao.size() > 0) {
							idesVegetacao = listVegetacao.toString().replace("[", "").replace("]", "");

							if (!listArea.contains("1") && temaGeobahia.size() > 0) {
								listVegetacao = relService.filtrarObjetosGeoBahia("vgn", idesVegetacao, temaGeobahia.toString(), filtroGeobahia.toString());
								idesVegetacao = listVegetacao.toString().replace("[", "").replace("]", "");
							}

							Double totalAreaVegetacao = idesVegetacao.equals("") ? 0 : Double.parseDouble(relService.getTotalArea("vegetacao_nativa", "ide_vegetacao_nativa", idesVegetacao).get(0).toString());
							params.put("rvnAREA", df.format(totalAreaVegetacao));

							if (listStatus.equals("2") || listStatus.equals("3") || listStatus.equals("4")) {
								Double totalAreaVegetacaoGeo = idesVegetacao.equals("") ? 0 : Double.parseDouble(relService.getTotalAreaGeorreferenciada("vegetacao_nativa", "vn", "ide_vegetacao_nativa", idesVegetacao).get(0).toString());
								params.put("rvnAREAGEO", df.format(totalAreaVegetacaoGeo));
							} else {
								params.put("rvnAREAGEO", "---");
							}
						}
						params.put("rvnQTD", listVegetacao.size());
					} else {
						params.put("rvnQTD", "");
						params.put("rvnAREA", "");
						params.put("rvnAREAGEO", "");
					}
				}
			}

			RelatorioUtil lRelatorio = new RelatorioUtil("relatorioQuantitativoImovel.jrxml", params);
			DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			relatorio.setContentType("application/pdf");
			return relatorio;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public List<String> getListArea() {
		return listArea;
	}

	public void setListArea(List<String> listArea) {
		/*if (listArea.contains("1")) {
			this.listArea.clear();
			this.listArea.add("1");
			this.listArea.add("2");
			this.listArea.add("3");
			this.listArea.add("4");
			this.listArea.add("5");
		} else {
			this.listArea.clear();*/
			this.listArea = listArea;
		//}
	}

	public String getListStatus() {
		return this.listStatus;
	}

	public void setListStatus(String listStatus) {
		this.listStatus = listStatus;
	}

	public List<GeoBahia> getListMunicipio() {
		return listMunicipio;
	}

	public void setListMunicipio(List<GeoBahia> listMunicipio) {
		this.listMunicipio = listMunicipio;
	}

	public List<GeoBahia> getListTerritorio() {
		return listTerritorio;
	}

	public void setListTerritorio(List<GeoBahia> listTerritorio) {
		this.listTerritorio = listTerritorio;
	}

	public List<GeoBahia> getListUnidade() {
		return listUnidade;
	}

	public void setListUnidade(List<GeoBahia> listUnidade) {
		this.listUnidade = listUnidade;
	}

	public List<GeoBahia> getListBioma() {
		return listBioma;
	}

	public void setListBioma(List<GeoBahia> listBioma) {
		this.listBioma = listBioma;
	}

	public List<GeoBahia> getListRpga() {
		return listRpga;
	}

	public void setListRpga(List<GeoBahia> listRpga) {
		this.listRpga = listRpga;
	}

	public List<GeoBahia> getListBacia() {
		return listBacia;
	}

	public void setListBacia(List<GeoBahia> listBacia) {
		this.listBacia = listBacia;
	}

	public List<String> getListaGidMunSelect() {
		return listaGidMunSelect;
	}

	public void setListaGidMunSelect(List<String> listaGidMunSelect) {
		this.listaGidMunSelect = listaGidMunSelect;
	}

	public List<String> getListaGidTerSelect() {
		return listaGidTerSelect;
	}

	public void setListaGidTerSelect(List<String> listaGidTerSelect) {
		this.listaGidTerSelect = listaGidTerSelect;
	}

	public List<String> getListaGidUniSelect() {
		return listaGidUniSelect;
	}

	public void setListaGidUniSelect(List<String> listaGidUniSelect) {
		this.listaGidUniSelect = listaGidUniSelect;
	}

	public List<String> getListaGidBiomaSelect() {
		return listaGidBiomaSelect;
	}

	public void setListaGidBiomaSelect(List<String> listaGidBioSelect) {
		this.listaGidBiomaSelect = listaGidBioSelect;
	}

	public List<String> getListaGidRpgaSelect() {
		return listaGidRpgaSelect;
	}

	public void setListaGidRpgaSelect(List<String> listaGidRpgaSelect) {
		this.listaGidRpgaSelect = listaGidRpgaSelect;
	}

	public List<String> getListaGidBaciaSelect() {
		return listaGidBaciaSelect;
	}

	public void setListaGidBaciaSelect(List<String> listaGidBaciaSelect) {
		this.listaGidBaciaSelect = listaGidBaciaSelect;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public List<String> getListExtra() {
		return listExtra;
	}

	public void setListExtra(List<String> listExtra) {
		this.listExtra = listExtra;
	}

	public Date getDtCadastroInicio() {
		return dtCadastroInicio;
	}

	public void setDtCadastroInicio(Date dtCadastroInicio) {
		this.dtCadastroInicio = dtCadastroInicio;
	}

	public Date getDtCadastroFim() {
		return dtCadastroFim;
	}

	public void setDtCadastroFim(Date dtCadastroFim) {
		this.dtCadastroFim = dtCadastroFim;
	}
	
}
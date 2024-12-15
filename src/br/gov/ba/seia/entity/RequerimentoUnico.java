package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author alex.santos
 */
@Entity
@Table(name = "requerimento_unico")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RequerimentoUnico.findAll", query = "SELECT r FROM RequerimentoUnico r"),
		@NamedQuery(name = "RequerimentoUnico.findByIdeRequerimentoUnico", query = "SELECT r FROM RequerimentoUnico r WHERE r.ideRequerimentoUnico = :ideRequerimentoUnico"),
		@NamedQuery(name = "RequerimentoUnico.findByIndTramiteInema", query = "SELECT r FROM RequerimentoUnico r WHERE r.indTramiteInema = :indTramiteInema"),
		@NamedQuery(name = "RequerimentoUnico.findByIndTramiteAna", query = "SELECT r FROM RequerimentoUnico r WHERE r.indTramiteAna = :indTramiteAna"),
		@NamedQuery(name = "RequerimentoUnico.findByIndSupressaoVegetacao", query = "SELECT r FROM RequerimentoUnico r WHERE r.indSupressaoVegetacao = :indSupressaoVegetacao"),
		@NamedQuery(name = "RequerimentoUnico.findByIndPlanoManejo", query = "SELECT r FROM RequerimentoUnico r WHERE r.indPlanoManejo = :indPlanoManejo"),
		@NamedQuery(name = "RequerimentoUnico.findByIndServidaoFlorestal", query = "SELECT r FROM RequerimentoUnico r WHERE r.indServidaoFlorestal = :indServidaoFlorestal"),
		@NamedQuery(name = "RequerimentoUnico.findByNumOutorgaProtocoloAbertura", query = "SELECT r FROM RequerimentoUnico r WHERE r.numOutorgaProtocoloAbertura = :numOutorgaProtocoloAbertura"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProcOutorgaAtende", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProcOutorgaAtende = :indProcOutorgaAtende"),
		@NamedQuery(name = "RequerimentoUnico.findByIndUtilizaAgua", query = "SELECT r FROM RequerimentoUnico r WHERE r.indUtilizaAgua = :indUtilizaAgua"),
		@NamedQuery(name = "RequerimentoUnico.findByIndRealizaEmissaoResiduosLiquidos", query = "SELECT r FROM RequerimentoUnico r WHERE r.indRealizaEmissaoResiduosLiquidos = :indRealizaEmissaoResiduosLiquidos"),
		@NamedQuery(name = "RequerimentoUnico.findByIndIntervencaoCorpoHidrico", query = "SELECT r FROM RequerimentoUnico r WHERE r.indIntervencaoCorpoHidrico = :indIntervencaoCorpoHidrico"),
		@NamedQuery(name = "RequerimentoUnico.findByIndRegularizarPerfPoco", query = "SELECT r FROM RequerimentoUnico r WHERE r.indRegularizarPerfPoco = :indRegularizarPerfPoco"),
		@NamedQuery(name = "RequerimentoUnico.findByIndPocoRegularizarPerfurado", query = "SELECT r FROM RequerimentoUnico r WHERE r.indPocoRegularizarPerfurado = :indPocoRegularizarPerfurado"),
		@NamedQuery(name = "RequerimentoUnico.findByIndVolumeMaterial", query = "SELECT r FROM RequerimentoUnico r WHERE r.indVolumeMaterial = :indVolumeMaterial"),
		@NamedQuery(name = "RequerimentoUnico.findByIndCaptacaoBarramento", query = "SELECT r FROM RequerimentoUnico r WHERE r.indCaptacaoBarramento = :indCaptacaoBarramento"),
		@NamedQuery(name = "RequerimentoUnico.findByIndPocoCaptacaoPerfurado", query = "SELECT r FROM RequerimentoUnico r WHERE r.indPocoCaptacaoPerfurado = :indPocoCaptacaoPerfurado"),
		@NamedQuery(name = "RequerimentoUnico.findByIndAutorizacaoPerfuracaoPoco", query = "SELECT r FROM RequerimentoUnico r WHERE r.indAutorizacaoPerfuracaoPoco = :indAutorizacaoPerfuracaoPoco"),
		@NamedQuery(name = "RequerimentoUnico.findByNumVazaoCaptacao", query = "SELECT r FROM RequerimentoUnico r WHERE r.numVazaoCaptacao = :numVazaoCaptacao"),
		@NamedQuery(name = "RequerimentoUnico.findByDtcPerfPoco", query = "SELECT r FROM RequerimentoUnico r WHERE r.dtcPerfPoco = :dtcPerfPoco"),
		@NamedQuery(name = "RequerimentoUnico.findByNumProcessoAutorizacaoPoco", query = "SELECT r FROM RequerimentoUnico r WHERE r.numProcessoAutorizacaoPoco = :numProcessoAutorizacaoPoco"),
		@NamedQuery(name = "RequerimentoUnico.findByDscOutrosobjtLimpArea", query = "SELECT r FROM RequerimentoUnico r WHERE r.dscOutrosobjtLimpArea = :dscOutrosobjtLimpArea"),
		@NamedQuery(name = "RequerimentoUnico.findByDtcInicioLimpArea", query = "SELECT r FROM RequerimentoUnico r WHERE r.dtcInicioLimpArea = :dtcInicioLimpArea"),
		@NamedQuery(name = "RequerimentoUnico.findByDtcFinalLimpArea", query = "SELECT r FROM RequerimentoUnico r WHERE r.dtcFinalLimpArea = :dtcFinalLimpArea"),
		@NamedQuery(name = "RequerimentoUnico.findByIndBarragemCaptConstruida", query = "SELECT r FROM RequerimentoUnico r WHERE r.indBarragemCaptConstruida = :indBarragemCaptConstruida"),
		@NamedQuery(name = "RequerimentoUnico.findByIndAutorizacaoConstrucaoBarragemCapt", query = "SELECT r FROM RequerimentoUnico r WHERE r.indAutorizacaoConstrucaoBarragemCapt = :indAutorizacaoConstrucaoBarragemCapt"),
		@NamedQuery(name = "RequerimentoUnico.findByIndBarragemIntervConstruida", query = "SELECT r FROM RequerimentoUnico r WHERE r.indBarragemIntervConstruida = :indBarragemIntervConstruida"),
		@NamedQuery(name = "RequerimentoUnico.findByIndAutorizacaoConstrucaoBarragemInterv", query = "SELECT r FROM RequerimentoUnico r WHERE r.indAutorizacaoConstrucaoBarragemInterv = :indAutorizacaoConstrucaoBarragemInterv"),
		@NamedQuery(name = "RequerimentoUnico.findByIndRealizarQueimaControlada", query = "SELECT r FROM RequerimentoUnico r WHERE r.indRealizarQueimaControlada = :indRealizarQueimaControlada"),
		@NamedQuery(name = "RequerimentoUnico.findByIndUtilMatLenhosoArvrMortas", query = "SELECT r FROM RequerimentoUnico r WHERE r.indUtilMatLenhosoArvrMortas = :indUtilMatLenhosoArvrMortas"),
		@NamedQuery(name = "RequerimentoUnico.findByIndDeclaracaoPlanoSuprSust", query = "SELECT r FROM RequerimentoUnico r WHERE r.indDeclaracaoPlanoSuprSust = :indDeclaracaoPlanoSuprSust"),
		@NamedQuery(name = "RequerimentoUnico.findByIndVincFlorProdRepFlor", query = "SELECT r FROM RequerimentoUnico r WHERE r.indVincFlorProdRepFlor = :indVincFlorProdRepFlor"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortariaLicSilvicultura", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortariaLicSilvicultura = :numPortariaLicSilvicultura"),
		@NamedQuery(name = "RequerimentoUnico.findByIndTransfCredRepFlorestal", query = "SELECT r FROM RequerimentoUnico r WHERE r.indTransfCredRepFlorestal = :indTransfCredRepFlorestal"),
		@NamedQuery(name = "RequerimentoUnico.findByIndAprovExecPlanoMnjFlor", query = "SELECT r FROM RequerimentoUnico r WHERE r.indAprovExecPlanoMnjFlor = :indAprovExecPlanoMnjFlor"),
		@NamedQuery(name = "RequerimentoUnico.findByNumProcessoAprovExecPlanoMnjFlor", query = "SELECT r FROM RequerimentoUnico r WHERE r.numProcessoAprovExecPlanoMnjFlor = :numProcessoAprovExecPlanoMnjFlor"),
		@NamedQuery(name = "RequerimentoUnico.findByIndCortFlorProd", query = "SELECT r FROM RequerimentoUnico r WHERE r.indCortFlorProd = :indCortFlorProd"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProrValidAsv", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProrValidAsv = :indProrValidAsv"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProrValidRcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProrValidRcfp = :indProrValidRcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProrValidAcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProrValidAcfp = :indProrValidAcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProrValidLa", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProrValidLa = :indProrValidLa"),
		@NamedQuery(name = "RequerimentoUnico.findByIndProrValidNao", query = "SELECT r FROM RequerimentoUnico r WHERE r.indProrValidNao = :indProrValidNao"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortAsv", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortAsv = :numPortAsv"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortRcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortRcfp = :numPortRcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortAcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortAcfp = :numPortAcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortLa", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortLa = :numPortLa"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortCertRegFlorProd", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortCertRegFlorProd = :numPortCertRegFlorProd"),
		@NamedQuery(name = "RequerimentoUnico.findByIndFlorVincRepFlor", query = "SELECT r FROM RequerimentoUnico r WHERE r.indFlorVincRepFlor = :indFlorVincRepFlor"),
		@NamedQuery(name = "RequerimentoUnico.findByNumPortEmissCredRepFlor", query = "SELECT r FROM RequerimentoUnico r WHERE r.numPortEmissCredRepFlor = :numPortEmissCredRepFlor"),
		@NamedQuery(name = "RequerimentoUnico.findByIndRecVolFlorRem", query = "SELECT r FROM RequerimentoUnico r WHERE r.indRecVolFlorRem = :indRecVolFlorRem"),
		@NamedQuery(name = "RequerimentoUnico.findByIndOrigMatLenAsv", query = "SELECT r FROM RequerimentoUnico r WHERE r.indOrigMatLenAsv = :indOrigMatLenAsv"),
		@NamedQuery(name = "RequerimentoUnico.findByIndOrigMatLenRcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.indOrigMatLenRcfp = :indOrigMatLenRcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByIndOrigMatLenAcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.indOrigMatLenAcfp = :indOrigMatLenAcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByNumOrigMatLenAsv", query = "SELECT r FROM RequerimentoUnico r WHERE r.numOrigMatLenAsv = :numOrigMatLenAsv"),
		@NamedQuery(name = "RequerimentoUnico.findByNumOrigMatLenRcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.numOrigMatLenRcfp = :numOrigMatLenRcfp"),
		@NamedQuery(name = "RequerimentoUnico.findByNumOrigMatLenAcfp", query = "SELECT r FROM RequerimentoUnico r WHERE r.numOrigMatLenAcfp = :numOrigMatLenAcfp"),
		//@NamedQuery(name = "RequerimentoUnico.findByRequerente", query = "SELECT ru FROM RequerimentoUnico ru left join ru.requerimento r left join r.empreendimentoCollection e where e.idePessoa.idePessoa = :idePessoa"),
		//@NamedQuery(name = "RequerimentoUnico.findByRepresentanteLegal", query = "SELECT ru FROM RequerimentoUnico ru left join ru.requerimento r left join r.empreendimentoCollection e left join e.idePessoa p left join p.pessoaJuridica pj left join pj.representanteLegalCollection rl where rl.idePessoaFisica.idePessoaFisica = :idePessoa"),
		//@NamedQuery(name = "RequerimentoUnico.findByProcuradorPessoaFisica", query = "SELECT ru FROM RequerimentoUnico ru left join ru.requerimento r left join r.empreendimentoCollection e left join e.procuradorPfEmpreendimentoCollection ppfe left join ppfe.ideProcuradorPessoaFisica ppf left join ppf.idePessoaFisica pf where pf.idePessoaFisica = :idePessoa"),
		//@NamedQuery(name = "RequerimentoUnico.findByProcuradorPessoaJuridica", query = "SELECT ru FROM RequerimentoUnico ru left join ru.requerimento r left join r.empreendimentoCollection e left join e.procuradorRepEmpreendimentoCollection ppje left join ppje.ideProcuradorRepresentante ppj left join ppj.ideProcurador pf where pf.idePessoaFisica = :idePessoa") 
		})
public class RequerimentoUnico implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ide_requerimento_unico")
	private Integer ideRequerimentoUnico;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_tramite_inema", nullable = false)
	private Boolean indTramiteInema;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_tramite_ana", nullable = false)
	private Boolean indTramiteAna;
	@Column(name = "ind_supressao_vegetacao")
	private Boolean indSupressaoVegetacao;
	@Column(name = "ind_plano_manejo")
	private Boolean indPlanoManejo;
	@Column(name = "ind_servidao_florestal")
	private Boolean indServidaoFlorestal;
	@Column(name = "ind_reloc_servid_florest")
	private Boolean indRelocServidFlorest;
	@Size(max = 40)
	@Column(name = "num_outorga_protocolo_abertura", length = 40)
	private String numOutorgaProtocoloAbertura;
	@Column(name = "ind_proc_outorga_atende")
	private Boolean indProcOutorgaAtende;
	@Column(name = "ind_utiliza_agua", columnDefinition = "default 'false'")
	private Boolean indUtilizaAgua;
	@Column(name = "ind_realiza_emissao_residuos_liquidos")
	private Boolean indRealizaEmissaoResiduosLiquidos;
	@Column(name = "ind_intervencao_corpo_hidrico")
	private Boolean indIntervencaoCorpoHidrico;
	@Column(name = "ind_regularizar_perf_poco")
	private Boolean indRegularizarPerfPoco;
	@Column(name = "ind_poco_regularizar_perfurado")
	private Boolean indPocoRegularizarPerfurado;
	@Column(name = "ind_volume_material")
	private Boolean indVolumeMaterial;
	@Column(name = "ind_captacao_barramento")
	private Boolean indCaptacaoBarramento;
	@Column(name = "ind_poco_captacao_perfurado")
	private Boolean indPocoCaptacaoPerfurado;
	@Column(name = "ind_autorizacao_perfuracao_poco")
	private Boolean indAutorizacaoPerfuracaoPoco;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "num_vazao_captacao", precision = 10, scale = 3)
	private BigDecimal numVazaoCaptacao;
	@Column(name = "dtc_perf_poco")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPerfPoco;
	@Size(max = 40)
	@Column(name = "num_processo_autorizacao_poco", length = 40)
	private String numProcessoAutorizacaoPoco;
	@Size(max = 100)
	@Column(name = "dsc_outrosobjt_limp_area", length = 100)
	private String dscOutrosobjtLimpArea;
	@Column(name = "dtc_inicio_limp_area")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcInicioLimpArea;
	@Column(name = "dtc_final_limp_area")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFinalLimpArea;
	@Column(name = "ind_barragem_capt_construida")
	private Boolean indBarragemCaptConstruida;
	@Column(name = "ind_autorizacao_construcao_barragem_capt")
	private Boolean indAutorizacaoConstrucaoBarragemCapt;
	@Column(name = "ind_barragem_interv_construida")
	private Boolean indBarragemIntervConstruida;
	@Column(name = "ind_autorizacao_construcao_barragem_interv")
	private Boolean indAutorizacaoConstrucaoBarragemInterv;
	@Column(name = "ind_realizar_queima_controlada")
	private Boolean indRealizarQueimaControlada;
	@Column(name = "ind_util_mat_lenhoso_arvr_mortas")
	private Boolean indUtilMatLenhosoArvrMortas;
	@Column(name = "ind_declaracao_plano_supr_sust")
	private Boolean indDeclaracaoPlanoSuprSust;
	@Column(name = "ind_vinc_flor_prod_rep_flor")
	private Boolean indVincFlorProdRepFlor;
	@Size(max = 50)
	@Column(name = "num_portaria_lic_silvicultura", length = 50)
	private String numPortariaLicSilvicultura;
	@Column(name = "ind_transf_cred_rep_florestal")
	private Boolean indTransfCredRepFlorestal;
	@Column(name = "ind_aprov_exec_plano_mnj_flor")
	private Boolean indAprovExecPlanoMnjFlor;
	@Size(max = 50)
	@Column(name = "num_processo_aprov_exec_plano_mnj_flor", length = 50)
	private String numProcessoAprovExecPlanoMnjFlor;
	@Column(name = "ind_cort_flor_prod")
	private Boolean indCortFlorProd;
	@Column(name = "ind_pror_valid_asv")
	private Boolean indProrValidAsv;
	@Column(name = "ind_pror_valid_rcfp")
	private Boolean indProrValidRcfp;
	@Column(name = "ind_pror_valid_acfp")
	private Boolean indProrValidAcfp;
	@Column(name = "ind_pror_valid_la")
	private Boolean indProrValidLa;
	@Column(name = "ind_pror_valid_nao")
	private Boolean indProrValidNao;
	@Size(max = 50)
	@Column(name = "num_port_asv", length = 50)
	private String numPortAsv;
	@Size(max = 50)
	@Column(name = "num_port_rcfp", length = 50)
	private String numPortRcfp;
	@Size(max = 50)
	@Column(name = "num_port_acfp", length = 50)
	private String numPortAcfp;
	@Size(max = 50)
	@Column(name = "num_port_la", length = 50)
	private String numPortLa;
	@Size(max = 50)
	@Column(name = "num_port_cert_reg_flor_prod", length = 50)
	private String numPortCertRegFlorProd;
	@Column(name = "ind_flor_vinc_rep_flor")
	private Boolean indFlorVincRepFlor;
	@Size(max = 50)
	@Column(name = "num_port_emiss_cred_rep_flor", length = 50)
	private String numPortEmissCredRepFlor;
	@Column(name = "ind_rec_vol_flor_rem")
	private Boolean indRecVolFlorRem;
	@Column(name = "ind_orig_mat_len_asv")
	private Boolean indOrigMatLenAsv;
	@Column(name = "ind_orig_mat_len_rcfp")
	private Boolean indOrigMatLenRcfp;
	@Column(name = "ind_orig_mat_len_acfp")
	private Boolean indOrigMatLenAcfp;
	@Size(max = 50)
	@Column(name = "num_orig_mat_len_asv", length = 50)
	private String numOrigMatLenAsv;
	@Column(name = "ind_alterar_licenca_ambiental")
	private Boolean indAlterarLicencaAmbiental;
	@Column(name = "num_alterar_licenca_ambiental")
	private String numAlterarLicencaAmbiental;
	@Size(max = 50)
	@Column(name = "num_orig_mat_len_rcfp", length = 50)
	private String numOrigMatLenRcfp;
	@Size(max = 50)
	@Column(name = "num_orig_mat_len_acfp", length = 50)
	private String numOrigMatLenAcfp;
	@JoinTable(name = "requerimento_tipo_finalidade_uso_agua", joinColumns = { @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento_unico", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false) })
	
	@ManyToMany
	private Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection;
	@JoinTable(name = "requerimento_tipo_receptor", joinColumns = { @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento_unico", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_receptor", referencedColumnName = "ide_tipo_receptor", nullable = false) })
	@ManyToMany
	private Collection<TipoReceptor> tipoReceptorCollection;
	@JoinTable(name = "requerimento_tipo_captacao", joinColumns = { @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento_unico", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_captacao", referencedColumnName = "ide_tipo_captacao", nullable = false) })
	@ManyToMany
	private Collection<TipoCaptacao> tipoCaptacaoCollection;
	@JoinTable(name = "requerimento_tipo_intervencao", joinColumns = { @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento_unico", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_intervencao", referencedColumnName = "ide_tipo_intervencao", nullable = false) })
	@ManyToMany
	private Collection<TipoIntervencao> tipoIntervencaoCollection;
	@OneToMany(mappedBy = "ideRequerimentoUnico")
	private Collection<Enquadramento> enquadramento;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento")
	private Collection<RequerimentoTipologia> requerimentoTipologiaCollection;
	@JoinColumn(name = "ide_requerimento_unico", referencedColumnName = "ide_requerimento", nullable = false, insertable = false, updatable = false)
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private Requerimento requerimento;
	@JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", nullable = false)
	@ManyToOne(optional = false)
	private Porte idePorte;
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	@JoinColumn(name = "ide_fase_empreendimento", referencedColumnName = "ide_fase_empreendimento", nullable = false)
	@ManyToOne(optional = false)
	private FaseEmpreendimento ideFaseEmpreendimento;
	@Column(name = "num_proc_lic_ambiental", length = 50)
	private String numProcLicAmbiental;
	@Size(max = 50)
	@Column(name = "num_proc_emiss_cred", length = 50)
	private String numProcEmissCred;
	@Column(name = "ind_dla")
	private Boolean indDla;
	@Column(name = "ind_revovar_licenca_ambiental")
	private Boolean indRevovarLicencaAmbiental;
	@Size(max = 50)
	@Column(name = "num_revovar_licenca_ambiental", length = 50)
	private String numRevovarLicencaAmbiental;
	@Column(name = "ind_ato_ambiental")
	private Boolean indAtoAmbiental;
	@Column(name = "ind_ato_fauna")
	private Boolean indAtoFauna;
	@Column(name = "ind_manejo_fauna")
	private Boolean indManejoFauna;
	@Column(name = "ind_estudo_fauna")
	private Boolean indEstudoFauna;

	@Transient
	private Collection<AtoAmbiental> atosAmbientais;
	@Transient
	private AtoAmbiental atoAmbiental;
	@Transient
	private String dtcPerfPocoString;

	public RequerimentoUnico() {
	}

	public RequerimentoUnico(Requerimento requerimento) {
		this.ideRequerimentoUnico = requerimento.getIdeRequerimento();
		this.requerimento = requerimento;
	}
	
	public RequerimentoUnico(Integer ideRequerimentoUnico) {
		this.ideRequerimentoUnico = ideRequerimentoUnico;
	}

	public RequerimentoUnico(Integer ideRequerimentoUnico, Boolean indTramiteInema, Boolean indTramiteAna, Boolean indSupressaoVegetacao,
			Boolean indPlanoManejo, Boolean indServidaoFlorestal) {
		this.ideRequerimentoUnico = ideRequerimentoUnico;
		this.indTramiteInema = indTramiteInema;
		this.indTramiteAna = indTramiteAna;
		this.indSupressaoVegetacao = indSupressaoVegetacao;
		this.indPlanoManejo = indPlanoManejo;
		this.indServidaoFlorestal = indServidaoFlorestal;
	}

	public Integer getIdeRequerimentoUnico() {
		return ideRequerimentoUnico;
	}
	
	public Boolean getIdeRequerimentoUnicoNotNull() {
		if(Util.isNullOuVazio(ideRequerimentoUnico))
			return false;
		else
			return true;
	}

	public void setIdeRequerimentoUnico(Integer ideRequerimentoUnico) {
		this.ideRequerimentoUnico = ideRequerimentoUnico;
	}

	public Boolean getIndTramiteInema() {
		return indTramiteInema;
	}

	public void setIndTramiteInema(Boolean indTramiteInema) {
		this.indTramiteInema = indTramiteInema;
	}

	public Boolean getIndTramiteAna() {
		return indTramiteAna;
	}

	public void setIndTramiteAna(Boolean indTramiteAna) {
		this.indTramiteAna = indTramiteAna;
	}

	public Boolean getIndSupressaoVegetacao() {
		return indSupressaoVegetacao;
	}

	public void setIndSupressaoVegetacao(Boolean indSupressaoVegetacao) {
		this.indSupressaoVegetacao = indSupressaoVegetacao;
	}

	public Boolean getIndPlanoManejo() {
		return indPlanoManejo;
	}

	public void setIndPlanoManejo(Boolean indPlanoManejo) {
		this.indPlanoManejo = indPlanoManejo;
	}

	public Boolean getIndServidaoFlorestal() {
		return indServidaoFlorestal;
	}

	public void setIndServidaoFlorestal(Boolean indServidaoFlorestal) {
		this.indServidaoFlorestal = indServidaoFlorestal;
	}

	public String getNumOutorgaProtocoloAbertura() {
		return numOutorgaProtocoloAbertura;
	}

	public void setNumOutorgaProtocoloAbertura(String numOutorgaProtocoloAbertura) {
		this.numOutorgaProtocoloAbertura = numOutorgaProtocoloAbertura;
	}

	public Boolean getIndProcOutorgaAtende() {
		return indProcOutorgaAtende;
	}

	public void setIndProcOutorgaAtende(Boolean indProcOutorgaAtende) {
		this.indProcOutorgaAtende = indProcOutorgaAtende;
	}

	public Boolean getIndUtilizaAgua() {
		return indUtilizaAgua;
	}

	public void setIndUtilizaAgua(Boolean indUtilizaAgua) {
		this.indUtilizaAgua = indUtilizaAgua;
	}

	public Boolean getIndRealizaEmissaoResiduosLiquidos() {
		return indRealizaEmissaoResiduosLiquidos;
	}

	public void setIndRealizaEmissaoResiduosLiquidos(Boolean indRealizaEmissaoResiduosLiquidos) {
		this.indRealizaEmissaoResiduosLiquidos = indRealizaEmissaoResiduosLiquidos;
	}

	public Boolean getIndIntervencaoCorpoHidrico() {
		return indIntervencaoCorpoHidrico;
	}

	public void setIndIntervencaoCorpoHidrico(Boolean indIntervencaoCorpoHidrico) {
		this.indIntervencaoCorpoHidrico = indIntervencaoCorpoHidrico;
	}

	public Boolean getIndRegularizarPerfPoco() {
		return indRegularizarPerfPoco;
	}

	public void setIndRegularizarPerfPoco(Boolean indRegularizarPerfPoco) {
		this.indRegularizarPerfPoco = indRegularizarPerfPoco;
	}

	public Boolean getIndPocoRegularizarPerfurado() {
		return indPocoRegularizarPerfurado;
	}

	public void setIndPocoRegularizarPerfurado(Boolean indPocoRegularizarPerfurado) {
		this.indPocoRegularizarPerfurado = indPocoRegularizarPerfurado;
	}

	public Boolean getIndVolumeMaterial() {
		return indVolumeMaterial;
	}

	public void setIndVolumeMaterial(Boolean indVolumeMaterial) {
		this.indVolumeMaterial = indVolumeMaterial;
	}

	public Boolean getIndCaptacaoBarramento() {
		return indCaptacaoBarramento;
	}

	public void setIndCaptacaoBarramento(Boolean indCaptacaoBarramento) {
		this.indCaptacaoBarramento = indCaptacaoBarramento;
	}

	public Boolean getIndPocoCaptacaoPerfurado() {
		return indPocoCaptacaoPerfurado;
	}

	public void setIndPocoCaptacaoPerfurado(Boolean indPocoCaptacaoPerfurado) {
		this.indPocoCaptacaoPerfurado = indPocoCaptacaoPerfurado;
	}

	public Boolean getIndAutorizacaoPerfuracaoPoco() {
		return indAutorizacaoPerfuracaoPoco;
	}

	public void setIndAutorizacaoPerfuracaoPoco(Boolean indAutorizacaoPerfuracaoPoco) {
		this.indAutorizacaoPerfuracaoPoco = indAutorizacaoPerfuracaoPoco;
	}

	public BigDecimal getNumVazaoCaptacao() {
		return numVazaoCaptacao;
	}

	public void setNumVazaoCaptacao(BigDecimal numVazaoCaptacao) {
		this.numVazaoCaptacao = numVazaoCaptacao;
	}

	public Boolean getIndAlterarLicencaAmbiental() {
		return indAlterarLicencaAmbiental;
	}

	public void setIndAlterarLicencaAmbiental(Boolean indAlterarLicencaAmbiental) {
		this.indAlterarLicencaAmbiental = indAlterarLicencaAmbiental;
	}

	public String getNumAlterarLicencaAmbiental() {
		return numAlterarLicencaAmbiental;
	}

	public void setNumAlterarLicencaAmbiental(String numAlterarLicencaAmbiental) {
		this.numAlterarLicencaAmbiental = numAlterarLicencaAmbiental;
	}

	public Date getDtcPerfPoco() {
		return dtcPerfPoco;
	}
	
	public String getDtcPerfPocoString() {
		String data = null;
		if(dtcPerfPoco != null){
			data = new SimpleDateFormat("dd/MM/yyyy").format(this.dtcPerfPoco);
		}
		return data;
	}

	public void setDtcPerfPoco(Date dtcPerfPoco) {
		this.dtcPerfPoco = dtcPerfPoco;
	}

	public String getNumProcessoAutorizacaoPoco() {
		return numProcessoAutorizacaoPoco;
	}

	public void setNumProcessoAutorizacaoPoco(String numProcessoAutorizacaoPoco) {
		this.numProcessoAutorizacaoPoco = numProcessoAutorizacaoPoco;
	}

	public String getDscOutrosobjtLimpArea() {
		return dscOutrosobjtLimpArea;
	}

	public void setDscOutrosobjtLimpArea(String dscOutrosobjtLimpArea) {
		this.dscOutrosobjtLimpArea = dscOutrosobjtLimpArea;
	}

	public Date getDtcInicioLimpArea() {
		return dtcInicioLimpArea;
	}

	public void setDtcInicioLimpArea(Date dtcInicioLimpArea) {
		this.dtcInicioLimpArea = dtcInicioLimpArea;
	}

	public Date getDtcFinalLimpArea() {
		return dtcFinalLimpArea;
	}

	public void setDtcFinalLimpArea(Date dtcFinalLimpArea) {
		this.dtcFinalLimpArea = dtcFinalLimpArea;
	}

	public Boolean getIndBarragemCaptConstruida() {
		return indBarragemCaptConstruida;
	}

	public void setIndBarragemCaptConstruida(Boolean indBarragemCaptConstruida) {
		this.indBarragemCaptConstruida = indBarragemCaptConstruida;
	}

	public Boolean getIndAutorizacaoConstrucaoBarragemCapt() {
		return indAutorizacaoConstrucaoBarragemCapt;
	}

	public void setIndAutorizacaoConstrucaoBarragemCapt(Boolean indAutorizacaoConstrucaoBarragemCapt) {
		this.indAutorizacaoConstrucaoBarragemCapt = indAutorizacaoConstrucaoBarragemCapt;
	}

	public Boolean getIndBarragemIntervConstruida() {
		return indBarragemIntervConstruida;
	}

	public void setIndBarragemIntervConstruida(Boolean indBarragemIntervConstruida) {
		this.indBarragemIntervConstruida = indBarragemIntervConstruida;
	}

	public Boolean getIndAutorizacaoConstrucaoBarragemInterv() {
		return indAutorizacaoConstrucaoBarragemInterv;
	}

	public void setIndAutorizacaoConstrucaoBarragemInterv(Boolean indAutorizacaoConstrucaoBarragemInterv) {
		this.indAutorizacaoConstrucaoBarragemInterv = indAutorizacaoConstrucaoBarragemInterv;
	}

	public Boolean getIndRealizarQueimaControlada() {
		return indRealizarQueimaControlada;
	}

	public void setIndRealizarQueimaControlada(Boolean indRealizarQueimaControlada) {
		this.indRealizarQueimaControlada = indRealizarQueimaControlada;
	}

	public Boolean getIndUtilMatLenhosoArvrMortas() {
		return indUtilMatLenhosoArvrMortas;
	}

	public void setIndUtilMatLenhosoArvrMortas(Boolean indUtilMatLenhosoArvrMortas) {
		this.indUtilMatLenhosoArvrMortas = indUtilMatLenhosoArvrMortas;
	}

	public Boolean getIndDeclaracaoPlanoSuprSust() {
		return indDeclaracaoPlanoSuprSust;
	}

	public void setIndDeclaracaoPlanoSuprSust(Boolean indDeclaracaoPlanoSuprSust) {
		this.indDeclaracaoPlanoSuprSust = indDeclaracaoPlanoSuprSust;
	}

	public Boolean getIndVincFlorProdRepFlor() {
		return indVincFlorProdRepFlor;
	}

	public void setIndVincFlorProdRepFlor(Boolean indVincFlorProdRepFlor) {
		this.indVincFlorProdRepFlor = indVincFlorProdRepFlor;
	}

	public String getNumPortariaLicSilvicultura() {
		return numPortariaLicSilvicultura;
	}

	public void setNumPortariaLicSilvicultura(String numPortariaLicSilvicultura) {
		this.numPortariaLicSilvicultura = numPortariaLicSilvicultura;
	}

	public Boolean getIndTransfCredRepFlorestal() {
		return indTransfCredRepFlorestal;
	}

	public void setIndTransfCredRepFlorestal(Boolean indTransfCredRepFlorestal) {
		this.indTransfCredRepFlorestal = indTransfCredRepFlorestal;
	}

	public Boolean getIndAprovExecPlanoMnjFlor() {
		return indAprovExecPlanoMnjFlor;
	}

	public void setIndAprovExecPlanoMnjFlor(Boolean indAprovExecPlanoMnjFlor) {
		this.indAprovExecPlanoMnjFlor = indAprovExecPlanoMnjFlor;
	}

	public String getNumProcessoAprovExecPlanoMnjFlor() {
		return numProcessoAprovExecPlanoMnjFlor;
	}

	public void setNumProcessoAprovExecPlanoMnjFlor(String numProcessoAprovExecPlanoMnjFlor) {
		this.numProcessoAprovExecPlanoMnjFlor = numProcessoAprovExecPlanoMnjFlor;
	}

	public Boolean getIndCortFlorProd() {
		return indCortFlorProd;
	}

	public void setIndCortFlorProd(Boolean indCortFlorProd) {
		this.indCortFlorProd = indCortFlorProd;
	}

	public Boolean getIndProrValidAsv() {
		return indProrValidAsv;
	}

	public void setIndProrValidAsv(Boolean indProrValidAsv) {
		this.indProrValidAsv = indProrValidAsv;
	}

	public Boolean getIndProrValidRcfp() {
		return indProrValidRcfp;
	}

	public void setIndProrValidRcfp(Boolean indProrValidRcfp) {
		this.indProrValidRcfp = indProrValidRcfp;
	}

	public Boolean getIndProrValidAcfp() {
		return indProrValidAcfp;
	}

	public void setIndProrValidAcfp(Boolean indProrValidAcfp) {
		this.indProrValidAcfp = indProrValidAcfp;
	}

	public Boolean getIndProrValidLa() {
		return indProrValidLa;
	}

	public void setIndProrValidLa(Boolean indProrValidLa) {
		this.indProrValidLa = indProrValidLa;
	}

	public Boolean getIndProrValidNao() {
		return indProrValidNao;
	}

	public void setIndProrValidNao(Boolean indProrValidNao) {
		this.indProrValidNao = indProrValidNao;
	}

	public String getNumPortAsv() {
		return numPortAsv;
	}

	public void setNumPortAsv(String numPortAsv) {
		this.numPortAsv = numPortAsv;
	}

	public String getNumPortRcfp() {
		return numPortRcfp;
	}

	public void setNumPortRcfp(String numPortRcfp) {
		this.numPortRcfp = numPortRcfp;
	}

	public String getNumPortAcfp() {
		return numPortAcfp;
	}

	public void setNumPortAcfp(String numPortAcfp) {
		this.numPortAcfp = numPortAcfp;
	}

	public String getNumPortLa() {
		return numPortLa;
	}

	public void setNumPortLa(String numPortLa) {
		this.numPortLa = numPortLa;
	}

	public String getNumPortCertRegFlorProd() {
		return numPortCertRegFlorProd;
	}

	public void setNumPortCertRegFlorProd(String numPortCertRegFlorProd) {
		this.numPortCertRegFlorProd = numPortCertRegFlorProd;
	}

	public Boolean getIndFlorVincRepFlor() {
		return indFlorVincRepFlor;
	}

	public void setIndFlorVincRepFlor(Boolean indFlorVincRepFlor) {
		this.indFlorVincRepFlor = indFlorVincRepFlor;
	}

	public String getNumPortEmissCredRepFlor() {
		return numPortEmissCredRepFlor;
	}

	public void setNumPortEmissCredRepFlor(String numPortEmissCredRepFlor) {
		this.numPortEmissCredRepFlor = numPortEmissCredRepFlor;
	}

	public Boolean getIndRecVolFlorRem() {
		return indRecVolFlorRem;
	}

	public void setIndRecVolFlorRem(Boolean indRecVolFlorRem) {
		this.indRecVolFlorRem = indRecVolFlorRem;
	}

	public Boolean getIndOrigMatLenAsv() {
		return indOrigMatLenAsv;
	}

	public void setIndOrigMatLenAsv(Boolean indOrigMatLenAsv) {
		this.indOrigMatLenAsv = indOrigMatLenAsv;
	}

	public Boolean getIndOrigMatLenRcfp() {
		return indOrigMatLenRcfp;
	}

	public void setIndOrigMatLenRcfp(Boolean indOrigMatLenRcfp) {
		this.indOrigMatLenRcfp = indOrigMatLenRcfp;
	}

	public Boolean getIndOrigMatLenAcfp() {
		return indOrigMatLenAcfp;
	}

	public void setIndOrigMatLenAcfp(Boolean indOrigMatLenAcfp) {
		this.indOrigMatLenAcfp = indOrigMatLenAcfp;
	}

	public String getNumOrigMatLenAsv() {
		return numOrigMatLenAsv;
	}

	public void setNumOrigMatLenAsv(String numOrigMatLenAsv) {
		this.numOrigMatLenAsv = numOrigMatLenAsv;
	}

	public String getNumOrigMatLenRcfp() {
		return numOrigMatLenRcfp;
	}

	public void setNumOrigMatLenRcfp(String numOrigMatLenRcfp) {
		this.numOrigMatLenRcfp = numOrigMatLenRcfp;
	}

	public String getNumOrigMatLenAcfp() {
		return numOrigMatLenAcfp;
	}

	public void setNumOrigMatLenAcfp(String numOrigMatLenAcfp) {
		this.numOrigMatLenAcfp = numOrigMatLenAcfp;
	}

	@XmlTransient
	public Collection<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaCollection() {
		return tipoFinalidadeUsoAguaCollection;
	}

	public void setTipoFinalidadeUsoAguaCollection(Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection) {
		this.tipoFinalidadeUsoAguaCollection = tipoFinalidadeUsoAguaCollection;
	}

	@XmlTransient
	public Collection<TipoReceptor> getTipoReceptorCollection() {
		return tipoReceptorCollection;
	}

	public void setTipoReceptorCollection(Collection<TipoReceptor> tipoReceptorCollection) {
		this.tipoReceptorCollection = tipoReceptorCollection;
	}

	@XmlTransient
	public Collection<TipoCaptacao> getTipoCaptacaoCollection() {
		return tipoCaptacaoCollection;
	}

	public void setTipoCaptacaoCollection(Collection<TipoCaptacao> tipoCaptacaoCollection) {
		this.tipoCaptacaoCollection = tipoCaptacaoCollection;
	}

	@XmlTransient
	public Collection<TipoIntervencao> getTipoIntervencaoCollection() {
		return tipoIntervencaoCollection;
	}

	public void setTipoIntervencaoCollection(Collection<TipoIntervencao> tipoIntervencaoCollection) {
		this.tipoIntervencaoCollection = tipoIntervencaoCollection;
	}

	public Collection<Enquadramento> getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(Collection<Enquadramento> enquadramento) {
		this.enquadramento = enquadramento;
	}

	@XmlTransient
	public Collection<RequerimentoTipologia> getRequerimentoTipologiaCollection() {
		return requerimentoTipologiaCollection;
	}

	public void setRequerimentoTipologiaCollection(Collection<RequerimentoTipologia> requerimentoTipologiaCollection) {
		this.requerimentoTipologiaCollection = requerimentoTipologiaCollection;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Porte getIdePorte() {
		return idePorte;
	}

	public void setIdePorte(Porte idePorte) {
		this.idePorte = idePorte;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public FaseEmpreendimento getIdeFaseEmpreendimento() {
		return ideFaseEmpreendimento;
	}

	public void setIdeFaseEmpreendimento(FaseEmpreendimento ideFaseEmpreendimento) {
		this.ideFaseEmpreendimento = ideFaseEmpreendimento;
	}

	public String getNumProcLicAmbiental() {
		return numProcLicAmbiental;
	}

	public void setNumProcLicAmbiental(String numProcLicAmbiental) {
		this.numProcLicAmbiental = numProcLicAmbiental;
	}

	public String getNumProcEmissCred() {
		return numProcEmissCred;
	}

	public void setNumProcEmissCred(String numProcEmissCred) {
		this.numProcEmissCred = numProcEmissCred;
	}

	public Boolean getIndDla() {
		return indDla;
	}

	public void setIndDla(Boolean indDla) {
		this.indDla = indDla;
	}

	public Boolean getIndRevovarLicencaAmbiental() {
		return indRevovarLicencaAmbiental;
	}

	public void setIndRevovarLicencaAmbiental(Boolean indRevovarLicencaAmbiental) {
		this.indRevovarLicencaAmbiental = indRevovarLicencaAmbiental;
	}

	public String getNumRevovarLicencaAmbiental() {
		return numRevovarLicencaAmbiental;
	}

	public void setNumRevovarLicencaAmbiental(String numRevovarLicencaAmbiental) {
		this.numRevovarLicencaAmbiental = numRevovarLicencaAmbiental;
	}

	public Boolean getIndAtoAmbiental() {
		return indAtoAmbiental;
	}

	public void setIndAtoAmbiental(Boolean indAtoAmbiental) {
		this.indAtoAmbiental = indAtoAmbiental;
	}

	public Boolean getIndAtoFauna() {
		return indAtoFauna;
	}

	public void setIndAtoFauna(Boolean indAtoFauna) {
		this.indAtoFauna = indAtoFauna;
	}

	public Boolean getIndManejoFauna() {
		return indManejoFauna;
	}

	public void setIndManejoFauna(Boolean indManejoFauna) {
		this.indManejoFauna = indManejoFauna;
	}

	public Boolean getIndEstudoFauna() {
		return indEstudoFauna;
	}

	public void setIndEstudoFauna(Boolean indEstudoFauna) {
		this.indEstudoFauna = indEstudoFauna;
	}

	public Collection<AtoAmbiental> getAtosAmbientais() {
		return Util.isNull(atosAmbientais) ? atosAmbientais = new ArrayList<AtoAmbiental>(): atosAmbientais ;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public void setDtcPerfPocoString(String dtcPerfPocoString) {
		this.dtcPerfPocoString = dtcPerfPocoString;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideRequerimentoUnico != null ? ideRequerimentoUnico.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof RequerimentoUnico)) {
			return false;
		}
		RequerimentoUnico other = (RequerimentoUnico) object;
		if ((this.ideRequerimentoUnico == null && other.ideRequerimentoUnico != null)
				|| (this.ideRequerimentoUnico != null && !this.ideRequerimentoUnico.equals(other.ideRequerimentoUnico))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return  ideRequerimentoUnico.toString() ;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Boolean getIndRelocServidFlorest() {
		return indRelocServidFlorest;
	}

	public void setIndRelocServidFlorest(Boolean indRelocServidFlorest) {
		this.indRelocServidFlorest = indRelocServidFlorest;
	}

}


create sequence tipo_especie_armazem_seq;

CREATE TABLE tipo_especie_armazem (
                ide_tipo_especie_armazem INTEGER NOT NULL Default nextVal('tipo_especie_armazem_seq'),
                nom_tipo_especie_armazem VARCHAR(20) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_tipo_especie_armazem PRIMARY KEY (ide_tipo_especie_armazem)
);


create sequence silos_armazens_sistema_seguranca_seq;
CREATE TABLE silos_armazens_sistema_seguranca (
                ide_silos_armazens_sistema_seguranca INTEGER NOT NULL Default nextVal('silos_armazens_sistema_seguranca_seq'),
                nom_sistema_segurana VARCHAR(100) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_silos_armazens_sistema_seguranca PRIMARY KEY (ide_silos_armazens_sistema_seguranca)
);
COMMENT ON COLUMN silos_armazens_sistema_seguranca.ind_ativo IS 'Sim-true
Nao-False';

create sequence destinacao_final_seq;
CREATE TABLE destinacao_final (
                ide_destinacao_final INTEGER NOT NULL Default nextVal('destinacao_final_seq'),
                ind_ativo BOOLEAN NOT NULL,
                nom_destinacao_final VARCHAR(30) NOT NULL,
                CONSTRAINT ide_destinacao_final PRIMARY KEY (ide_destinacao_final)
);
COMMENT ON COLUMN destinacao_final.ind_ativo IS 'Sim-true
Nao-False';


create sequence classificacao_residuo_seq;
CREATE TABLE classificacao_residuo (
                ide_classificacao_residuo INTEGER NOT NULL Default nextVal('classificacao_residuo_seq'),
                ind_ativo BOOLEAN NOT NULL,
                nom_classificacao_residuo VARCHAR(30) NOT NULL,
                CONSTRAINT ide_classificacao_residuo PRIMARY KEY (ide_classificacao_residuo)
);
COMMENT ON COLUMN classificacao_residuo.ind_ativo IS 'Sim-true
Nao - False';

create sequence medida_controle_emissao_seq;
CREATE TABLE medida_controle_emissao (
                ide_medida_controle_emissao INTEGER NOT NULL Default nextVal('medida_controle_emissao_seq'),
                nom_medida_controle_emissao VARCHAR(200) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_medida_controle_emissao PRIMARY KEY (ide_medida_controle_emissao)
);

create sequence equipamento_controle_seq;
CREATE TABLE equipamento_controle (
                ide_equipamento_controle INTEGER NOT NULL Default nextVal('equipamento_controle_seq'),
                nom_equipamento_controle VARCHAR(50) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_equipamento_controle PRIMARY KEY (ide_equipamento_controle)
);

create sequence silos_armazens_caracterizacao_atmosferica_seq;
CREATE TABLE silos_armazens_caracterizacao_atmosferica (
                ide_silos_armazens_caracterizacao_atmosferica INTEGER NOT NULL Default nextVal('silos_armazens_caracterizacao_atmosferica_seq'),
                nom_caracterizacao_atmosferica VARCHAR(100) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_silos_armazens_caracterizacao_atmosferica PRIMARY KEY (ide_silos_armazens_caracterizacao_atmosferica)
);

create sequence sistema_tratamento_agua_seq;
CREATE TABLE sistema_tratamento_agua (
                ide_sistema_tratamento_agua INTEGER NOT NULL Default nextVal('sistema_tratamento_agua_seq'),
                nom_sistema_tratamento_agua VARCHAR(60) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_sistema_tratamento_agua PRIMARY KEY (ide_sistema_tratamento_agua)
);
COMMENT ON COLUMN sistema_tratamento_agua.ind_ativo IS 'O sistema s� deve exibir os sistemas de tratamento de �gua ativo.';

create sequence tipo_concessionaria_seq;
CREATE TABLE tipo_concessionaria (
                ide_tipo_concessionaria INTEGER NOT NULL Default nextVal('tipo_concessionaria_seq'),
                nom_concessionarias VARCHAR(15) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_tipo_concessionaria PRIMARY KEY (ide_tipo_concessionaria)
);
COMMENT ON COLUMN tipo_concessionaria.ind_ativo IS 'Sistema vai exibir apenas os ativos.
Sim=true
Nao=False';

create sequence silos_armazens_origem_agua_seq;
CREATE TABLE silos_armazens_origem_agua (
                ide_silos_armazens_origem_agua INTEGER NOT NULL Default nextVal('silos_armazens_origem_agua_seq'),
                nom_origem_agua VARCHAR(30) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_silos_armazens_origem_agua PRIMARY KEY (ide_silos_armazens_origem_agua)
);

create sequence tipo_armazem_seq;
CREATE TABLE tipo_armazem (
                ide_tipo_armazem INTEGER NOT NULL Default nextVal('tipo_armazem_seq'),
                nom_tipo_armazem VARCHAR(20) NOT NULL,
                ide_tipo_especie_armazem INTEGER NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_tipo_armazem PRIMARY KEY (ide_tipo_armazem)
);

create sequence tipo_madeira_seq;
CREATE TABLE tipo_madeira (
                ide_tipo_madeira INTEGER NOT NULL Default nextVal('tipo_madeira_seq'),
                nom_tipo_madeira VARCHAR(30) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT ide_tipo_madeira PRIMARY KEY (ide_tipo_madeira)
);

create sequence tipo_combustivel_silo_armazens_seq;
CREATE TABLE tipo_combustivel_silo_armazens (
                ide_tipo_combustivel_silo_armazens INTEGER NOT NULL Default nextVal('tipo_combustivel_silo_armazens_seq'),
                nom_tipo_combustivel VARCHAR(30) NOT NULL,
                ide_tipo_madeira INTEGER,
		ide_tipo_combustivel_silo_armazens_pai Integer,	
                CONSTRAINT ide_tipo_combustivel_silo_armazens PRIMARY KEY (ide_tipo_combustivel_silo_armazens)
);

create sequence operacao_desenvolvida_silos_armazens_seq;
CREATE TABLE operacao_desenvolvida_silos_armazens (
                ide_operacao_desenvolvida_silos_armazens INTEGER NOT NULL Default nextVal('operacao_desenvolvida_silos_armazens_seq'),
                nom_operacao_desevolvida VARCHAR(50) NOT NULL,
                dtc_exclusao Timestamp ,
                ind_excluido Boolean NOT NULL,
                ide_operacao_desenvolvida_pai INTEGER,
                CONSTRAINT ide_operacao_desenvolvida_silos_armazens PRIMARY KEY (ide_operacao_desenvolvida_silos_armazens)
);



create sequence silos_armazens_seq;
CREATE TABLE silos_armazens (
                ide_silos_armazens INTEGER NOT NULL Default nextVal('silos_armazens_seq'),
                ind_industrializacao BOOLEAN NOT NULL,
                ind_existe_comunidade BOOLEAN NOT NULL,
                ind_empreendimento_caldeira BOOLEAN NOT NULL,
                val_area_total_terreno NUMERIC(10,2) NOT NULL,
                ind_queima_combustivel BOOLEAN NOT NULL,
                ind_aceite_declaracao_final BOOLEAN NOT NULL,
                ind_aceite_instrucoes BOOLEAN NOT NULL,
                val_area_total_construida NUMERIC(10,2) NOT NULL,
                ind_aceite_empreendimento_area_protegida BOOLEAN NOT NULL,
                ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL,
                ide_empreendimento INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens PRIMARY KEY (ide_silos_armazens)
);
COMMENT ON COLUMN silos_armazens.ind_industrializacao IS 'No in�cio do cadastro tem a tela de orieta��es com o aceite: "Declaro que o empreendimento n�o � realizado processo de industrializa��o.":
Sim=true
N�o= False';
COMMENT ON COLUMN silos_armazens.ind_existe_comunidade IS '* Existe alguma comunidade num raio de 1 km do empreendimento? 
Sim=true
False=N�o';
COMMENT ON COLUMN silos_armazens.ind_empreendimento_caldeira IS 'No empreendimento � utilizada caldeira?
Sim=True
N�o= False';
COMMENT ON COLUMN silos_armazens.ind_queima_combustivel IS '*No empreendimento � realizada a queima de combust�veis para secagem?
Sim= true
N�o = False';
COMMENT ON COLUMN silos_armazens.ind_aceite_declaracao_final IS 'No final do cadastro o usu�rio precisa concordar com uma declara��o de veracidade das informa��es preenchidas.
Sim- True
N�o - False';
COMMENT ON COLUMN silos_armazens.ind_aceite_instrucoes IS 'No in�cio do cadastro tem um tela de aceite:
Declaro que estou ciente quanto �s instru��es acima.
SIM= TRUE
N�O= FALSE';
COMMENT ON COLUMN silos_armazens.val_area_total_construida IS 'Esse valor n�o pode ser maior que o campo val_area_total_terreno';
COMMENT ON COLUMN silos_armazens.ind_aceite_empreendimento_area_protegida IS 'No in�cio do cadastro tem um tela de aceite:
"Declaro que o empreendimento n�o est� localizado em �reas protegidas, como as citadas nas instru��es acima."
Sim=true
Nao= False';

create sequence caracterizacao_ambiental_silos_armazens_seq;
CREATE TABLE caracterizacao_ambiental_silos_armazens (
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_silos_armazens_seq'),
                ind_utiliza_agua BOOLEAN NOT NULL,
                ind_medida_controle_emissao BOOLEAN NOT NULL,
                ind_sistema_tratamento BOOLEAN NOT NULL,
                ind_lanca_efluente BOOLEAN NOT NULL,
                ide_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_caracterizacao_ambiental_silos_armazens PRIMARY KEY (ide_caracterizacao_ambiental_silos_armazens)
);
COMMENT ON COLUMN caracterizacao_ambiental_silos_armazens.ind_utiliza_agua IS '* A atividade utiliza ou vai utilizar agua? 
Sim= true
Nao = False';
COMMENT ON COLUMN caracterizacao_ambiental_silos_armazens.ind_medida_controle_emissao IS 'Possui medidas de controle de emissoes?
Sim- true
Nao- False';
COMMENT ON COLUMN caracterizacao_ambiental_silos_armazens.ind_sistema_tratamento IS 'Se o empreendimento tem sistema de tratamento de agua Sim= True';
COMMENT ON COLUMN caracterizacao_ambiental_silos_armazens.ind_lanca_efluente IS 'Se o empreendimento lanca efluente  no corpo hidrico Sim = True';

create sequence caracterizacao_ambiental_destinacao_final_seq;
CREATE TABLE caracterizacao_ambiental_destinacao_final (
                ide_caracterizacao_ambiental_destinacao_final INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_destinacao_final_seq'),
                ide_destinacao_final INTEGER NOT NULL,
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_caracterizacao_ambiental_destinacao_final PRIMARY KEY (ide_caracterizacao_ambiental_destinacao_final)
);

create sequence caracterizacao_ambiental_caracterizacao_atmosferica_seq;
CREATE TABLE caracterizacao_ambiental_caracterizacao_atmosferica (
                ide_caracterizacao_ambiental_caracterizacao_atmosferica INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_caracterizacao_atmosferica_seq'),
                ide_silos_armazens_caracterizacao_atmosferica INTEGER NOT NULL,
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_caracterizacao_ambiental_caracterizacao_atmosferica PRIMARY KEY (ide_caracterizacao_ambiental_caracterizacao_atmosferica)
);


create sequence silos_armazens_sistema_tratamento_agua_seq;
CREATE TABLE silos_armazens_sistema_tratamento_agua (
                ide_silos_armazens_sistema_tratamento_agua INTEGER NOT NULL Default nextVal('silos_armazens_sistema_tratamento_agua_seq'),
                ide_sistema_tratamento_agua INTEGER NOT NULL,
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_sistema_tratamento_agua PRIMARY KEY (ide_silos_armazens_sistema_tratamento_agua)
);

create sequence silos_armazens_lancamento_efluente_seq;
CREATE TABLE silos_armazens_lancamento_efluente (
                ide_silos_armazens_lancamento_efluente INTEGER NOT NULL Default nextVal('silos_armazens_lancamento_efluente_seq'),
                num_documento_dispensa VARCHAR(30),
                num_documento_portaria VARCHAR(30),
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_lancamento_efluente PRIMARY KEY (ide_silos_armazens_lancamento_efluente)
);

create sequence caracterizacao_ambiental_medida_controle_emissao_seq;
CREATE TABLE caracterizacao_ambiental_medida_controle_emissao (
                ide_caracterizacao_ambiental_medida_controle_emissao INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_medida_controle_emissao_seq'),
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                ide_medida_controle_emissao INTEGER NOT NULL,
                CONSTRAINT ide_caracterizacao_ambiental_medida_controle_emissao PRIMARY KEY (ide_caracterizacao_ambiental_medida_controle_emissao)
);

create sequence classificacao_residuo_caracterizacao_ambiental_seq;
CREATE TABLE classificacao_residuo_caracterizacao_ambiental (
                ide_classificacao_residuo_caracterizacao_ambiental INTEGER NOT NULL Default nextVal('classificacao_residuo_caracterizacao_ambiental_seq'),
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                ide_classificacao_residuo INTEGER NOT NULL,
                CONSTRAINT ide_classificacao_residuo_caracterizacao_ambiental PRIMARY KEY (ide_classificacao_residuo_caracterizacao_ambiental)
);

create sequence caracterizacao_ambiental_equipamento_controle_seq;
CREATE TABLE caracterizacao_ambiental_equipamento_controle (
                ide_caracterizacao_ambiental_equipamento_controle INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_equipamento_controle_seq'),
                ide_equipamento_controle INTEGER NOT NULL,
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_caracterizacao_ambiental_equipamento_controle PRIMARY KEY (ide_caracterizacao_ambiental_equipamento_controle)
);

create sequence caracterizacao_ambiental_origem_agua_seq;
CREATE TABLE caracterizacao_ambiental_origem_agua (
                ide_caracterizacao_ambiental_origem_agua INTEGER NOT NULL Default nextVal('caracterizacao_ambiental_origem_agua_seq'),
                ide_caracterizacao_ambiental_silos_armazens INTEGER NOT NULL,
                ide_silos_armazens_origem_agua INTEGER NOT NULL,
                num_documento_dispensa VARCHAR(30),
                num_documento_portaria VARCHAR(30),
                CONSTRAINT ide_caracterizacao_ambiental_origem_agua PRIMARY KEY (ide_caracterizacao_ambiental_origem_agua)
);

create sequence origem_agua_tipo_concessionaria_seq;
CREATE TABLE origem_agua_tipo_concessionaria (
                ide_origem_agua_tipo_concessionaria INTEGER NOT NULL Default nextVal('origem_agua_tipo_concessionaria_seq'),
                ide_tipo_concessionaria INTEGER NOT NULL,
                ide_caracterizacao_ambiental_origem_agua INTEGER NOT NULL,
                CONSTRAINT ide_origem_agua_tipo_concessionaria PRIMARY KEY (ide_origem_agua_tipo_concessionaria)
);


create sequence silos_armazens_unidade_armazenadora_seq;
CREATE TABLE silos_armazens_unidade_armazenadora (
                ide_silos_armazens_unidade_armazenadora INTEGER NOT NULL Default nextVal('silos_armazens_unidade_armazenadora_seq'),
                nom_unidade_armazenadora VARCHAR(50) NOT NULL,
                cod_cda VARCHAR(30) NOT NULL,
                val_capacidade_estatica NUMERIC(10,2) NOT NULL,
                ide_tipo_armazem INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER NOT NULL,
                ide_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_unidade_armazenadora PRIMARY KEY (ide_silos_armazens_unidade_armazenadora)
);
COMMENT ON COLUMN silos_armazens_unidade_armazenadora.nom_unidade_armazenadora IS 'nom_unidade_armazenadora= campo Idenficacao da unidade armazenadora na tela.';

create sequence sistema_seguranca_silos_armazens_seq;
CREATE TABLE sistema_seguranca_silos_armazens (
                ide_sistema_seguranca_silos_armazens INTEGER NOT NULL Default nextVal('sistema_seguranca_silos_armazens_seq'),
                ide_silos_armazens_sistema_seguranca INTEGER NOT NULL,
                ide_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_sistema_seguranca_silos_armazens PRIMARY KEY (ide_sistema_seguranca_silos_armazens)
);

create sequence silos_armazens_tipo_combustivel_seq;
CREATE TABLE silos_armazens_tipo_combustivel (
                ide_silos_armazens_tipo_combustivel INTEGER NOT NULL Default nextVal('silos_armazens_tipo_combustivel_seq'),
                ide_tipo_combustivel_silo_armazens INTEGER NOT NULL,
                num_raf VARCHAR(30),
                ide_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_tipo_combustivel PRIMARY KEY (ide_silos_armazens_tipo_combustivel)
);


create sequence silos_armazens_operacao_desenvolvida_seq;
CREATE TABLE silos_armazens_operacao_desenvolvida (
                ide_silos_armazens_operacao_desenvolvida INTEGER NOT NULL Default nextVal('silos_armazens_operacao_desenvolvida_seq'),
                ide_silos_armazens INTEGER NOT NULL,
                ide_operacao_desenvolvida_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_operacao_desenvolvida PRIMARY KEY (ide_silos_armazens_operacao_desenvolvida)
);


create sequence silos_armazens_imovel_seq;
CREATE TABLE silos_armazens_imovel (
                ide_silos_armazens_imovel INTEGER NOT NULL Default nextVal('silos_armazens_imovel_seq'),
                ide_imovel INTEGER NOT NULL,
                ind_num_car BOOLEAN NOT NULL,
                ide_silos_armazens INTEGER NOT NULL,
                CONSTRAINT ide_silos_armazens_imovel PRIMARY KEY (ide_silos_armazens_imovel)
);
COMMENT ON COLUMN silos_armazens_imovel.ind_num_car IS 'Sera True quando o imovel for vinculado atraves do numero CAR';


create sequence silos_armazens_responsavel_tecnico_seq;
CREATE TABLE silos_armazens_responsavel_tecnico (
                ide_silos_armazens_responsavel_tecnico INTEGER NOT NULL Default nextVal('silos_armazens_responsavel_tecnico_seq'),
                ide_pessoa_fisica INTEGER NOT NULL,
                ide_silos_armazens INTEGER NOT NULL,
                dtc_criacao DATE NOT NULL,
                CONSTRAINT ide_silos_armazens_responsavel_tecnico PRIMARY KEY (ide_silos_armazens_responsavel_tecnico)
);


ALTER TABLE tipo_armazem ADD CONSTRAINT tipo_especie_armazem_tipo_armazem_fk
FOREIGN KEY (ide_tipo_especie_armazem)
REFERENCES tipo_especie_armazem (ide_tipo_especie_armazem);

ALTER TABLE silos_armazens_responsavel_tecnico ADD CONSTRAINT pessoa_fisica_pessoa_fisica_cad_silos_armazens_fk
FOREIGN KEY (ide_pessoa_fisica)
REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE sistema_seguranca_silos_armazens ADD CONSTRAINT sistema_seguranca_cad_silos_armazens_sistema_seguranca_fk
FOREIGN KEY (ide_silos_armazens_sistema_seguranca)
REFERENCES silos_armazens_sistema_seguranca (ide_silos_armazens_sistema_seguranca);

ALTER TABLE caracterizacao_ambiental_destinacao_final ADD CONSTRAINT destinacao_final_caracterizacao_ambiental_destinacao_final_fk
FOREIGN KEY (ide_destinacao_final)
REFERENCES destinacao_final (ide_destinacao_final);

ALTER TABLE classificacao_residuo_caracterizacao_ambiental ADD CONSTRAINT classificacao_residuo_classificacao_residuo_caracterizacao_ambiental_fk
FOREIGN KEY (ide_classificacao_residuo)
REFERENCES classificacao_residuo (ide_classificacao_residuo);

ALTER TABLE caracterizacao_ambiental_medida_controle_emissao ADD CONSTRAINT medida_controle_emissao_caracterizacao_ambiental_medida_controle_emissao_fk
FOREIGN KEY (ide_medida_controle_emissao)
REFERENCES medida_controle_emissao (ide_medida_controle_emissao);

ALTER TABLE caracterizacao_ambiental_equipamento_controle ADD CONSTRAINT equipamento_controle_equipamento_controle_caracterizacao_ambiental_fk
FOREIGN KEY (ide_equipamento_controle)
REFERENCES equipamento_controle (ide_equipamento_controle);

ALTER TABLE caracterizacao_ambiental_caracterizacao_atmosferica ADD CONSTRAINT silos_armazens_caracterizacao_atmosferica_caracterizacao_ambiental_caracterizacao_atmosferica_fk
FOREIGN KEY (ide_silos_armazens_caracterizacao_atmosferica)
REFERENCES silos_armazens_caracterizacao_atmosferica (ide_silos_armazens_caracterizacao_atmosferica);

ALTER TABLE silos_armazens_sistema_tratamento_agua ADD CONSTRAINT sistema_tratamento_agua_silos_armazens_sistema_tratamento_agua_fk
FOREIGN KEY (ide_sistema_tratamento_agua)
REFERENCES sistema_tratamento_agua (ide_sistema_tratamento_agua);

ALTER TABLE origem_agua_tipo_concessionaria ADD CONSTRAINT tipo_concessionaria_origem_agua_tipo_concessionaria_fk
FOREIGN KEY (ide_tipo_concessionaria)
REFERENCES tipo_concessionaria (ide_tipo_concessionaria);

ALTER TABLE caracterizacao_ambiental_origem_agua ADD CONSTRAINT origem_agua_caracterizacao_ambiental_origem_agua_fk
FOREIGN KEY (ide_silos_armazens_origem_agua)
REFERENCES silos_armazens_origem_agua (ide_silos_armazens_origem_agua);

ALTER TABLE silos_armazens_unidade_armazenadora ADD CONSTRAINT tipo_armazem_cad_unidade_armazenadora_silos_armazens_fk
FOREIGN KEY (ide_tipo_armazem)
REFERENCES tipo_armazem (ide_tipo_armazem);

ALTER TABLE tipo_combustivel_silo_armazens ADD CONSTRAINT tipo_madeira_tipo_combustivel_fk
FOREIGN KEY (ide_tipo_madeira)
REFERENCES tipo_madeira (ide_tipo_madeira);

ALTER TABLE silos_armazens_tipo_combustivel ADD CONSTRAINT tipo_combustivel_cad_silos_armazens_tipo_combustivel_fk
FOREIGN KEY (ide_tipo_combustivel_silo_armazens)
REFERENCES tipo_combustivel_silo_armazens (ide_tipo_combustivel_silo_armazens);

ALTER TABLE silos_armazens_operacao_desenvolvida ADD CONSTRAINT operacao_desenvolvida_cad_silos_armazens_operacao_desenvolvida_fk
FOREIGN KEY (ide_operacao_desenvolvida_silos_armazens)
REFERENCES operacao_desenvolvida_silos_armazens (ide_operacao_desenvolvida_silos_armazens);

ALTER TABLE silos_armazens_imovel ADD CONSTRAINT imovel_cad_silos_armazens_imovel_fk
FOREIGN KEY (ide_imovel)
REFERENCES imovel (ide_imovel);

ALTER TABLE silos_armazens_unidade_armazenadora ADD CONSTRAINT localizacao_geografica_cad_unidade_armazenadora_silos_armazens_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES localizacao_geografica (ide_localizacao_geografica);

ALTER TABLE silos_armazens ADD CONSTRAINT empreendimento_cad_silos_armazens_fk
FOREIGN KEY (ide_empreendimento)
REFERENCES empreendimento (ide_empreendimento);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_status ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_cadastro_atividade_nao_sujeita_lic_status_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE silos_armazens ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_cad_silos_armazens_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE cadastro_atividade_documento_identificacao_representacao ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_cadastro_atividade_documento_identificacao_representacao_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_comunicacao ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_cadastro_atividade_nao_sujeita_lic_comunicacao_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE silos_armazens_responsavel_tecnico ADD CONSTRAINT cad_silos_armazens_cad_silos_armazens_responsavel_tecnico_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE silos_armazens_imovel ADD CONSTRAINT cad_silos_armazens_cad_silos_armazens_imovel_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE silos_armazens_operacao_desenvolvida ADD CONSTRAINT cad_silos_armazens_cad_silos_armazens_operacao_desenvolvida_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE silos_armazens_tipo_combustivel ADD CONSTRAINT cad_silos_armazens_cad_silos_armazens_tipo_combustivel_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE sistema_seguranca_silos_armazens ADD CONSTRAINT cad_silos_armazens_cad_silos_armazens_sistema_seguranca_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE silos_armazens_unidade_armazenadora ADD CONSTRAINT silos_armazens_silos_armazens_unidade_armazenadora_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE caracterizacao_ambiental_silos_armazens ADD CONSTRAINT silos_armazens_caracterizacao_ambiental_silos_armazens_fk
FOREIGN KEY (ide_silos_armazens)
REFERENCES silos_armazens (ide_silos_armazens);

ALTER TABLE caracterizacao_ambiental_origem_agua ADD CONSTRAINT caracterizacao_ambiental_caracterizacao_ambiental_origem_agua_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE caracterizacao_ambiental_equipamento_controle ADD CONSTRAINT silos_armazens_caracterizacao_ambiental_equipamento_controle_caracterizacao_ambiental_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE classificacao_residuo_caracterizacao_ambiental ADD CONSTRAINT silos_armazens_caracterizacao_ambiental_classificacao_residuo_caracterizacao_ambiental_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE caracterizacao_ambiental_medida_controle_emissao ADD CONSTRAINT silos_armazens_caracterizacao_ambiental_caracterizacao_ambiental_medida_controle_emissao_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE silos_armazens_lancamento_efluente ADD CONSTRAINT caracterizacao_ambiental_silos_armazens_silos_armazens_tratamento_agua_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE silos_armazens_sistema_tratamento_agua ADD CONSTRAINT caracterizacao_ambiental_silos_armazens_silos_armazens_sistema_tratamento_agua_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE caracterizacao_ambiental_caracterizacao_atmosferica ADD CONSTRAINT caracterizacao_ambiental_silos_armazens_caracterizacao_ambiental_caracterizacao_atmosferica_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE caracterizacao_ambiental_destinacao_final ADD CONSTRAINT caracterizacao_ambiental_silos_armazens_caracterizacao_ambiental_destinacao_final_fk
FOREIGN KEY (ide_caracterizacao_ambiental_silos_armazens)
REFERENCES caracterizacao_ambiental_silos_armazens (ide_caracterizacao_ambiental_silos_armazens);

ALTER TABLE origem_agua_tipo_concessionaria ADD CONSTRAINT caracterizacao_ambiental_origem_agua_origem_agua_tipo_concessionaria_fk
FOREIGN KEY (ide_caracterizacao_ambiental_origem_agua)
REFERENCES caracterizacao_ambiental_origem_agua (ide_caracterizacao_ambiental_origem_agua);

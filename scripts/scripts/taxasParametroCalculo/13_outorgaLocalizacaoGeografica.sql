﻿alter TABLE outorga_localizacao_geografica ADD column num_area_dessedentacao_animal numeric(10,2);
alter TABLE outorga_localizacao_geografica ADD column num_area_empreendimento numeric(10,2);
alter TABLE outorga_localizacao_geografica ADD column ind_abastecimento_em_distrito_industrial boolean default false;
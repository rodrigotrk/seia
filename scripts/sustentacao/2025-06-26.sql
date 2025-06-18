--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 12/06/2025 11:24:38
--Versão 4.36.0

BEGIN;

    ALTER TABLE public.fce_localizacao_geografica ADD num_expectativa_captacao numeric(10, 2) NULL;
    ALTER TABLE public.outorga_localizacao_geografica ADD num_expectativa_captacao numeric(10, 2) NULL;
    ALTER TABLE public.outorga_localizacao_geografica ADD ind_concedido boolean NULL;
    ALTER TABLE public.fce_localizacao_geografica ADD ind_concedido boolean NULL;

COMMIT;

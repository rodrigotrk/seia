CREATE OR REPLACE FUNCTION preencher_cerh_status()
RETURNS void AS
$BODY$
DECLARE
        item RECORD;
BEGIN
	FOR item IN select * from cerh_status_historico 
		    where ide_cerh_status in (select max(ide_cerh_status) from cerh_status_historico group by ide_cerh)
		    order by ide_cerh
	LOOP
	     update cerh set ide_cerh_status=item.ide_cerh_tipo_status where ide_cerh = item.ide_cerh;
        END LOOP;        
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

SELECT preencher_cerh_status();
DROP FUNCTION preencher_cerh_status();

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (73, '/paginas/manter-cerh/historico.xhtml', false);

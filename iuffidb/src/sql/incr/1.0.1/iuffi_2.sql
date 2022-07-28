set define off;

-- Add/modify columns 
ALTER TABLE IUF_T_VERBALE
ADD EXT_ID_DOCUMENTO_INDEX number(10);
-- Add comments to the columns
comment on column IUF_T_VERBALE.EXT_ID_DOCUMENTO_INDEX
  is 'Identificativo del documento archiviato su SIAP';

alter table IUF_T_TRAPPOLAGGIO modify id_ispezione_visiva NUMBER(10);

-- Add/modify columns 
alter table IUF_T_ISP_VISIVA_PIANTA modify numero_pianta NUMBER(10);


-- Script grants
declare
  
  Procedure CreateGrantToUser is
  TYPE tpUser IS RECORD (nomeUser  VARCHAR2(30),
                                 Comando   VARCHAR2(300));
                           
  TYPE typTbUser IS TABLE OF tpUser INDEX BY BINARY_INTEGER;                           
  
  tUser    typTbUser;
  
  begin
    tUser(1).nomeUser := 'IUFFI_RW';
    tUser(1).comando  := 'grant select,insert, update, delete on ';
    
    FOR i IN tUser.FIRST..tUser.LAST LOOP
    --grant sulle tabelle
    for c in ( 
       select tUser(i).comando||tb.table_name||' to '||tUser(i).nomeUser cmd from user_tables tb 
    ) loop
      execute immediate c.cmd;
    end loop;

    -- Grant sulle sequence
    for c in ( 
       select 'grant select,alter on '||se.sequence_name||' to '||tUser(i).nomeUser cmd from user_sequences se
    ) loop 
      execute immediate c.cmd;
    end loop;

    -- Grant su ProcedureFunzioniPackage
    for c in ( 
       select distinct 'grant execute on '||pr.object_name||' to '||tUser(i).nomeUser cmd from user_procedures pr 
    ) loop
      execute immediate c.cmd;
    end loop;
    
    -- Grant su types
    for c in ( 
       select distinct 'grant execute on '||pr.type_name||' to '||tUser(i).nomeUser cmd from user_types pr where pr.type_name not like '%SYS%' 
    ) loop
      execute immediate c.cmd;
    end loop;
    
    -- Grant su viste
    for c in ( 
       select distinct 'grant select on '||vi.view_name||' to '||tUser(i).nomeUser cmd from user_views vi 
    ) loop
      execute immediate c.cmd;
    end loop;
    
    end loop;
  end;                           
begin
  CreateGrantToUser;
end;
/

  

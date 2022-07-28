set define off;

-- Add/modify columns 
alter table IUF_T_CAMPIONAMENTO add note varchar2(4000);
-- Add comments to the columns 
comment on column IUF_T_CAMPIONAMENTO.note
  is 'Campo note';

-- Add/modify columns 
alter table IUF_T_ESITO_CAMPIONE add note varchar2(4000);
-- Add comments to the columns 
comment on column IUF_T_ESITO_CAMPIONE.note
  is 'Campo note';
  
-- Create table
create table iuf_d_codice_esito
(
  id_codice_esito       NUMBER(4) not null,
  codice                VARCHAR2(5) not null,
  descrizione            VARCHAR2(50) not null,
  data_inizio_validita        DATE not null,
  data_fine_validita          DATE,
  data_inserimento            date default sysdate not null,
  ext_id_utente_aggiornamento NUMBER(10) not null,
  data_ultimo_aggiornamento   DATE default sysdate not null
)
tablespace IUFFI_TBL;
-- Add comments to the table 
comment on table iuf_d_codice_esito
  is 'Codici esiti  di laboratorio';
-- Add comments to the columns 
comment on column iuf_d_codice_esito.id_codice_esito
  is 'Progressivo univoco identificativo del record';
comment on column iuf_d_codice_esito.codice
  is 'Codice esito laboratorio';
comment on column iuf_d_codice_esito.descrizione
  is 'Descrizione estesa esito laboratorio';  
comment on column iuf_d_codice_esito.data_inizio_validita
  is 'Data inizio validità del piano';
comment on column iuf_d_codice_esito.data_fine_validita
  is 'Data fine validità del record';
comment on column iuf_d_codice_esito.data_inserimento
  is 'Data inserimento del piano';
comment on column iuf_d_codice_esito.ext_id_utente_aggiornamento
  is 'Utente inserimento o di ultima modifica del record';
comment on column iuf_d_codice_esito.data_ultimo_aggiornamento
  is 'Timestamp inserimento/ultimo aggiornamento del record';
-- Create/Recreate primary, unique and foreign key constraints 
alter table iuf_d_codice_esito
  add constraint PK_IUF_D_CODICE_ESITO primary key (ID_CODICE_ESITO)
  using index 
  tablespace IUFFI_IDX;
  
alter table IUF_D_CODICE_ESITO drop column data_inserimento;
alter table IUF_T_ESITO_CAMPIONE drop column esito;
alter table IUF_T_ESITO_CAMPIONE add id_codice_esito number(4);
comment on column IUF_T_ESITO_CAMPIONE.id_codice_esito
  is 'Riferimento all''esito dell''esame codificato';
alter table IUF_T_ESITO_CAMPIONE
  add constraint FK_IUF_D_CODICE_ESITO_01 foreign key (ID_CODICE_ESITO)
  references iuf_d_codice_esito (ID_CODICE_ESITO);  
  
-- Add comments to the table 
comment on table IUF_D_TRAPPOLA_ON
  is 'Associazione trappole - organismi nocivi';
-- Create/Recreate primary, unique and foreign key constraints 
alter table IUF_D_TRAPPOLA_ON
  drop constraint PK_IUF_D_TRAPPOLA_ON cascade;
alter table IUF_D_TRAPPOLA_ON
  add constraint PK_IUF_D_TRAPPOLA_ON primary key (ID_TRAPPOLA_ON)
  using index 
  tablespace IUFFI_IDX;
alter table IUF_D_TRAPPOLA_ON
  add constraint AK_IUF_D_TRAPPOLA_ON unique (ID_TRAPPOLA, ID_ON, DATA_FINE_VALIDITA);  

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

  

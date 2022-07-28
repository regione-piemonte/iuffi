set define off;

-- Create table
create table IUF_D_TIPO_ATTIVITA
(
  id_tipo_attivita            NUMBER(4) not null,
  desc_tipo_attivita          VARCHAR2(255) not NULL
)
tablespace IUFFI_TBL;
-- Add comments to the table 
comment on table IUF_D_TIPO_ATTIVITA
  is 'Tabella tipologie attività';
-- Add comments to the columns 
comment on column IUF_D_TIPO_ATTIVITA.id_tipo_attivita
  is 'Progressivo univoco identificativo del record';
comment on column IUF_D_TIPO_ATTIVITA.desc_tipo_attivita
  is 'Descrizione tipologia attività';
-- Create/Recreate primary, unique and foreign key constraints 
alter table IUF_D_TIPO_ATTIVITA
  add constraint PK_IUF_D_TIPO_ATTIVITA primary key (id_tipo_attivita)
  using index 
  tablespace IUFFI_IDX;
-- Create table
create table IUF_D_SHAPE_MASTER
(
  id_shape_master             NUMBER(10) not null,
  descrizione                 VARCHAR2(255) not null,
  data_attivita               DATE,
  id_tipo_attivita            NUMBER(4),
  data_inizio_validita        DATE not null,
  data_fine_validita          DATE,
  ext_id_utente_aggiornamento NUMBER(10) not null,
  data_ultimo_aggiornamento   DATE default sysdate not null
)
tablespace IUFFI_TBL;
-- Add comments to the table 
comment on table IUF_D_SHAPE_MASTER
  is 'Tabella shapefile master';
-- Add comments to the columns 
comment on column IUF_D_SHAPE_MASTER.id_shape_master
  is 'Progressivo univoco identificativo del record';
comment on column IUF_D_SHAPE_MASTER.descrizione
  is 'Descrizione contenuto';
comment on column IUF_D_SHAPE_MASTER.data_attivita
  is 'Data attività';
comment on column IUF_D_SHAPE_MASTER.id_tipo_attivita
  is 'Riferimento al tipo di attività';
comment on column IUF_D_SHAPE_MASTER.data_inizio_validita
  is 'Data inizio validità del record';
comment on column IUF_D_SHAPE_MASTER.data_fine_validita
  is 'Data fine validità del record';
comment on column IUF_D_SHAPE_MASTER.ext_id_utente_aggiornamento
  is 'Utente inserimento o di ultima modifica del record';
comment on column IUF_D_SHAPE_MASTER.data_ultimo_aggiornamento
  is 'Timestamp inserimento/ultimo aggiornamento del record';
-- Create/Recreate primary, unique and foreign key constraints 
alter table IUF_D_SHAPE_MASTER
  add constraint PK_IUF_D_SHAPE_MASTER primary key (ID_SHAPE_MASTER)
  using index 
  tablespace IUFFI_IDX;
alter table IUF_D_SHAPE_MASTER
  add constraint FK_IUF_D_TIPO_ATTIVITA_01 foreign key (ID_TIPO_ATTIVITA)
  references IUF_D_TIPO_ATTIVITA (ID_TIPO_ATTIVITA);
-- Create table
create table iuf_d_shape_detail
(
  id_shape_detail number(10) not null,
  latitudine      number(18,15) not null,
  longitudine     number(18,15) not null,
  id_shape_master number(10) not null
)
tablespace IUFFI_TBL;
-- Add comments to the table 
comment on table iuf_d_shape_detail
  is 'Tabella shapefile dettaglio (coordinate)';
-- Add comments to the columns 
comment on column iuf_d_shape_detail.id_shape_detail
  is 'Progressivo univoco identificativo del record';
comment on column iuf_d_shape_detail.latitudine
  is 'Latitudine (WGS 84)';
comment on column iuf_d_shape_detail.longitudine
  is 'Longitudine (WGS 84)';
comment on column iuf_d_shape_detail.id_shape_master
  is 'Riferimento al record master cui si riferisce il punto';
-- Create/Recreate primary, unique and foreign key constraints 
alter table iuf_d_shape_detail
  add constraint pk_iuf_d_shape_detail primary key (ID_SHAPE_DETAIL)
  using index 
  tablespace IUFFI_IDX;
alter table iuf_d_shape_detail
  add constraint fk_iuf_d_shape_master_01 foreign key (ID_SHAPE_MASTER)
  references iuf_d_shape_master (ID_SHAPE_MASTER);
-- Create/Recreate indexes 
create index ie1_iuf_d_shape_detail on iuf_d_shape_detail (id_shape_master)
  tablespace IUFFI_IDX;
-- Create sequence 
create sequence SEQ_IUF_D_SHAPE_MASTER start with 1 nocache;
-- Create sequence 
create sequence SEQ_IUF_D_SHAPE_DETAIL start with 1 nocache;

-- Add/modify columns 
alter table IUF_D_SHAPE_MASTER add id_organismo_nocivo number(4);
-- Add comments to the columns 
comment on column IUF_D_SHAPE_MASTER.id_organismo_nocivo
  is 'Identificativo dell''organismo nocivo cui si riferiscono le coordinate';
-- Create/Recreate primary, unique and foreign key constraints 
alter table IUF_D_SHAPE_MASTER
  add constraint FK_IUF_D_ORGANISMO_NOCIVO_09 foreign key (ID_ORGANISMO_NOCIVO)
  references iuf_d_organismo_nocivo (ID_ORGANISMO_NOCIVO);

-- Add/modify columns 
alter table IUF_D_ORGANISMO_NOCIVO add flag_pianificazione VARCHAR2(1) default 'N';
-- Add comments to the columns 
comment on column IUF_D_ORGANISMO_NOCIVO.flag_pianificazione
  is 'Flag S/N - Indica se l''organismo nocivo deve essere mostrato in fase di upload shape file di pianificazione';


-- Add/modify columns 
alter table IUF_D_SHAPE_MASTER add comune varchar2(100);
alter table IUF_D_SHAPE_MASTER add campionamento varchar2(100);
alter table IUF_D_SHAPE_MASTER add trappola varchar2(100);
alter table IUF_D_SHAPE_MASTER add cod_tipo_trappola varchar2(100);
alter table IUF_D_SHAPE_MASTER add desc_tipo_trappola varchar2(100);
alter table IUF_D_SHAPE_MASTER add n_visual varchar2(100);
alter table IUF_D_SHAPE_MASTER add note varchar2(255);
-- Add comments to the columns 
comment on column IUF_D_SHAPE_MASTER.comune
  is 'Comune oggetto della pianificazione presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.campionamento
  is 'Identificativo del campionamento presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.trappola
  is 'Identificativo della trappola presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.cod_tipo_trappola
  is 'Codice tipo trappola presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.desc_tipo_trappola
  is 'Descrizione tipo trappola presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.n_visual
  is 'Attributo n_visual presente nello shape file';
comment on column IUF_D_SHAPE_MASTER.note
  is 'Note presenti nello shape file';


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

  

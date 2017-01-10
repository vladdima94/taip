
DROP TABLE IMAGE_FEATURES_SLAVE CASCADE CONSTRAINTS;
DROP TABLE STATS_SLAVE CASCADE CONSTRAINTS;

CREATE TABLE IMAGE_FEATURES_SLAVE
  (
    ID INTEGER NOT NULL,
    IMAGE_LINK   VARCHAR2 (1024) NOT NULL,
    FEATURE_PATH VARCHAR2 (1024) NOT NULL,
    ID_CLUSTER INTEGER
  ) ;


CREATE TABLE STATS_SLAVE
  (
    MAX_DB_SIZE     INTEGER NOT NULL ,
    CURRENT_DB_SIZE INTEGER NOT NULL
  ) ;


--------------------------------------------------------------------------------
-- TRIGGERS --------------------------------------------------------------------
--------------------------------------------------------------------------------
DROP SEQUENCE IMAGE_FEATURES_SEQ_SLAVE; 
CREATE SEQUENCE IMAGE_FEATURES_SEQ_SLAVE START WITH 1;
/
CREATE OR REPLACE TRIGGER FIMAGE_FEATURES_TRIGGER_SLAVE
BEFORE INSERT ON IMAGE_FEATURES_SLAVE
FOR EACH ROW
BEGIN
  SELECT IMAGE_FEATURES_SEQ_SLAVE.NEXTVAL
  INTO   :new.ID
  FROM   dual;
END;
/
--------------------------------------------------------------------------------
--------------------------------------------------------------------------------
commit;

SELECT * FROM IMAGE_FEATURES_SLAVE ;
SELECT count(ID) FROM IMAGE_FEATURES_SLAVE WHERE ID_CLUSTER = 5;
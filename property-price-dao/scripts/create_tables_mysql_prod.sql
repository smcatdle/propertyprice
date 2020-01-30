DROP TABLE property_sale_error;
DROP TABLE property_sale;
DROP TABLE property;
DROP TABLE address;
DROP TABLE geocode;


CREATE TABLE geocode
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  latitude DOUBLE(18,15),
  longitude DOUBLE(18,15),
  format_addrr VARCHAR(60),
  status VARCHAR(1) NOT NULL,
  type VARCHAR(1) NOT NULL,
  location_type VARCHAR(1) NOT NULL,
  partial_match BOOLEAN,
  results SMALLINT(2),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT geocode_id_pk PRIMARY KEY (id)
);


CREATE TABLE address
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  geocode_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  address_line_1 VARCHAR(50) NOT NULL,
  address_line_2 VARCHAR(50),
  address_line_3 VARCHAR(50),
  address_line_4 VARCHAR(50),
  address_line_5 VARCHAR(50),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  -- Remove this constraint as we want duplicate addresses for an update history of this address.
  --CONSTRAINT address_uq UNIQUE (address_line_1, address_line_2, address_line_3, address_line_4, address_line_5),
  CONSTRAINT address_id_pk PRIMARY KEY (id),
  CONSTRAINT geocode_uq UNIQUE (geocode_id),
  CONSTRAINT geocode_id_fk FOREIGN KEY (geocode_id)
      REFERENCES geocode (id) 
);


CREATE TABLE property_sale
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  address_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  price DOUBLE(11,2) NOT NULL,
  date_of_sale DATE NOT NULL,
  full_market_price BOOLEAN,
  vat_exclusive BOOLEAN,
  property_size VARCHAR(60),
  description VARCHAR(60),
  ppr_url VARCHAR(150) NOT NULL,
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT address_id_uq UNIQUE (address_id),
  CONSTRAINT ppr_url_uq UNIQUE (ppr_url),
  CONSTRAINT property_sale_id_pk PRIMARY KEY (id),
  CONSTRAINT address_id_fk FOREIGN KEY (address_id)
      REFERENCES address (id) 
);

CREATE TABLE property_sale_update
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  ppr_url VARCHAR(150) NOT NULL,
  date_created DATE NOT NULL,
  CONSTRAINT property_sale_update_id_pk PRIMARY KEY (id)
);

CREATE TABLE property_sale_error
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  ppr_url VARCHAR(150) NOT NULL,
  issue VARCHAR(5) NOT NULL,
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT ppr_url_uq UNIQUE (ppr_url)
);

CREATE TABLE property
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  property_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  address_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  price DOUBLE(11,2) NOT NULL,
  beds SMALLINT(1) UNSIGNED ZEROFILL NOT NULL,
  baths SMALLINT(1) UNSIGNED ZEROFILL NOT NULL,
  image_url TEXT(400) NOT NULL,
  file_id VARCHAR(40),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT address_id_uq UNIQUE (address_id),
  CONSTRAINT property_id_uq UNIQUE (property_id),
  CONSTRAINT property_id_pk PRIMARY KEY (id),
  CONSTRAINT property_address_id_fk FOREIGN KEY (address_id)
      REFERENCES address (id) 
);

CREATE TABLE server_config
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  master_ip_address VARCHAR(15) NOT NULL,
  batch_size SMALLINT(4) UNSIGNED ZEROFILL NOT NULL,
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT server_config_id_pk PRIMARY KEY (id)
);

CREATE TABLE server_node
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  server_name VARCHAR(100) NOT NULL,
  type VARCHAR(1) NOT NULL,
  CONSTRAINT server_nodes_id_pk PRIMARY KEY (id),
  CONSTRAINT server_name_uq UNIQUE (server_name)
);

CREATE TABLE user_event
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  ip_address VARCHAR(20) NOT NULL,
  event_type VARCHAR(1) NOT NULL,
  data_type VARCHAR(1) NOT NULL,
  data VARCHAR(400) NOT NULL,
  date_created DATE NOT NULL,
  CONSTRAINT user_query_log_id_pk PRIMARY KEY (id)
);

CREATE TABLE user_crash
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  ip_address VARCHAR(20),
  device VARCHAR(20),
  event_type VARCHAR(1),
  data_type VARCHAR(1),
  data VARCHAR(4000) NOT NULL,
  date_created DATE,
  CONSTRAINT user_crash_id_pk PRIMARY KEY (id)
);

-- Indexes
CREATE INDEX address_geocode_latitude_idx ON geocode (latitude) USING BTREE;
CREATE INDEX address_geocode_longitude_idx ON geocode (longitude) USING BTREE;
CREATE INDEX address_address_line_1_idx ON address (address_line_1(50)) USING HASH;
CREATE INDEX geocode_grid_id_idx ON geocode (grid_id);

-- Updates
ALTER TABLE geocode ADD cross_check_type VARCHAR(1);
ALTER TABLE geocode ADD cross_check_score DOUBLE(10,7);
ALTER TABLE geocode ADD cross_check_lat DOUBLE(18,15);
ALTER TABLE geocode ADD cross_check_long DOUBLE(18,15);
ALTER TABLE property_sale ADD status VARCHAR(1);
ALTER TABLE address DROP INDEX address_uq;
ALTER TABLE geocode modify format_addrr VARCHAR(160);
ALTER TABLE geocode ADD longitude_bck DOUBLE(18,15);
ALTER TABLE geocode ADD latitude_bck DOUBLE(18,15);
ALTER TABLE geocode ADD geocode_cur_type VARCHAR(1);
ALTER TABLE geocode ADD geocode_bck_type VARCHAR(1);
ALTER TABLE geocode ADD format_addrr_bck VARCHAR(160);
ALTER TABLE geocode ADD cross_check_code SMALLINT(1) NOT NULL DEFAULT 0;
ALTER TABLE property_sale ADD beds SMALLINT(1);
ALTER TABLE property_sale ADD type VARCHAR(10);
ALTER TABLE geocode ADD grid_id INTEGER NOT NULL;
ALTER TABLE geocode MODIFY grid_id INTEGER NOT NULL default 0;
ALTER TABLE property_sale MODIFY ppr_url VARCHAR(60) NOT NULL;







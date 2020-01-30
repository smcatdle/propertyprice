DROP TABLE daft_property;
DROP TABLE address;


CREATE TABLE address
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  address_line_1 VARCHAR(50) NOT NULL,
  address_line_2 VARCHAR(50),
  address_line_3 VARCHAR(50),
  address_line_4 VARCHAR(50),
  address_line_5 VARCHAR(50),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT address_id_pk PRIMARY KEY (id)
);


CREATE TABLE daft_property
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  address_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  property_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  price DOUBLE(11,2) NOT NULL,
  beds SMALLINT(1),
  baths SMALLINT(1),
  size DOUBLE(11,2),
  dwelling_type VARCHAR(30),
  description VARCHAR(4000),
  file_id VARCHAR(20),
  url VARCHAR(100),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT address_id_uq UNIQUE (address_id),
  CONSTRAINT property_id_uq UNIQUE (property_id),
  CONSTRAINT daft_property_id_pk PRIMARY KEY (id),
  CONSTRAINT address_id_fk FOREIGN KEY (address_id)
      REFERENCES address (id) 
);

ALTER TABLE daft_property ADD CONSTRAINT url_uq UNIQUE (url);

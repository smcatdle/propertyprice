DROP TABLE address;

CREATE TABLE address
(
  id INT(4) UNSIGNED ZEROFILL NOT NULL,
  address_line_1 VARCHAR(200) NOT NULL,
  address_line_2 VARCHAR(200),
  address_line_3 VARCHAR(200),
  address_line_4 VARCHAR(200),
  address_line_5 VARCHAR(200),
  geocode_latitude DOUBLE(13,10),
  geocode_longitude DOUBLE(13,10),
  CONSTRAINT address_id_pk PRIMARY KEY (id)
);

DROP TABLE property_sale;

CREATE TABLE property_sale
(
  id INT(4) UNSIGNED ZEROFILL NOT NULL,
  address_id INT(4) UNSIGNED ZEROFILL NOT NULL,
  price DOUBLE(11,2) NOT NULL,
  date_of_sale DATE NOT NULL,
  full_market_price BOOLEAN,
  ppr_url TEXT(400) NOT NULL,
  CONSTRAINT property_sale_id_pk PRIMARY KEY (id),
  CONSTRAINT address_id_fk FOREIGN KEY (address_id)
      REFERENCES address (id) 
);

DROP TABLE property;

CREATE TABLE property
(
  id INT(4) UNSIGNED ZEROFILL NOT NULL,
  property_id INT(4) UNSIGNED ZEROFILL NOT NULL,
  address_id INT(4) UNSIGNED ZEROFILL NOT NULL,
  price DOUBLE(11,2) NOT NULL,
  beds INT(4) UNSIGNED ZEROFILL NOT NULL,
  baths INT(4) UNSIGNED ZEROFILL NOT NULL,
  image_url TEXT(400) NOT NULL,
  file_id VARCHAR(40),
  CONSTRAINT property_id_pk PRIMARY KEY (id),
  CONSTRAINT property_address_id_fk FOREIGN KEY (address_id)
      REFERENCES address (id) 
);
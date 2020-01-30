-- Server Configuration
INSERT INTO server_config (id, master_ip_address, batch_size, date_created, date_updated) VALUES (0, '10.209.169.162', 10, CURDATE(), CURDATE());


-- Server Nodes Configuration
INSERT INTO server_node (id, server_name, type) VALUES (0, 'sales-propertyprod.rhcloud.com', 'M');
INSERT INTO server_node (id, server_name, type) VALUES (1, 'irishpropertyprice-nd00.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (2, 'irishpropertyprice-nd01.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (3, 'irishpropertyprice-nd02.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (4, 'irishpropertyprice-nd03.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (5, 'irishpropertyprice-nd04.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (6, 'irishpropertyprice-nd05.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (7, 'irishpropertyprice-nd06.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (8, 'irishpropertyprice-nd07.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (9, 'irishpropertyprice-nd08.rhcloud.com', 'S');
INSERT INTO server_node (id, server_name, type) VALUES (10, 'irishpropertyprice-nd09.rhcloud.com', 'S');



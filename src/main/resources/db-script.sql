CREATE TABLE Customer(
                         id VARCHAR(50) PRIMARY KEY,
                         name VARCHAR(200) NOT NULL,
                         address VARCHAR(250) NOT NULL
);

INSERT INTO Customer (id, name, address) VALUES (UUID(),'Niipun','Badulla'),
                                                (UUID(),'Milinda','Colombo'),
                                                (UUID(),'Sandaru','Gampaha'),
                                                (UUID(), 'Banda','Kurunagala');


CREATE TABLE Item(
                     code VARCHAR(36) PRIMARY KEY,
                     stock INT NOT NULL,
                     unit_price DECIMAL(5,2) NOT NULL,
                     description VARCHAR(300) NOT NULL
);

CREATE TABLE `Order`(
                        id VARCHAR(36) PRIMARY KEY ,
                        customer_id VARCHAR(36) NOT NULL,
                        date DATE NOT NULL,
                        FOREIGN KEY (customer_id) REFERENCES Customer(id)

);

CREATE TABLE OrderDetail(
                            order_id VARCHAR(36) NOT NULL,
                            item_code VARCHAR(36) NOT NULL,
                            qty INT NOT NULL,
                            unit_price DECIMAL(5,2) NOT NULL,
                            FOREIGN KEY (order_id) REFERENCES `Order`(id),
                            FOREIGN KEY (item_code) REFERENCES Item(code),
                            PRIMARY KEY (order_id,item_code)
);
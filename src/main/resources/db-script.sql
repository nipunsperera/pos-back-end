CREATE TABLE Customer(
                         id VARCHAR(50) PRIMARY KEY,
                         name VARCHAR(200) NOT NULL,
                         address VARCHAR(250) NOT NULL
);

INSERT INTO Customer (id, name, address) VALUES (UUID(),'Niipun','Badulla'),
                                                (UUID(),'Milinda','Colombo'),
                                                (UUID(),'Sandaru','Gampaha'),
                                                (UUID(), 'Banda','Kurunagala');
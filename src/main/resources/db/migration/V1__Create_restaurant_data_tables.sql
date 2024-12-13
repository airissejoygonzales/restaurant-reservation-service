CREATE TABLE customers(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20),
    preferred_comms VARCHAR(20)
);

CREATE TABLE reservations (
      id INT AUTO_INCREMENT PRIMARY KEY,
      customer_id INT NOT NULL,
      reservation_date TIMESTAMP,
      guest_count INT,
      status VARCHAR(20),
      created_at TIMESTAMP,
      last_modified_at TIMESTAMP,
      FOREIGN KEY (customer_id) REFERENCES customers(id)
);

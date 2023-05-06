CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    phone_number VARCHAR(20),
    role_id INT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES user_roles (id)
);

INSERT INTO user_roles (role_name, description)
VALUES ('Normal User', 'A regular user'),
       ('Admin', 'An administrator'),
       ('Driver', 'A driver');

INSERT INTO users (username, password, email, created_date, modified_date, phone_number, role_id)
VALUES ('testuser', 'password123', 'testuser@example.com', NOW(), NOW(), '1234567890', 1);

-- Create truck_types table
CREATE TABLE IF NOT EXISTS truck_types (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(20) NOT NULL UNIQUE
);

-- Insert data into truck_types table
INSERT INTO truck_types (type_name) VALUES
('VAN'), ('TRUCK'), ('REFRIGERATE'), ('MINI_TRUCK');

-- Create trucks table with foreign key to truck_types
CREATE TABLE IF NOT EXISTS trucks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    title VARCHAR(100),
    image TEXT,
    price DECIMAL(10, 2),
    type INTEGER NOT NULL REFERENCES truck_types (id),
    location VARCHAR(100),
    latitude DECIMAL(10, 6),
    longitude DECIMAL(10, 6)
);

INSERT INTO trucks (name, description, title, image, price, type, location, latitude, longitude)
VALUES
  ('Truck 1', 'Lorem ipsum dolor sit amet', 'Title 1', 'https://example.com/truck1.jpg', 1000, 1, 'Location 1', 1.234, 2.345),
  ('Truck 2', 'Lorem ipsum dolor sit amet', 'Title 2', 'https://example.com/truck2.jpg', 2000, 2, 'Location 2', 3.456, 4.567),
  ('Truck 3', 'Lorem ipsum dolor sit amet', 'Title 3', 'https://example.com/truck3.jpg', 3000, 3, 'Location 3', 5.678, 6.789),
  ('Truck 4', 'Lorem ipsum dolor sit amet', 'Title 4', 'https://example.com/truck4.jpg', 4000, 4, 'Location 4', 7.890, 8.901),
  ('Truck 5', 'Lorem ipsum dolor sit amet', 'Title 5', 'https://example.com/truck5.jpg', 5000, 1, 'Location 5', 9.012, 1.234),
  ('Truck 6', 'Lorem ipsum dolor sit amet', 'Title 6', 'https://example.com/truck6.jpg', 6000, 2, 'Location 6', 2.345, 3.456),
  ('Truck 7', 'Lorem ipsum dolor sit amet', 'Title 7', 'https://example.com/truck7.jpg', 7000, 3, 'Location 7', 4.567, 5.678),
  ('Truck 8', 'Lorem ipsum dolor sit amet', 'Title 8', 'https://example.com/truck8.jpg', 8000, 4, 'Location 8', 6.789, 7.890),
  ('Truck 9', 'Lorem ipsum dolor sit amet', 'Title 9', 'https://example.com/truck9.jpg', 9000, 1, 'Location 9', 8.901, 9.012),
  ('Truck 10', 'Lorem ipsum dolor sit amet', 'Title 10', 'https://example.com/truck10.jpg', 10000, 2, 'Location 10', 1.234, 2.345);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    vehicle_id INTEGER NOT NULL,
    receiver_name VARCHAR(255) NOT NULL,
    pickup_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    pickup_location VARCHAR(255) NOT NULL,
    good_type VARCHAR(255) NOT NULL,
    weight DECIMAL(10, 2) NOT NULL,
    width DECIMAL(10, 2) NOT NULL,
    length DECIMAL(10, 2) NOT NULL,
    height DECIMAL(10, 2) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_user_orders FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_vehicle_orders FOREIGN KEY (vehicle_id) REFERENCES trucks (id)
);
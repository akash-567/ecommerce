-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, role) 
VALUES ('admin', 'admin@example.com', '$2a$10$ZK/GZOKqj9D1Yk.zqoYqU.WZB3T5Zp.kFHYJGW/u.UZf2ZYj5YtXi', 'ROLE_ADMIN');

-- Insert regular user (password: user123)
INSERT INTO users (username, email, password, role)
VALUES ('user', 'user@example.com', '$2a$10$ZwAEls4KQWzHx.zF.BQxKO4HFnKPZm/PRQX0ULhZYQY9v.mxXGdYi', 'ROLE_USER');

-- Insert sample products
INSERT INTO product (name, description, price, image_url, stock_quantity)
VALUES 
('iPhone 13', 'Latest iPhone model with A15 Bionic chip', 999.99, 'https://example.com/iphone13.jpg', 50),
('Samsung Galaxy S21', '5G Android smartphone with 8K video', 899.99, 'https://example.com/galaxys21.jpg', 45),
('MacBook Pro', '14-inch MacBook Pro with M1 Pro chip', 1999.99, 'https://example.com/macbook.jpg', 30),
('AirPods Pro', 'Wireless earbuds with active noise cancellation', 249.99, 'https://example.com/airpods.jpg', 100),
('iPad Air', '10.9-inch iPad with M1 chip', 599.99, 'https://example.com/ipad.jpg', 60); 
CREATE TABLE rol_Opciones (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
INSERT INTO rol_Opciones (name) VALUES
('Create'),
('Read'),
('Update'),
('Delete');

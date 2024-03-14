CREATE TABLE rol_Opciones_rol (
    rol_Opciones_id_Opcion SERIAL REFERENCES rol_Opciones(id),
    rol_id_Rol SERIAL REFERENCES roles(id),
    PRIMARY KEY (rol_Opciones_id_Opcion, rol_id_Rol)
);
-- Asignar todas las operaciones del CRUD al rol 'administrador'
INSERT INTO rol_Opciones_rol (rol_Opciones_id_Opcion, rol_id_Rol) VALUES
(1, 2),
(2, 2),
(3, 2),
(4, 2);

-- Asignar solo la operación 'Read' al rol 'user'
INSERT INTO rol_Opciones_rol (rol_Opciones_id_Opcion, rol_id_Rol) VALUES (2, 1);

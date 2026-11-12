-- Usuarios iniciales. Las claves se guardan encriptadas con SHA-256 (ver PasswordUtil):
--   admin    -> admin123
--   yazminvs -> yaz123
--   pepe     -> pepe12
MERGE INTO usuario (id_usuario, usuario, clave, estado, id_perfil) KEY (id_usuario)
    VALUES (1, 'admin', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'ACTIVO', 1),
           (2, 'yazminvs', 'jAgi9Yuk7cB0k8ZeTP9Xnxl0i/j4rI4oO3s715887OY=', 'ACTIVO', 1),
           (3, 'pepe', 'VOAgbfArhzKivGJ+QynTGX7VJ8SE4dXtewfojvRj72c=', 'ACTIVO', 3);

# Ejemplos de Peticiones HTTP para Testing

## USUARIOS

### 1. Crear Usuario
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Roberto",
  "apellido": "Silva Mendoza",
  "email": "roberto.silva@email.com",
  "telefono": "5551112233"
}

### 2. Obtener Todos los Usuarios
GET http://localhost:8080/api/usuarios

### 3. Obtener Usuario por ID
GET http://localhost:8080/api/usuarios/1

### 4. Obtener Usuario con Tarjetas
GET http://localhost:8080/api/usuarios/1/con-tarjetas

### 5. Actualizar Usuario
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "Roberto",
  "apellido": "Silva Mendoza",
  "email": "roberto.silva.nuevo@email.com",
  "telefono": "5559998877"
}

### 6. Buscar Usuarios
GET http://localhost:8080/api/usuarios/buscar?q=Silva

### 7. Obtener Usuarios Activos
GET http://localhost:8080/api/usuarios/activos

### 8. Eliminar Usuario (Soft Delete)
DELETE http://localhost:8080/api/usuarios/1

---

## TARJETAS

### 1. Crear Tarjeta de Crédito
POST http://localhost:8080/api/tarjetas
Content-Type: application/json

{
  "numeroTarjeta": "4532111122223333",
  "titular": "Roberto Silva",
  "fechaVencimiento": "2027-12-31",
  "usuarioId": 1,
  "tipoTarjeta": "CREDITO",
  "limiteCredito": 60000,
  "tasaInteres": 17.5
}

### 2. Crear Tarjeta de Débito
POST http://localhost:8080/api/tarjetas
Content-Type: application/json

{
  "numeroTarjeta": "5123444455556666",
  "titular": "Roberto Silva",
  "fechaVencimiento": "2026-08-31",
  "usuarioId": 1,
  "tipoTarjeta": "DEBITO",
  "cuentaAsociada": "1234567890",
  "permiteSobregiro": true,
  "limiteSobregiro": 8000
}

### 3. Crear Tarjeta de Nómina
POST http://localhost:8080/api/tarjetas
Content-Type: application/json

{
  "numeroTarjeta": "6011777788889999",
  "titular": "Roberto Silva",
  "fechaVencimiento": "2028-06-30",
  "usuarioId": 1,
  "tipoTarjeta": "NOMINA",
  "empresa": "Mi Empresa SA",
  "numeroEmpleado": "EMP999"
}

### 4. Obtener Todas las Tarjetas
GET http://localhost:8080/api/tarjetas

### 5. Obtener Tarjeta por ID
GET http://localhost:8080/api/tarjetas/1

### 6. Obtener Tarjetas de un Usuario
GET http://localhost:8080/api/tarjetas/usuario/1

### 7. Obtener Tarjetas Activas de un Usuario
GET http://localhost:8080/api/tarjetas/usuario/1/activas

### 8. Agregar Saldo a Tarjeta
POST http://localhost:8080/api/tarjetas/1/agregar-saldo
Content-Type: application/json

{
  "monto": 5000.00
}

### 9. Eliminar Tarjeta (Soft Delete)
DELETE http://localhost:8080/api/tarjetas/1

---

## CASOS DE PRUEBA DE VALIDACIONES

### Error: Email Inválido
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Test",
  "apellido": "User",
  "email": "email-sin-arroba",
  "telefono": "5551234567"
}

### Error: Teléfono Inválido
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Test",
  "apellido": "User",
  "email": "test@email.com",
  "telefono": "123"
}

### Error: Campos Vacíos
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "",
  "apellido": "",
  "email": "",
  "telefono": ""
}

### Error: Email Duplicado
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Duplicado",
  "apellido": "Test",
  "email": "juan.perez@email.com",
  "telefono": "5559998877"
}

### Error: Monto Negativo
POST http://localhost:8080/api/tarjetas/1/agregar-saldo
Content-Type: application/json

{
  "monto": -1000.00
}

### Error: Usuario Inexistente
POST http://localhost:8080/api/tarjetas
Content-Type: application/json

{
  "numeroTarjeta": "4532999988887777",
  "titular": "Test",
  "fechaVencimiento": "2027-12-31",
  "usuarioId": 9999,
  "tipoTarjeta": "CREDITO",
  "limiteCredito": 50000
}

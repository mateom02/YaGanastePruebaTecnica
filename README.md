# API REST - Sistema de Gestión de Usuarios y Tarjetas

## Descripción del Proyecto

Este proyecto es una API REST desarrollada en Java con Spring Boot que implementa un sistema de gestión de usuarios y tarjetas bancarias. Demuestra los principales conceptos de Programación Orientada a Objetos (POO) y buenas prácticas de desarrollo de APIs REST.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Oracle Database**
- **Maven**
- **Bean Validation**

## Características Principales

### 1. Programación Orientada a Objetos (POO)

- **Encapsulamiento**: Todas las clases tienen sus atributos privados con getters y setters
- **Herencia**: Clase abstracta `Tarjeta` con subclases `TarjetaCredito`, `TarjetaDebito` y `TarjetaNomina`
- **Polimorfismo**: Método abstracto `add()` implementado de forma diferente en cada tipo de tarjeta
- **Abstracción**: Clase abstracta `Tarjeta` que define el comportamiento común
- **Composición**: La clase `Usuario` contiene una lista de `Tarjeta`

### 2. API REST

#### Endpoints de Usuarios

- `POST /api/usuarios` - Crear usuario
- `GET /api/usuarios` - Obtener todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/{id}/con-tarjetas` - Obtener usuario con sus tarjetas
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario (soft delete)
- `GET /api/usuarios/buscar?q={texto}` - Buscar usuarios
- `GET /api/usuarios/activos` - Obtener usuarios activos

#### Endpoints de Tarjetas

- `POST /api/tarjetas` - Crear tarjeta
- `GET /api/tarjetas` - Obtener todas las tarjetas
- `GET /api/tarjetas/{id}` - Obtener tarjeta por ID
- `GET /api/tarjetas/usuario/{usuarioId}` - Obtener tarjetas de un usuario
- `GET /api/tarjetas/usuario/{usuarioId}/activas` - Obtener tarjetas activas de un usuario
- `POST /api/tarjetas/{id}/agregar-saldo` - Agregar saldo a una tarjeta
- `DELETE /api/tarjetas/{id}` - Eliminar tarjeta (soft delete)

### 3. Base de Datos

- Estructura relacional con Oracle Database
- Herencia mediante estrategia de tabla única (Single Table Inheritance)
- Secuencias para generación de IDs
- Índices para optimización de consultas
- Scripts SQL completos incluidos

## Estructura del Proyecto

```
tarjetas-api/
├── src/
│   ├── main/
│   │   ├── java/com/prueba/tarjetas/
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Excepciones personalizadas
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios Spring Data
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── util/            # Clase Result
│   │   │   └── TarjetasApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/prueba/tarjetas/
│           └── PruebasPOO.java  # Clase de prueba POO
├── sql/
│   ├── 01_crear_tablas.sql      # Script de creación de BD
│   ├── 02_insertar_datos.sql    # Script de datos de prueba
│   └── 03_consultas.sql         # Script de consultas SQL
├── pom.xml
└── README.md
```

## Requisitos Previos

1. **Java Development Kit (JDK) 17 o superior**
   - Descargar desde: https://www.oracle.com/java/technologies/downloads/

2. **Maven 3.6 o superior**
   - Descargar desde: https://maven.apache.org/download.cgi

3. **Oracle Database**
   - Oracle Express Edition (XE) o superior
   - Descargar desde: https://www.oracle.com/database/technologies/xe-downloads.html

## Configuración de la Base de Datos

### 1. Instalar Oracle Database

Si aún no tienes Oracle Database instalado:
- Descarga e instala Oracle Database XE
- Durante la instalación, configura una contraseña para el usuario SYSTEM

### 2. Crear Usuario de la Base de Datos

Conectarse a Oracle con SQLPlus o SQL Developer como SYSTEM y ejecutar:

```sql
CREATE USER USUARIO_BD IDENTIFIED BY PASSWORD_BD;
GRANT CONNECT, RESOURCE TO USUARIO_BD;
GRANT UNLIMITED TABLESPACE TO USUARIO_BD;
```

### 3. Ejecutar Scripts SQL

Conectarse con el usuario creado y ejecutar en orden:

```bash
# 1. Crear las tablas y secuencias
@sql/01_crear_tablas.sql

# 2. Insertar datos de prueba
@sql/02_insertar_datos.sql

# 3. (Opcional) Probar consultas
@sql/03_consultas.sql
```

## Configuración de la Aplicación

### 1. Configurar Base de Datos

Editar el archivo `src/main/resources/application.properties`:

```properties
# Configurar la URL de tu base de datos Oracle
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=USUARIO_BD
spring.datasource.password=PASSWORD_BD
```

Modificar según tu configuración:
- `localhost`: Host de tu base de datos
- `1521`: Puerto de Oracle (por defecto)
- `XE`: Service Name o SID
- `USUARIO_BD`: Tu usuario de base de datos
- `PASSWORD_BD`: Tu contraseña

### 2. Instalar Dependencias

```bash
cd tarjetas-api
mvn clean install
```

## Ejecución de la Aplicación

### Opción 1: Usando Maven

```bash
mvn spring-boot:run
```

### Opción 2: Usando JAR

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar el JAR
java -jar target/tarjetas-api-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Probar la Aplicación

### 1. Probar POO (sin necesidad de la API)

Ejecutar la clase de pruebas de POO:

```bash
mvn exec:java -Dexec.mainClass="com.prueba.tarjetas.PruebasPOO"
```

Esta clase demuestra:
- Creación de objetos con encapsulamiento
- Herencia entre clases
- Polimorfismo con el método `add()`
- Abstracción con la clase base `Tarjeta`

### 2. Probar la API REST

#### Usando cURL:

**Crear un usuario:**
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Roberto",
    "apellido": "Silva",
    "email": "roberto.silva@email.com",
    "telefono": "5551112233"
  }'
```

**Crear una tarjeta de crédito:**
```bash
curl -X POST http://localhost:8080/api/tarjetas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroTarjeta": "4532111122223333",
    "titular": "Roberto Silva",
    "fechaVencimiento": "2027-12-31",
    "usuarioId": 1,
    "tipoTarjeta": "CREDITO",
    "limiteCredito": 60000,
    "tasaInteres": 17.5
  }'
```

**Agregar saldo a una tarjeta:**
```bash
curl -X POST http://localhost:8080/api/tarjetas/1/agregar-saldo \
  -H "Content-Type: application/json" \
  -d '{
    "monto": 5000.00
  }'
```

**Obtener todas las tarjetas de un usuario:**
```bash
curl http://localhost:8080/api/tarjetas/usuario/1
```

#### Usando Postman o Thunder Client:

1. Importar la colección desde: (crear archivo JSON con los endpoints)
2. Configurar la URL base: `http://localhost:8080`
3. Ejecutar las peticiones de la colección

### 3. Verificar Validaciones

La API incluye validaciones automáticas. Prueba estos casos:

**Email inválido:**
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "User",
    "email": "email-invalido",
    "telefono": "5551234567"
  }'
```

**Teléfono con formato incorrecto:**
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "User",
    "email": "test@email.com",
    "telefono": "123"
  }'
```

## Ejemplos de Respuestas

### Respuesta Exitosa

```json
{
  "correct": true,
  "errorMessage": null,
  "ex": null,
  "object": {
    "id": 1,
    "nombre": "Roberto",
    "apellido": "Silva",
    "email": "roberto.silva@email.com",
    "telefono": "5551112233",
    "activo": true
  },
  "objects": null
}
```

### Respuesta con Error

```json
{
  "correct": false,
  "errorMessage": "Ya existe un usuario con ese email",
  "ex": null,
  "object": null,
  "objects": null
}
```

## Arquitectura y Patrones

- **Arquitectura en Capas**: Controller -> Service -> Repository -> Model
- **DTO Pattern**: Separación entre entidades y objetos de transferencia
- **Repository Pattern**: Uso de Spring Data JPA
- **Dependency Injection**: Inyección de dependencias con Spring
- **Result Pattern**: Estandarización de respuestas con la clase `Result`

## Validaciones Implementadas

- Email válido y único
- Teléfono de 10 dígitos
- Números de tarjeta únicos
- Montos positivos
- Límites de crédito obligatorios para tarjetas de crédito
- Fechas de vencimiento válidas

## Consultas SQL Destacadas

El proyecto incluye 15 consultas SQL que demuestran:
- Joins entre tablas
- Agregaciones (COUNT, SUM, AVG)
- Subconsultas
- Filtros complejos
- Agrupaciones
- Análisis de datos

## Solución de Problemas

### Error de conexión a la base de datos

**Problema**: `Unable to create initial connections of pool`

**Solución**:
1. Verificar que Oracle Database esté en ejecución
2. Confirmar usuario y contraseña en `application.properties`
3. Verificar que el puerto 1521 esté abierto
4. Comprobar el Service Name/SID

### Error de secuencias no encontradas

**Problema**: `Sequence "USUARIO_SEQ" not found`

**Solución**:
1. Ejecutar el script `01_crear_tablas.sql`
2. Verificar que estás conectado con el usuario correcto
3. Revisar que las secuencias se crearon exitosamente

### Puerto 8080 ya en uso

**Problema**: `Port 8080 was already in use`

**Solución**:
1. Cambiar el puerto en `application.properties`: `server.port=8081`
2. O detener el proceso que usa el puerto 8080

## Autor

Proyecto desarrollado como prueba técnica para demostrar:
- Programación Orientada a Objetos
- Desarrollo de APIs REST
- Diseño de bases de datos relacionales

## Licencia

Este proyecto es de uso educativo y de evaluación.

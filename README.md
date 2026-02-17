# 💳 Usuario - Gestión de Tarjetas

## 📋 Descripción

Flujo básico de la entidad **Usuario** que gestiona una lista de tarjetas. La clase `Tarjeta` es una clase abstracta que comparte sus atributos con las siguientes implementaciones:

- `TarjetaDebito`
- `TarjetaCredito`
- `TarjetaNomina`

---

## 🗂️ Diagrama de clases

```
Usuario
 └── List<Tarjeta>
        ├── TarjetaDebito
        ├── TarjetaCredito
        └── TarjetaNomina
```

---

## ⚙️ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:

| Herramienta | Versión recomendada |
|-------------|---------------------|
| Java        | 17                  |
| Maven       | 3.8+                |
| Oracle DB   | 19c / 21c           |

---

## 🚀 Instalación y configuración

### 1. Clonar el repositorio

```bash
git clonehttps://github.com/mateom02/YaGanastePruebaTecnica.git
cd YaGanastePruebaTecnica
```

### 2. Configurar la base de datos

Edita el archivo `src/main/resources/application.properties` (o `application.yml`) con tus credenciales de Oracle:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

Crea un usuario en SQL
'''sql 
CONN/AS SYSDBA
ALTER SESSION SET "_ORACLE_SCRIPT"=TRUE
CREATE USER TuUsuario IDENTIFIED BY tuPassword
GRANT DBA TO TuUsuario
'''

Copia, pega y ejecuta el contenido de PruebaTecnicaYaGanaste.txt

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar el proyecto

```bash
mvn spring-boot:run
```

---

## 📄 Documentación de la API (Swagger)

Una vez que el proyecto esté en ejecución, puedes acceder a la documentación interactiva de los endpoints en:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger UI puedes explorar y probar todos los endpoints disponibles para la gestión de usuarios y tarjetas.

---


## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot**
- **Maven**
- **Oracle Database**
- **Swagger / OpenAPI 3**
- **JPA / Hibernate**

---


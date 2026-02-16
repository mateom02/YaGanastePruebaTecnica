-- =====================================================
-- Script de Consultas SQL
-- Sistema de Gestión de Usuarios y Tarjetas
-- =====================================================

-- =====================================================
-- CONSULTAS BÁSICAS
-- =====================================================

-- 1. Obtener todos los usuarios activos
SELECT ID, NOMBRE, APELLIDO, EMAIL, TELEFONO
FROM USUARIOS
WHERE ACTIVO = 1
ORDER BY APELLIDO, NOMBRE;

-- 2. Obtener todas las tarjetas de un usuario específico
SELECT T.ID, T.TIPO_TARJETA, T.NUMERO_TARJETA, T.TITULAR, 
       T.FECHA_VENCIMIENTO, T.SALDO, T.ACTIVA
FROM TARJETAS T
WHERE T.USUARIO_ID = 1
ORDER BY T.TIPO_TARJETA;

-- 3. Obtener usuarios con su cantidad de tarjetas
SELECT U.ID, U.NOMBRE, U.APELLIDO, U.EMAIL, 
       COUNT(T.ID) AS CANTIDAD_TARJETAS
FROM USUARIOS U
LEFT JOIN TARJETAS T ON U.ID = T.USUARIO_ID
GROUP BY U.ID, U.NOMBRE, U.APELLIDO, U.EMAIL
ORDER BY CANTIDAD_TARJETAS DESC;

-- =====================================================
-- CONSULTAS CON FILTROS Y CONDICIONES
-- =====================================================

-- 4. Obtener tarjetas de crédito con saldo disponible mayor a 40,000
SELECT T.ID, T.NUMERO_TARJETA, T.TITULAR, T.SALDO, T.LIMITE_CREDITO,
       U.NOMBRE || ' ' || U.APELLIDO AS PROPIETARIO
FROM TARJETAS T
INNER JOIN USUARIOS U ON T.USUARIO_ID = U.ID
WHERE T.TIPO_TARJETA = 'CREDITO'
  AND T.SALDO > 40000
  AND T.ACTIVA = 1
ORDER BY T.SALDO DESC;

-- 5. Obtener tarjetas de débito que permiten sobregiro
SELECT T.ID, T.NUMERO_TARJETA, T.TITULAR, T.SALDO, 
       T.PERMITE_SOBREGIRO, T.LIMITE_SOBREGIRO,
       U.EMAIL AS EMAIL_PROPIETARIO
FROM TARJETAS T
INNER JOIN USUARIOS U ON T.USUARIO_ID = U.ID
WHERE T.TIPO_TARJETA = 'DEBITO'
  AND T.PERMITE_SOBREGIRO = 1
  AND T.ACTIVA = 1;

-- 6. Obtener tarjetas de nómina ordenadas por depósito mensual
SELECT T.ID, T.NUMERO_TARJETA, T.TITULAR, T.EMPRESA, 
       T.NUMERO_EMPLEADO, T.DEPOSITO_MENSUAL, T.SALDO
FROM TARJETAS T
WHERE T.TIPO_TARJETA = 'NOMINA'
  AND T.ACTIVA = 1
ORDER BY T.DEPOSITO_MENSUAL DESC;

-- =====================================================
-- CONSULTAS CON AGREGACIONES
-- =====================================================

-- 7. Saldo total por tipo de tarjeta
SELECT TIPO_TARJETA, 
       COUNT(*) AS CANTIDAD,
       SUM(SALDO) AS SALDO_TOTAL,
       AVG(SALDO) AS SALDO_PROMEDIO,
       MIN(SALDO) AS SALDO_MINIMO,
       MAX(SALDO) AS SALDO_MAXIMO
FROM TARJETAS
WHERE ACTIVA = 1
GROUP BY TIPO_TARJETA
ORDER BY SALDO_TOTAL DESC;

-- 8. Estadísticas por usuario
SELECT U.ID, 
       U.NOMBRE || ' ' || U.APELLIDO AS NOMBRE_COMPLETO,
       COUNT(T.ID) AS TOTAL_TARJETAS,
       SUM(CASE WHEN T.TIPO_TARJETA = 'CREDITO' THEN 1 ELSE 0 END) AS TARJETAS_CREDITO,
       SUM(CASE WHEN T.TIPO_TARJETA = 'DEBITO' THEN 1 ELSE 0 END) AS TARJETAS_DEBITO,
       SUM(CASE WHEN T.TIPO_TARJETA = 'NOMINA' THEN 1 ELSE 0 END) AS TARJETAS_NOMINA,
       SUM(T.SALDO) AS SALDO_TOTAL
FROM USUARIOS U
LEFT JOIN TARJETAS T ON U.ID = T.USUARIO_ID AND T.ACTIVA = 1
WHERE U.ACTIVO = 1
GROUP BY U.ID, U.NOMBRE, U.APELLIDO
ORDER BY SALDO_TOTAL DESC;

-- =====================================================
-- CONSULTAS COMPLEJAS CON MÚLTIPLES JOINS
-- =====================================================

-- 9. Reporte completo de usuarios con sus tarjetas
SELECT U.ID AS USUARIO_ID,
       U.NOMBRE || ' ' || U.APELLIDO AS USUARIO,
       U.EMAIL,
       U.TELEFONO,
       T.ID AS TARJETA_ID,
       T.TIPO_TARJETA,
       T.NUMERO_TARJETA,
       T.SALDO,
       CASE 
           WHEN T.TIPO_TARJETA = 'CREDITO' THEN T.LIMITE_CREDITO
           WHEN T.TIPO_TARJETA = 'DEBITO' AND T.PERMITE_SOBREGIRO = 1 THEN T.LIMITE_SOBREGIRO
           ELSE NULL
       END AS LIMITE_ADICIONAL,
       T.ACTIVA AS TARJETA_ACTIVA
FROM USUARIOS U
LEFT JOIN TARJETAS T ON U.ID = T.USUARIO_ID
WHERE U.ACTIVO = 1
ORDER BY U.APELLIDO, U.NOMBRE, T.TIPO_TARJETA;

-- 10. Usuarios con tarjetas próximas a vencer (6 meses)
SELECT U.NOMBRE || ' ' || U.APELLIDO AS USUARIO,
       U.EMAIL,
       T.TIPO_TARJETA,
       T.NUMERO_TARJETA,
       T.FECHA_VENCIMIENTO,
       TRUNC(T.FECHA_VENCIMIENTO - SYSDATE) AS DIAS_PARA_VENCER
FROM USUARIOS U
INNER JOIN TARJETAS T ON U.ID = T.USUARIO_ID
WHERE T.ACTIVA = 1
  AND T.FECHA_VENCIMIENTO BETWEEN SYSDATE AND ADD_MONTHS(SYSDATE, 6)
ORDER BY T.FECHA_VENCIMIENTO;

-- =====================================================
-- CONSULTAS DE ANÁLISIS
-- =====================================================

-- 11. Top 5 usuarios con mayor saldo total
SELECT * FROM (
    SELECT U.NOMBRE || ' ' || U.APELLIDO AS USUARIO,
           U.EMAIL,
           COUNT(T.ID) AS TOTAL_TARJETAS,
           SUM(T.SALDO) AS SALDO_TOTAL
    FROM USUARIOS U
    INNER JOIN TARJETAS T ON U.ID = T.USUARIO_ID
    WHERE U.ACTIVO = 1 AND T.ACTIVA = 1
    GROUP BY U.ID, U.NOMBRE, U.APELLIDO, U.EMAIL
    ORDER BY SALDO_TOTAL DESC
)
WHERE ROWNUM <= 5;

-- 12. Tarjetas de crédito con uso mayor al 70% del límite
SELECT U.NOMBRE || ' ' || U.APELLIDO AS USUARIO,
       T.NUMERO_TARJETA,
       T.LIMITE_CREDITO,
       T.SALDO AS SALDO_DISPONIBLE,
       (T.LIMITE_CREDITO - T.SALDO) AS SALDO_USADO,
       ROUND(((T.LIMITE_CREDITO - T.SALDO) / T.LIMITE_CREDITO) * 100, 2) AS PORCENTAJE_USO
FROM TARJETAS T
INNER JOIN USUARIOS U ON T.USUARIO_ID = U.ID
WHERE T.TIPO_TARJETA = 'CREDITO'
  AND T.ACTIVA = 1
  AND ((T.LIMITE_CREDITO - T.SALDO) / T.LIMITE_CREDITO) > 0.70
ORDER BY PORCENTAJE_USO DESC;

-- 13. Distribución de tarjetas por tipo (porcentaje)
SELECT TIPO_TARJETA,
       COUNT(*) AS CANTIDAD,
       ROUND((COUNT(*) * 100.0) / SUM(COUNT(*)) OVER (), 2) AS PORCENTAJE
FROM TARJETAS
WHERE ACTIVA = 1
GROUP BY TIPO_TARJETA
ORDER BY CANTIDAD DESC;

-- =====================================================
-- CONSULTAS CON SUBCONSULTAS
-- =====================================================

-- 14. Usuarios que tienen todos los tipos de tarjetas
SELECT U.ID, U.NOMBRE || ' ' || U.APELLIDO AS USUARIO, U.EMAIL
FROM USUARIOS U
WHERE U.ACTIVO = 1
  AND (SELECT COUNT(DISTINCT T.TIPO_TARJETA)
       FROM TARJETAS T
       WHERE T.USUARIO_ID = U.ID AND T.ACTIVA = 1) = 3;

-- 15. Tarjetas con saldo superior al promedio de su tipo
SELECT T.TIPO_TARJETA,
       T.NUMERO_TARJETA,
       T.TITULAR,
       T.SALDO,
       (SELECT AVG(T2.SALDO) 
        FROM TARJETAS T2 
        WHERE T2.TIPO_TARJETA = T.TIPO_TARJETA 
          AND T2.ACTIVA = 1) AS SALDO_PROMEDIO_TIPO
FROM TARJETAS T
WHERE T.ACTIVA = 1
  AND T.SALDO > (SELECT AVG(T2.SALDO) 
                 FROM TARJETAS T2 
                 WHERE T2.TIPO_TARJETA = T.TIPO_TARJETA 
                   AND T2.ACTIVA = 1)
ORDER BY T.TIPO_TARJETA, T.SALDO DESC;

-- =====================================================
-- FIN DE LAS CONSULTAS
-- =====================================================

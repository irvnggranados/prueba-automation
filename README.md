# 🚀 Automatización de Pruebas

Este repositorio contiene pruebas automatizadas de **UI** y **API** desarrolladas en **Java + Selenium + TestNG**, con generación de reportes mediante **Allure**.

---

## 📌 Objetivo
- Validar funcionalidades de la aplicación web [SauceDemo](https://www.saucedemo.com/) y [API](https://dog.ceo/dog-api/).
- Ejecutar pruebas de interfaz gráfica (UI) y pruebas de servicios (API).
- Generar reportes de ejecución con Allure.

---

## ⚙️ Requisitos
- **Java 21**
- **Maven 3.9+**
- **Selenium 4.21**
- **TestNG**
- **Allure 2.44.0** instalado y disponible en el `PATH`.
- **Docker** (opcional, para ejecutar pruebas en contenedores).

---

## 🧪 Ejecución de pruebas

### 🔹 Pruebas de UI
Ejecuta todas las pruebas de interfaz gráfica:
```bash
mvn clean test
```
O bien, usando el archivo de suite específico:
```bash
mvn clean test "-Dsurefire.suiteXmlFiles=src/test/resources/testng.xml"
```

### 🔹 Pruebas de API
ejecutar con el comando:
```bash
mvn clean test "-Dsurefire.suiteXmlFiles=src/test/resources/testng-api.xml"
```
### ⚙️ Configuración de Headless
En qa.properties cambia de false a true:
```properties
headless=true
```


### 🐋 Dockerización
Para ejecutar las pruebas en un contenedor Docker, sigue estos pasos:
1. Construye la imagen de Docker:
```bash
docker build -t prueba-automation .
```     

2. Ejecuta el contenedor:
```bash
docker run --shm-size=2g prueba-automation 
```

### ⚠️ NOTA:
Para docker tambien debe activar el modo headless en el archivo qa.properties.; comentar elel propertie de allure actual y descomentar este:
```properties
# En Linux/Docker (si Allure está en /usr/bin/allure)
#allure.path=allure
```




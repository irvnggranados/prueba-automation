# ==========================
# Etapa de compilación
# ==========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiamos primero el pom para aprovechar la cache
COPY pom.xml .

RUN mvn dependency:go-offline

# Copiamos el resto del proyecto
COPY . .

# Compilamos sin ejecutar pruebas
RUN mvn clean test-compile

# ==========================
# Imagen final
# ==========================
FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

# Instalar dependencias necesarias para Chrome (paquetes actualizados para Debian 12)
RUN apt-get update && apt-get install -y \
    wget unzip curl gnupg \
    libnss3 libxi6 libxcursor1 \
    libxcomposite1 libxrandr2 libxtst6 \
    fonts-liberation libappindicator3-1 xdg-utils \
    libasound2t64 \
    && rm -rf /var/lib/apt/lists/*

# Instalar Google Chrome estable
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y ./google-chrome-stable_current_amd64.deb \
    && rm google-chrome-stable_current_amd64.deb

# Instalar Allure
RUN apt-get update && apt-get install -y openjdk-21-jdk \
    && wget https://github.com/allure-framework/allure2/releases/download/2.44.0/allure-2.44.0.zip \
    && unzip allure-2.44.0.zip -d /opt/ \
    && ln -s /opt/allure-2.44.0/bin/allure /usr/bin/allure \
    && rm allure-2.44.0.zip

# Copiar artefactos compilados
COPY --from=builder /app .

# Entrypoint por defecto: ejecutar pruebas
ENTRYPOINT ["mvn"]
CMD ["clean","test"]
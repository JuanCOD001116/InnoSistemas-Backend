# Usa la imagen base de Ubuntu
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

# Instala herramientas adicionales
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        postgresql-client \
        curl \
        wget \
        git \
        unzip \
        zip \
        tree \
        htop \
        nano \
        vim \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Instala Node.js para herramientas de desarrollo front-end (si las necesitas)
RUN curl -fsSL https://deb.nodesource.com/setup_lts.x | bash - \
    && apt-get install -y nodejs

# Configura el entorno Java
ENV JAVA_HOME=/usr/local/sdkman/candidates/java/current
ENV PATH=$JAVA_HOME/bin:$PATH

# Crea directorio para aplicaciones locales
RUN mkdir -p /opt/app

# Configura el usuario vscode
USER vscode

# Configura Maven para el usuario
RUN mkdir -p /home/vscode/.m2

# Configura alias útiles
RUN echo 'alias ll="ls -alF"' >> /home/vscode/.bashrc \
    && echo 'alias la="ls -A"' >> /home/vscode/.bashrc \
    && echo 'alias l="ls -CF"' >> /home/vscode/.bashrc \
    && echo 'alias mvn-spring="mvn spring-boot:run -Dmaven.test.skip=true"' >> /home/vscode/.bashrc \
    && echo 'alias mvn-clean="mvn clean install -Dmaven.test.skip=true"' >> /home/vscode/.bashrc

# Mensaje de bienvenida
RUN echo 'echo "🚀 InnoSistemas Development Environment Ready!"' >> /home/vscode/.bashrc \
    && echo 'echo "📁 Project: Spring Boot + PostgreSQL + JWT"' >> /home/vscode/.bashrc \
    && echo 'echo "🔧 Use: mvn-spring to run the application"' >> /home/vscode/.bashrc \
    && echo 'echo "📄 Don'\''t forget to configure your database credentials in application-secrets.properties"' >> /home/vscode/.bashrc

WORKDIR /workspace
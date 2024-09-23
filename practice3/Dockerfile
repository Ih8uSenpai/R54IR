FROM php:7.4-apache

# Установка необходимых пакетов и библиотек для работы с PostgreSQL
RUN apt-get update && apt-get install -y \
    libpq-dev \
    && docker-php-ext-install pdo pdo_pgsql

# Копируем конфигурацию Apache
COPY ./000-default.conf /etc/apache2/sites-available/000-default.conf
RUN a2ensite 000-default.conf
RUN a2enmod rewrite

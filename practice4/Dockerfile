FROM php:7.4-apache

# Установка необходимых пакетов для работы с PostgreSQL
RUN apt-get update && apt-get install -y \
    libpq-dev \
    && docker-php-ext-install pdo pdo_pgsql

# Копирование файлов в директорию
COPY ./web/ /var/www/html/

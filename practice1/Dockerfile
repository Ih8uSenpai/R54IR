# Базовый образ с последней версией PHP и Apache
FROM php:8.1-apache

# Установка расширений и пакетов, необходимых для работы с PostgreSQL
RUN apt-get update && apt-get install -y \
    libpq-dev \
    && docker-php-ext-install pgsql pdo_pgsql

# Копирование файлов приложения в директорию Apache
COPY ./app /var/www/html/

# Установка прав доступа
RUN chown -R www-data:www-data /var/www/html

# Открытие порта 80 для доступа к веб-серверу
EXPOSE 80

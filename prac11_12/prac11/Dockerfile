# Используй образ Python
FROM python:3.8

# Установи рабочий каталог в контейнере
WORKDIR /usr/src/app

# Копируй файлы проекта
COPY . .

# Установи зависимости
RUN pip install --no-cache-dir -r requirements.txt

# Запуск Django сервера (Эту часть мы убрали, так как она теперь в docker-compose.yml)

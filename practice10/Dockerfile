# Используем базовый образ Go
FROM golang:latest

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем go.mod и go.sum для установки зависимостей
COPY go.mod go.sum ./

# Загружаем зависимости
RUN go mod download

# Копируем весь исходный код проекта в контейнер
COPY . .

# Запускаем все три сервиса параллельно с помощью `go run`
CMD ["sh", "-c", "go run ./cmd/api-gateway/main.go & go run ./cmd/service1/main.go & go run ./cmd/service2/main.go & wait"]

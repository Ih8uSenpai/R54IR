FROM golang:latest

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . .

# Создаем директорию для логов
RUN mkdir -p /var/log

RUN go build -o /practice9 cmd/app/main.go

EXPOSE 8080

CMD ["/practice9"]

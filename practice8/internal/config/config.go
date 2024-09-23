package config

import (
	"github.com/joho/godotenv"
	"log"
)

func LoadEnv() {
	err := godotenv.Load("env/.env")
	if err != nil {
		log.Fatal("Ошибка загрузки .env файла")
	}
}

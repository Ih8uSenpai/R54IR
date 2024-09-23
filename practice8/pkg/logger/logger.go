package logger

import (
	"log"
	"os"
)

func InitLogger() *os.File {
	logFile, err := os.OpenFile("logs/app.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0666)
	if err != nil {
		log.Fatal("Ошибка создания лог файла:", err)
	}
	log.SetOutput(logFile)
	return logFile
}

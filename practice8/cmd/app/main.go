package main

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"os"
	"practice8/internal/config"
	"practice8/internal/handlers"
	"practice8/pkg/logger"
)

func main() {
	// Чтение конфигурации
	config.LoadEnv()

	// Логгер
	logFile := logger.InitLogger()
	defer logFile.Close()

	r := mux.NewRouter()

	// Маршруты
	r.HandleFunc("/api/save/linear", handlers.SaveDataLinear).Methods("POST")
	r.HandleFunc("/api/save/concurrent", handlers.SaveDataConcurrent).Methods("POST")
	r.HandleFunc("/api/get", handlers.GetData).Methods("GET")

	// Запуск сервера
	log.Println("Сервер запущен на порту:", os.Getenv("PORT"))
	if err := http.ListenAndServe(":"+os.Getenv("PORT"), r); err != nil {
		log.Fatal("Ошибка запуска сервера:", err)
	}
}

package main

import (
	"context"
	"encoding/json"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
	"io"
	"log"
	"net/http"
	"os"
	"practice9/internal/files"
	"practice9/pkg/mongodb"
)

func setupLogging() {
	logFile, err := os.OpenFile("/var/log/practice9.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Fatalf("Failed to open log file: %v", err)
	}

	mw := io.MultiWriter(os.Stdout, logFile)
	log.SetOutput(mw)
	log.Println("Logging setup complete.")
}

func main() {
	err := godotenv.Load("configs/.env")
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	setupLogging()

	mongodb.ConnectMongoDB()
	fileService := files.NewFileService(mongodb.DB)

	r := mux.NewRouter()

	// Загрузка файла
	r.HandleFunc("/files", func(w http.ResponseWriter, r *http.Request) {
		r.ParseMultipartForm(10 << 20)

		file, fileHeader, err := r.FormFile("file")
		if err != nil {
			log.Printf("Error getting file from request: %v", err)
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		defer file.Close()

		id, err := fileService.UploadFile(context.Background(), file, fileHeader)
		if err != nil {
			log.Printf("Error uploading file: %v", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		log.Printf("File uploaded successfully: %s", id)
		w.Write([]byte(id))
	}).Methods("POST")

	// Получение всех файлов
	r.HandleFunc("/files", func(w http.ResponseWriter, r *http.Request) {
		files, err := fileService.GetAllFiles(context.Background())
		if err != nil {
			log.Printf("Error fetching files: %v", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		log.Println("Successfully retrieved all files.")
		json.NewEncoder(w).Encode(files)
	}).Methods("GET")

	// Получение файла по id
	r.HandleFunc("/files/{id}", func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		data, err := fileService.GetFile(context.Background(), id)
		if err != nil {
			log.Printf("Error retrieving file with id %s: %v", id, err)
			http.Error(w, err.Error(), http.StatusNotFound)
			return
		}

		log.Printf("Successfully retrieved file with id %s.", id)
		w.Write(data)
	}).Methods("GET")

	// Получение информации о файле по id
	r.HandleFunc("/files/{id}/info", func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		info, err := fileService.GetFileInfo(context.Background(), id)
		if err != nil {
			log.Printf("Error retrieving file info with id %s: %v", id, err)
			http.Error(w, err.Error(), http.StatusNotFound)
			return
		}

		log.Printf("Successfully retrieved file info with id %s.", id)
		json.NewEncoder(w).Encode(info)
	}).Methods("GET")

	// Удаление файла по id
	r.HandleFunc("/files/{id}", func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		err := fileService.DeleteFile(context.Background(), id)
		if err != nil {
			log.Printf("Error deleting file with id %s: %v", id, err)
			http.Error(w, err.Error(), http.StatusNotFound)
			return
		}

		log.Printf("Successfully deleted file with id %s.", id)
		w.WriteHeader(http.StatusNoContent)
	}).Methods("DELETE")

	// Обновление файла по ID
	r.HandleFunc("/files/{id}", func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		r.ParseMultipartForm(10 << 20)

		file, fileHeader, err := r.FormFile("file")
		if err != nil {
			log.Printf("Error getting file from request: %v", err)
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		defer file.Close()

		newID, err := fileService.UpdateFile(context.Background(), id, file, fileHeader)
		if err != nil {
			log.Printf("Error updating file with id %s: %v", id, err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		log.Printf("File with id %s updated successfully. New file id: %s", id, newID)
		w.Write([]byte(newID))
	}).Methods("PUT")

	log.Println("Server started on port", os.Getenv("SERVER_PORT"))
	log.Fatal(http.ListenAndServe(":"+os.Getenv("SERVER_PORT"), r))
}

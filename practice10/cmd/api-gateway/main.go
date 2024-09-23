package main

import (
	"context"
	"log"
	"net/http"
	"os"

	pb1 "practice10/internal/service1/proto"
	pb2 "practice10/internal/service2/proto"

	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
	"google.golang.org/grpc"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	logFilePath := os.Getenv("LOG_FILE_PATH")
	logFile, err := os.OpenFile(logFilePath+"/gateway.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0666)
	if err != nil {
		log.Fatalf("Failed to open log file: %v", err)
	}
	defer logFile.Close()
	log.SetOutput(logFile)

	service1Conn, err := grpc.Dial("localhost:"+os.Getenv("SERVICE1_PORT"), grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to Service1: %v", err)
	}
	defer service1Conn.Close()

	service2Conn, err := grpc.Dial("localhost:"+os.Getenv("SERVICE2_PORT"), grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to Service2: %v", err)
	}
	defer service2Conn.Close()

	service1Client := pb1.NewTimeServiceClient(service1Conn)
	service2Client := pb2.NewGreetServiceClient(service2Conn)

	router := mux.NewRouter()

	router.HandleFunc("/time", func(w http.ResponseWriter, r *http.Request) {
		resp, err := service1Client.GetCurrentTime(context.Background(), &pb1.TimeRequest{})
		if err != nil {
			log.Printf("Error calling GetCurrentTime: %v", err)
			http.Error(w, "Error calling Service1", http.StatusInternalServerError)
			return
		}
		w.Write([]byte(resp.CurrentTime))
	})

	router.HandleFunc("/greet", func(w http.ResponseWriter, r *http.Request) {
		name := r.URL.Query().Get("name")
		if name == "" {
			name = "Guest"
		}
		resp, err := service2Client.Greet(context.Background(), &pb2.GreetRequest{Name: name})
		if err != nil {
			log.Printf("Error calling Greet: %v", err)
			http.Error(w, "Error calling Service2", http.StatusInternalServerError)
			return
		}
		w.Write([]byte(resp.Message))
	})

	log.Println("API Gateway listening on port", os.Getenv("API_GATEWAY_PORT"))
	log.Fatal(http.ListenAndServe(":"+os.Getenv("API_GATEWAY_PORT"), router))
}

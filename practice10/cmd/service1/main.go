package main

import (
    "context"
    "log"
    "net"
    "os"
    "time"

    "google.golang.org/grpc"
    pb "practice10/internal/service1/proto"
    "github.com/joho/godotenv"
)

type server struct {
    pb.UnimplementedTimeServiceServer
}

func (s *server) GetCurrentTime(ctx context.Context, in *pb.TimeRequest) (*pb.TimeResponse, error) {
    log.Println("Received request for current time")
    currentTime := time.Now().Format(time.RFC3339)
    return &pb.TimeResponse{CurrentTime: currentTime}, nil
}

func main() {
    err := godotenv.Load()
    if err != nil {
        log.Fatalf("Error loading .env file: %v", err)
    }

    port := os.Getenv("SERVICE1_PORT")
    logFilePath := os.Getenv("LOG_FILE_PATH")

    logFile, err := os.OpenFile(logFilePath+"/service1.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0666)
    if err != nil {
        log.Fatalf("Failed to open log file: %v", err)
    }
    defer logFile.Close()
    log.SetOutput(logFile)

    lis, err := net.Listen("tcp", ":"+port)
    if err != nil {
        log.Fatalf("Failed to listen: %v", err)
    }

    grpcServer := grpc.NewServer()
    pb.RegisterTimeServiceServer(grpcServer, &server{})
    log.Printf("Service1 listening on port %s", port)
    if err := grpcServer.Serve(lis); err != nil {
        log.Fatalf("Failed to serve: %v", err)
    }
}

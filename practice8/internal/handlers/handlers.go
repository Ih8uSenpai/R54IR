package handlers

import (
	"encoding/json"
	"github.com/gorilla/securecookie"
	"log"
	"net/http"
	"time"
)

var hashKey = []byte("very-secret-key") // Для шифрования
var s = securecookie.New(hashKey, nil)  // Создаем новый secure cookie

type Request struct {
	Input string `json:"input"`
}

type Response struct {
	Output string `json:"output"`
}

// Линейная версия сохранения данных с задержкой
func SaveDataLinear(w http.ResponseWriter, r *http.Request) {
	var req Request
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		log.Println("Ошибка декодирования JSON:", err)
		http.Error(w, "Неверный формат данных", http.StatusBadRequest)
		return
	}

	// Симулируем долгую обработку
	log.Println("Линейная обработка: начало")
	time.Sleep(2 * time.Second) // Задержка для демонстрации линейной обработки
	log.Println("Линейная обработка: завершена")

	// Подготовка ответа
	resp := Response{Output: "Received: " + req.Input}

	// Сериализация и шифрование cookie
	if encoded, err := s.Encode("user-data", resp); err == nil {
		cookie := &http.Cookie{
			Name:  "user-data",
			Value: encoded,
			Path:  "/",
		}
		http.SetCookie(w, cookie)
		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(resp)
	} else {
		log.Println("Ошибка шифрования cookie:", err)
		http.Error(w, "Ошибка на сервере", http.StatusInternalServerError)
	}
}

func SaveDataConcurrent(w http.ResponseWriter, r *http.Request) {
	var req Request
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		log.Println("Ошибка декодирования JSON:", err)
		http.Error(w, "Неверный формат данных", http.StatusBadRequest)
		return
	}

	done := make(chan bool) // Канал для синхронизации выполнения горутины

	go func(req Request) {
		log.Println("Конкурентная обработка: начало")
		time.Sleep(2 * time.Second) // Эмуляция долгой обработки
		log.Println("Конкурентная обработка: завершена")

		// Подготовка нового значения для cookie
		resp := Response{Output: "Received: " + req.Input}

		// Сериализация и шифрование cookie
		if encoded, err := s.Encode("user-data", resp); err == nil {
			cookie := &http.Cookie{
				Name:  "user-data",
				Value: encoded,
				Path:  "/",
			}
			// Обновляем cookie
			http.SetCookie(w, cookie)
			done <- true // Сигнал о завершении работы горутины
		} else {
			log.Println("Ошибка шифрования cookie:", err)
			done <- false // Сигнал о неудачной обработке
		}
	}(req)

	log.Println("Код после запуска конкурентной обработки продолжает работу")
	// Ждем завершения работы горутины
	success := <-done

	if success {
		log.Println("Ответ отправляется клиенту")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("Конкурентная обработка завершена и данные обновлены"))
	} else {
		log.Println("Ошибка обработки данных")
		http.Error(w, "Ошибка на сервере", http.StatusInternalServerError)
	}
}

// Получаем данные из cookie
func GetData(w http.ResponseWriter, r *http.Request) {
	if cookie, err := r.Cookie("user-data"); err == nil {
		var resp Response
		if err = s.Decode("user-data", cookie.Value, &resp); err == nil {
			w.WriteHeader(http.StatusOK)
			json.NewEncoder(w).Encode(resp)
		} else {
			log.Println("Ошибка расшифровки cookie:", err)
			http.Error(w, "Ошибка на сервере", http.StatusInternalServerError)
		}
	} else {
		log.Println("Ошибка чтения cookie:", err)
		http.Error(w, "Нет сохраненных данных", http.StatusBadRequest)
	}
}

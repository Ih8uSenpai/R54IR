package files

import (
	"context"
	"io"
	"mime/multipart"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
)

type FileService struct {
	db *mongo.Database
}

func NewFileService(db *mongo.Database) *FileService {
	return &FileService{db: db}
}

// Загрузка файла
func (s *FileService) UploadFile(ctx context.Context, file multipart.File, fileHeader *multipart.FileHeader) (string, error) {
	bucket, err := gridfs.NewBucket(s.db)
	if err != nil {
		return "", err
	}

	uploadStream, err := bucket.OpenUploadStream(fileHeader.Filename)
	if err != nil {
		return "", err
	}
	defer uploadStream.Close()

	_, err = io.Copy(uploadStream, file)
	if err != nil {
		return "", err
	}

	// Возвращаем ID загруженного файла
	return uploadStream.FileID.(primitive.ObjectID).Hex(), nil
}

// Получение файла по ID
func (s *FileService) GetFile(ctx context.Context, id string) ([]byte, error) {
	bucket, err := gridfs.NewBucket(s.db)
	if err != nil {
		return nil, err
	}

	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}

	downloadStream, err := bucket.OpenDownloadStream(objectID)
	if err != nil {
		return nil, err
	}
	defer downloadStream.Close()

	buf := make([]byte, downloadStream.GetFile().Length)
	_, err = downloadStream.Read(buf)
	if err != nil && err != io.EOF {
		return nil, err
	}

	return buf, nil
}

// Получение информации о файле
func (s *FileService) GetFileInfo(ctx context.Context, id string) (gridfs.File, error) {
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return gridfs.File{}, err
	}

	var result gridfs.File
	err = s.db.Collection("fs.files").FindOne(ctx, bson.M{"_id": objectID}).Decode(&result)
	if err != nil {
		return gridfs.File{}, err
	}

	return result, nil
}

// Удаление файла
func (s *FileService) DeleteFile(ctx context.Context, id string) error {
	bucket, err := gridfs.NewBucket(s.db)
	if err != nil {
		return err
	}

	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}

	return bucket.Delete(objectID)
}

// Получение всех файлов
func (s *FileService) GetAllFiles(ctx context.Context) ([]bson.M, error) {
	cursor, err := s.db.Collection("fs.files").Find(ctx, bson.M{})
	if err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)

	var files []bson.M
	if err = cursor.All(ctx, &files); err != nil {
		return nil, err
	}

	return files, nil
}

// Обновление файла по ID
func (s *FileService) UpdateFile(ctx context.Context, id string, file multipart.File, fileHeader *multipart.FileHeader) (string, error) {
	// Удаление старого файла
	err := s.DeleteFile(ctx, id)
	if err != nil {
		return "", err
	}

	// Загрузка нового файла
	newFileID, err := s.UploadFile(ctx, file, fileHeader)
	if err != nil {
		return "", err
	}

	return newFileID, nil
}

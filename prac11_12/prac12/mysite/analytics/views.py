from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Book, Telephone, WashingMachine
from .utils import generate_chart
import json
from hashlib import md5

@csrf_exempt
def upload_data(request, model, model_class):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
        except json.JSONDecodeError:
            return JsonResponse({'error': 'Invalid JSON'}, status=400)

        hash_key = md5(str(data).encode()).hexdigest()

        # Проверка на существующий график
        chart_path = f'media/charts/{hash_key}.png'
        try:
            with open(chart_path, 'rb') as f:
                return HttpResponse(f.read(), content_type="image/png")
        except IOError:
            pass

        # Обработка и сохранение данных в БД
        for item in data:
            model_class.objects.create(**item)

        # Генерация графика
        generate_chart(data, chart_path)

        return HttpResponse(open(chart_path, 'rb').read(), content_type="image/png")
    else:
        return JsonResponse({'error': 'Invalid request'}, status=400)

@csrf_exempt
def books(request):
    return upload_data(request, Book, Book)


@csrf_exempt
def telephones(request):
    return upload_data(request, Telephone, Telephone)

@csrf_exempt
def washing_machines(request):
    return upload_data(request, WashingMachine, WashingMachine)

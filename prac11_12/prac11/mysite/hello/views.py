from django.http import HttpResponse

def index(request, username='World'):
    return HttpResponse(f"Hello, {username}")
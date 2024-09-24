const form = document.querySelector('form');
form.addEventListener('submit', async (event) => {
    event.preventDefault(); // отменяем стандартное поведение формы

    const username = form.elements.username.value;
    const password = form.elements.password.value;

    // отправляем post запрос на сервер
    const response = await fetch('http://localhost:8081/auth/register', {
        method: 'POST',
        headers: {
            "Access-Control-Allow-Headers" : "Content-Type",
            "Access-Control-Allow-Origin": "*",
            'Content-Type': 'application/json',
            "Access-Control-Allow-Methods": "OPTIONS,POST,GET,PATCH"
        },
        body: JSON.stringify({ username, password })
    });

    // получаем ответ от сервера



    const data = await response.json();
    
    // сохраняем данные в sessionStorage
    sessionStorage.setItem('token', data.token);
    sessionStorage.setItem('role', data.role);
    sessionStorage.setItem('username', data.username);
    sessionStorage.setItem('password', password);
    // перенаправляем пользователя на другую страницу
    location.href = '/books';
});

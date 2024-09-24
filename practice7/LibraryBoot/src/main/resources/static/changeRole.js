
async function myFunction() {
    const username = sessionStorage.getItem('username');
    const password = sessionStorage.getItem('password');

    // отправляем post запрос на сервер
    const response = await fetch('http://localhost:8081/auth/registerReader', {
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

    // перенаправляем пользователя на другую страницу
    location.href = '/books';
}

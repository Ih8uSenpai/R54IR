<html lang="en">
<head>
    <title>Hello world page</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<h1>Таблица пользователей данного продукта</h1>
<table>
    <tr><th>Id</th><th>Name</th><th>Surname</th></tr>
    <?php
    // Подключение к базе данных PostgreSQL
    $connection_string = "host=db dbname=appdb user=user password=password";
    $conn = pg_connect($connection_string);

    if (!$conn) {
        echo "Ошибка подключения к базе данных.";
        exit;
    }

    // Выполнение запроса
    $result = pg_query($conn, "SELECT * FROM users");

    if (!$result) {
        echo "Ошибка выполнения запроса.";
        exit;
    }

    // Вывод данных
    while ($row = pg_fetch_assoc($result)) {
        echo "<tr><td>{$row['id']}</td><td>{$row['name']}</td><td>{$row['surname']}</td></tr>";
    }

    // Закрытие соединения
    pg_close($conn);
    ?>
</table>
<?php
phpinfo();
?>
</body>
</html>

<?php
$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));
$result = $mysqli->query("SELECT * FROM products");
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Маркетплейс</title>
</head>
<body>
<h1>Добро пожаловать в наш маркетплейс!</h1>
<p>Список товаров:</p>


<table>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Цена</th>
    </tr>
    <?php foreach ($result as $row): ?>
        <tr>
            <td><?= $row['id'] ?></td>
            <td><?= $row['name'] ?></td>
            <td><?= $row['price'] ?></td>
        </tr>
    <?php endforeach; ?>
</table>

</body>
</html>

<?php
$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'];
    $price = $_POST['price'];

    $stmt = $mysqli->prepare("INSERT INTO products (name, price) VALUES (?, ?)");
    $stmt->execute([$name, $price]);
}

?>

<form method="post">
    <input type="text" name="name" placeholder="Название продукта" required>
    <input type="number" name="price" placeholder="Цена" required>
    <button type="submit">Создать</button>
</form>

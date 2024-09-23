<?php
$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = $_POST['id'];
    $name = $_POST['name'];
    $price = $_POST['price'];

    $stmt = $mysqli->prepare("UPDATE products SET name=?, price=? WHERE id=?");
    $stmt->execute([$name, $price, $id]);
}

$result = $mysqli->query("SELECT * FROM products");
?>

<form method="post">
    <input type="number" name="id" placeholder="ID продукта" required>
    <input type="text" name="name" placeholder="Новое название" required>
    <input type="number" name="price" placeholder="Новая цена" required>
    <button type="submit">Обновить</button>
</form>

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

<?php
$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = $_POST['id'];

    $stmt = $mysqli->prepare("DELETE FROM products WHERE id=?");
    $stmt->execute([$id]);
}

$result = $mysqli->query("SELECT * FROM products");
?>

<form method="post">
    <input type="number" name="id" placeholder="ID продукта для удаления" required>
    <button type="submit">Удалить</button>
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

<?php
$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));
$result = $mysqli->query("SELECT * FROM products");
?>

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

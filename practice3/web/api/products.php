<?php
header("Content-Type: application/json");

$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));

$requestMethod = $_SERVER['REQUEST_METHOD'];

switch ($requestMethod) {
    case 'GET':
        if (isset($_GET['id'])) {
            $id = $_GET['id'];
            $stmt = $mysqli->prepare("SELECT * FROM products WHERE id = ?");
            $stmt->execute([$id]);
            $product = $stmt->fetch(PDO::FETCH_ASSOC);
            echo json_encode($product);
        } else {
            $stmt = $mysqli->query("SELECT * FROM products");
            $products = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($products);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);
        $name = $data['name'];
        $price = $data['price'];
        $stmt = $mysqli->prepare("INSERT INTO products (name, price) VALUES (?, ?)");
        $stmt->execute([$name, $price]);
        echo json_encode(['status' => 'success', 'message' => 'Product created']);
        break;

    case 'PUT':
        $data = json_decode(file_get_contents("php://input"), true);
        $id = $data['id'];
        $name = $data['name'];
        $price = $data['price'];
        $stmt = $mysqli->prepare("UPDATE products SET name=?, price=? WHERE id=?");
        $stmt->execute([$name, $price, $id]);
        echo json_encode(['status' => 'success', 'message' => 'Product updated']);
        break;

    case 'DELETE':
        $id = $_GET['id'];
        $stmt = $mysqli->prepare("DELETE FROM products WHERE id=?");
        $stmt->execute([$id]);
        echo json_encode(['status' => 'success', 'message' => 'Product deleted']);
        break;

    default:
        http_response_code(405);
        echo json_encode(['status' => 'error', 'message' => 'Method not allowed']);
        break;
}
?>

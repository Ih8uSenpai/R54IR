<?php
header("Content-Type: application/json");

$mysqli = new PDO("pgsql:host=" . getenv('DB_HOST') . ";dbname=" . getenv('DB_NAME'), getenv('DB_USER'), getenv('DB_PASSWORD'));

$requestMethod = $_SERVER['REQUEST_METHOD'];

switch ($requestMethod) {
    case 'GET':
        if (isset($_GET['id'])) {
            $id = $_GET['id'];
            $stmt = $mysqli->prepare("SELECT * FROM orders WHERE id = ?");
            $stmt->execute([$id]);
            $order = $stmt->fetch(PDO::FETCH_ASSOC);
            echo json_encode($order);
        } else {
            $stmt = $mysqli->query("SELECT * FROM orders");
            $orders = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($orders);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);
        $product_id = $data['product_id'];
        $quantity = $data['quantity'];
        $stmt = $mysqli->prepare("INSERT INTO orders (product_id, quantity) VALUES (?, ?)");
        $stmt->execute([$product_id, $quantity]);
        echo json_encode(['status' => 'success', 'message' => 'Order created']);
        break;

    case 'PUT':
        $data = json_decode(file_get_contents("php://input"), true);
        $id = $data['id'];
        $product_id = $data['product_id'];
        $quantity = $data['quantity'];
        $stmt = $mysqli->prepare("UPDATE orders SET product_id=?, quantity=? WHERE id=?");
        $stmt->execute([$product_id, $quantity, $id]);
        echo json_encode(['status' => 'success', 'message' => 'Order updated']);
        break;

    case 'DELETE':
        $id = $_GET['id'];
        $stmt = $mysqli->prepare("DELETE FROM orders WHERE id=?");
        $stmt->execute([$id]);
        echo json_encode(['status' => 'success', 'message' => 'Order deleted']);
        break;

    default:
        http_response_code(405);
        echo json_encode(['status' => 'error', 'message' => 'Method not allowed']);
        break;
}
?>

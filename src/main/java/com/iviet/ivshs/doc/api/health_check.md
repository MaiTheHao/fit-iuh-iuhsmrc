# Health Check Module

## API Documentation for System Health (v1)

**Base URL:** `/api/v1`

---

### 1. Client Health Resources

#### GET /clients/{clientId}/health

> Kiểm tra trạng thái sức khỏe của một Client (Gateway/Device) cụ thể dựa trên ID.

**Tham số Đường dẫn (Path Parameters)**

| Tên      | Loại | Mô tả                                  | Bắt buộc |
| -------- | ---- | -------------------------------------- | -------- |
| clientId | Long | ID định danh của Client trong hệ thống | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": {
		"roomCode": 101,
		"ipAddress": "192.168.1.10",
		"devices": [
			{
				"naturalId": "LIGHT-001",
				"category": "LIGHT",
				"controlType": "RELAY",
				"bleMac": "AA:BB:CC:DD:EE:FF",
				"gpioPin": 12,
				"isActive": true
			}
		]
	},
	"timestamp": "2024-06-07T09:00:00Z"
}
```

---

#### GET /clients/health

> Kiểm tra trạng thái sức khỏe của Client thông qua địa chỉ IP.

**Tham số Truy vấn (Query Parameters)**

| Tên | Loại   | Mô tả                                | Bắt buộc |
| --- | ------ | ------------------------------------ | -------- |
| ip  | String | Địa chỉ IP của thiết bị cần kiểm tra | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": {
		"roomCode": 101,
		"ipAddress": "192.168.1.10",
		"devices": [
			{
				"naturalId": "LIGHT-001",
				"category": "LIGHT",
				"controlType": "RELAY",
				"bleMac": "AA:BB:CC:DD:EE:FF",
				"gpioPin": 12,
				"isActive": true
			}
		]
	},
	"timestamp": "2024-06-07T09:05:00Z"
}
```

---

#### GET /clients/{clientId}/health-score

> Lấy điểm số sức khỏe (Health Score) của Client. Điểm số từ 0 đến 100 dựa trên tỉ lệ thiết bị con hoạt động.

**Tham số Đường dẫn (Path Parameters)**

| Tên      | Loại | Mô tả                   | Bắt buộc |
| -------- | ---- | ----------------------- | -------- |
| clientId | Long | ID định danh của Client | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": 95,
	"timestamp": "2024-06-07T09:10:00Z"
}
```

---

### 2. Room Health Resources

#### GET /rooms/{roomId}/health

> Kiểm tra sức khỏe toàn bộ các Gateway/Client nằm trong một phòng cụ thể theo ID phòng.

**Tham số Đường dẫn (Path Parameters)**

| Tên    | Loại | Mô tả                     | Bắt buộc |
| ------ | ---- | ------------------------- | -------- |
| roomId | Long | ID của phòng cần kiểm tra | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": {
		"192.168.1.10": {
			"roomCode": 101,
			"ipAddress": "192.168.1.10",
			"devices": [
				{
					"naturalId": "LIGHT-001",
					"category": "LIGHT",
					"controlType": "RELAY",
					"bleMac": "AA:BB:CC:DD:EE:FF",
					"gpioPin": 12,
					"isActive": true
				}
			]
		},
		"192.168.1.11": {
			"roomCode": 101,
			"ipAddress": "192.168.1.11",
			"devices": []
		}
	},
	"timestamp": "2024-06-07T09:15:00Z"
}
```

---

#### GET /rooms/health

> Kiểm tra sức khỏe toàn bộ các thiết bị trong phòng dựa trên Mã phòng (Room Code).

**Tham số Truy vấn (Query Parameters)**

| Tên  | Loại   | Mô tả                  | Bắt buộc |
| ---- | ------ | ---------------------- | -------- |
| code | String | Mã định danh của phòng | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": {
		"192.168.1.10": {
			"roomCode": 101,
			"ipAddress": "192.168.1.10",
			"devices": [
				{
					"naturalId": "LIGHT-001",
					"category": "LIGHT",
					"controlType": "RELAY",
					"bleMac": "AA:BB:CC:DD:EE:FF",
					"gpioPin": 12,
					"isActive": true
				}
			]
		}
	},
	"timestamp": "2024-06-07T09:20:00Z"
}
```

---

#### GET /rooms/{roomId}/health-score

> Tính toán điểm số sức khỏe trung bình của cả phòng dựa trên tất cả các Client trong phòng đó.

**Tham số Đường dẫn (Path Parameters)**

| Tên    | Loại | Mô tả                        | Bắt buộc |
| ------ | ---- | ---------------------------- | -------- |
| roomId | Long | ID của phòng cần lấy điểm số | Có       |

**Ví dụ Response (200 OK)**

```json
{
	"status": 200,
	"message": "Success",
	"data": 87,
	"timestamp": "2024-06-07T09:25:00Z"
}
```

---

### Response Models

#### HealthCheckResponseDtoV1

| Trường    | Loại   | Mô tả              |
| --------- | ------ | ------------------ |
| roomCode  | int    | Mã phòng           |
| ipAddress | string | Địa chỉ IP gateway |
| devices   | array  | Danh sách thiết bị |

#### DeviceDto

| Trường      | Loại    | Mô tả                |
| ----------- | ------- | -------------------- |
| naturalId   | string  | Mã thiết bị vật lý   |
| category    | string  | Loại thiết bị        |
| controlType | string  | Kiểu điều khiển      |
| bleMac      | string  | Địa chỉ BLE MAC      |
| gpioPin     | int     | Chân GPIO            |
| isActive    | boolean | Trạng thái hoạt động |

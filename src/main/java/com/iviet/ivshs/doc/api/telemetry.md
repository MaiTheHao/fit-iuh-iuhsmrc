# Telemetry Module

## Quản lý dữ liệu telemetry từ gateway và cảm biến

---

### POST /api/v1/telemetries/gateway/{gatewayUsername}

> Fetch dữ liệu telemetry từ gateway.

#### Tham số Đường dẫn (Path Parameters)

| Tên             | Loại   | Mô tả                 | Bắt buộc |
| :-------------- | :----- | :-------------------- | :------- |
| gatewayUsername | string | Tên đăng nhập gateway | Có       |

#### Ví dụ Response (200 OK)

```json
{
	"status": 200,
	"message": "Success",
	"data": null,
	"timestamp": "2024-06-07T09:00:00Z"
}
```

---

### POST /api/v1/telemetries/room/{roomCode}

> Fetch dữ liệu telemetry từ tất cả gateway trong phòng.

#### Tham số Đường dẫn (Path Parameters)

| Tên      | Loại   | Mô tả           | Bắt buộc |
| :------- | :----- | :-------------- | :------- |
| roomCode | string | Mã phòng (room) | Có       |

#### Ví dụ Response (200 OK)

```json
{
	"status": 200,
	"message": "Success",
	"data": null,
	"timestamp": "2024-06-07T09:00:00Z"
}
```

---

### POST /api/v1/telemetries/temperature/{naturalId}

> Fetch dữ liệu nhiệt độ từ cảm biến.

#### Tham số Đường dẫn (Path Parameters)

| Tên       | Loại   | Mô tả                 | Bắt buộc |
| :-------- | :----- | :-------------------- | :------- |
| naturalId | string | Mã định danh cảm biến | Có       |

#### Ví dụ Response (200 OK)

```json
{
	"status": 200,
	"message": "Success",
	"data": null,
	"timestamp": "2024-06-07T09:00:00Z"
}
```

---

### POST /api/v1/telemetries/power-consumption/{naturalId}

> Fetch dữ liệu tiêu thụ điện năng từ cảm biến.

#### Tham số Đường dẫn (Path Parameters)

| Tên       | Loại   | Mô tả                 | Bắt buộc |
| :-------- | :----- | :-------------------- | :------- |
| naturalId | string | Mã định danh cảm biến | Có       |

#### Ví dụ Response (200 OK)

```json
{
	"status": 200,
	"message": "Success",
	"data": null,
	"timestamp": "2024-06-07T09:00:00Z"
}
```

---

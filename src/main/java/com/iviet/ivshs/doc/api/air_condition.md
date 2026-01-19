# Air Condition API Documentation

## 1. Air Condition Management (Quản lý)

### GET /api/v1/air-conditions

> Lấy danh sách tất cả các thiết bị máy lạnh (phân trang).

**Query Parameters:**
| Tên | Loại | Mô tả | Mặc định |
| :--- | :--- | :------------------------------ | :------- |
| page | int | Trang hiện tại (bắt đầu từ 0) | 0 |
| size | int | Số lượng phần tử trên mỗi trang | 20 |

**Response (200 OK):** `ApiResponse<PaginatedResponse<AirConditionDto>>`

---

### GET /api/v1/air-conditions/room/{roomId}

> Lấy danh sách các máy lạnh thuộc về một phòng cụ thể.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------------- | :------- |
| roomId | Long | ID của phòng | Có |

**Query Parameters:**
| Tên | Loại | Mô tả | Mặc định |
| :--- | :--- | :------------------------------ | :------- |
| page | int | Trang hiện tại (bắt đầu từ 0) | 0 |
| size | int | Số lượng phần tử trên mỗi trang | 20 |

**Response (200 OK):** `ApiResponse<PaginatedResponse<AirConditionDto>>`

---

### GET /api/v1/air-conditions/{id}

> Lấy thông tin chi tiết của một máy lạnh theo ID.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------------- | :------- |
| id | Long | ID của máy lạnh | Có |

**Response (200 OK):** `ApiResponse<AirConditionDto>`

---

### POST /api/v1/air-conditions

> Tạo mới một thiết bị máy lạnh.

**Request Body (CreateAirConditionDto):**
| Tên trường | Loại | Mô tả |
| :------------- | :------ | :--------------------------------------------- |
| naturalId | string | ID định danh vật lý (Bắt buộc, Duy nhất) |
| name | string | Tên hiển thị (Bắt buộc) |
| roomId | Long | ID phòng chứa thiết bị (Bắt buộc) |
| deviceControlId| Long | ID bộ điều khiển trung tâm (Gateway) |
| langCode | string | Mã ngôn ngữ (Mặc định: vi) |
| isActive | boolean | Trạng thái kích hoạt |
| power | enum | Trạng thái nguồn (ON/OFF) |
| temperature | int | Nhiệt độ cài đặt (16-32) |

**Response (201 Created):** `ApiResponse<AirConditionDto>`

---

### PUT /api/v1/air-conditions/{id}

> Cập nhật thông tin cấu hình máy lạnh.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------------- | :------- |
| id | Long | ID của máy lạnh | Có |

**Request Body (UpdateAirConditionDto):**
| Tên trường | Loại | Mô tả |
| :------------- | :------ | :--------------------------------------------- |
| naturalId | string | ID định danh vật lý mới (nếu thay đổi) |
| name | string | Tên hiển thị mới |
| roomId | Long | Chuyển phòng |
| isActive | boolean | Bật/tắt kích hoạt hệ thống |
| ... | ... | Các thông số kỹ thuật khác |

**Response (200 OK):** `ApiResponse<AirConditionDto>`

---

### DELETE /api/v1/air-conditions/{id}

> Xóa thiết bị máy lạnh khỏi hệ thống.

**Response (204 No Content)**

---

## 2. Air Condition Control (Điều khiển)

### POST /api/v1/air-conditions/{id}/power

> Bật hoặc tắt nguồn máy lạnh.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------------- | :------- |
| id | Long | ID thiết bị | Có |

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| state | enum | Trạng thái: `ON`, `OFF` | Có |

**Response (202 Accepted):** `ApiResponse<Void>`

---

### POST /api/v1/air-conditions/{id}/temperature

> Điều chỉnh nhiệt độ máy lạnh.

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| value | int | Giá trị nhiệt độ (Min: 16, Max: 32) | Có |

---

### POST /api/v1/air-conditions/{id}/mode

> Thay đổi chế độ hoạt động (Làm lạnh, Sưởi, Hút ẩm...).

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| value | enum | Chế độ: `COOL`, `HEAT`, `DRY`, `FAN` | Có |

---

### POST /api/v1/air-conditions/{id}/fan

> Điều chỉnh tốc độ quạt gió.

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| speed | int | Tốc độ quạt (Min: 1, Max: 5) | Có |

---

### POST /api/v1/air-conditions/{id}/swing

> Điều chỉnh chế độ đảo gió.

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| state | enum | Đảo gió: `ON`, `OFF` | Có |

---

## Data Models & Wrappers

### ApiResponse (Wrapper)

Mọi phản hồi từ API đều được bọc trong cấu trúc này.

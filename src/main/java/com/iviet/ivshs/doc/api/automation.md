# Automation API Documentation

## 1. Automation Management (Cha)

### GET /api/v1/automations

> Lấy danh sách tất cả các kịch bản tự động hóa (phân trang). Nội dung không bao gồm danh sách actions.

**Query Parameters:**
| Tên | Loại | Mô tả | Mặc định |
| :--- | :--- | :------------------------------ | :------- |
| page | int | Trang hiện tại (bắt đầu từ 0) | 0 |
| size | int | Số lượng phần tử trên mỗi trang | 20 |

**Response (200 OK):** `PaginatedResponse<AutomationDto>`

---

### GET /api/v1/automations/{id}

> Lấy thông tin chi tiết của một kịch bản tự động hóa theo ID.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------------- | :------- |
| id | Long | ID của kịch bản cần lấy | Có |

**Response (200 OK):** `AutomationDto`

---

### POST /api/v1/automations

> Tạo mới một kịch bản tự động hóa.

**Request Body (CreateAutomationDto):**
| Tên trường | Loại | Mô tả |
| :------------- | :------ | :--------------------------------------------- |
| name | string | Tên kịch bản (Bắt buộc) |
| cronExpression | string | Biểu thức Cron (Bắt buộc, ví dụ: "0 18 \* \* ?") |
| isActive | boolean | Trạng thái kích hoạt (Mặc định: true) |
| description | string | Mô tả kịch bản |

**Response (201 Created):** `AutomationDto`

---

### PUT /api/v1/automations/{id}

> Cập nhật thông tin kịch bản (Tên, Cron, Mô tả...). _Lưu ý: API này không cập nhật actions._

**Request Body (UpdateAutomationDto):** Tương tự CreateAutomationDto.

**Response (200 OK):** `AutomationDto`

---

### DELETE /api/v1/automations/{id}

> Xóa kịch bản tự động hóa. Xóa Automation sẽ xóa tất cả actions con của nó.

**Response (204 No Content)**

---

### GET /api/v1/automations/active

> Lấy danh sách tất cả các kịch bản đang ở trạng thái hoạt động.

**Response (200 OK):** `List<AutomationDto>`

---

## 2. Action Management (Con)

### GET /api/v1/automations/{id}/actions

> Lấy danh sách các hành động thuộc về một kịch bản cụ thể.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :-- | :--- | :---------------- | :------- |
| id | Long | ID của Automation | Có |

**Response (200 OK):** `List<AutomationActionDto>` (Có kèm `targetName` để hiển thị).

---

### POST /api/v1/automations/{id}/actions

> Thêm một hành động mới vào kịch bản.

**Request Body (CreateAutomationActionDto):**
| Tên trường | Loại | Mô tả |
| :------------- | :----- | :--------------------------------------------- |
| targetType | enum | Loại mục tiêu: LIGHT |
| targetId | Long | ID của thiết bị mục tiêu |
| actionType | enum | Loại hành động: ON, OFF, SET_VALUE |
| parameterValue | string | Tham số (ví dụ: độ sáng 0-100) |
| executionOrder | int | Thứ tự thực hiện |

**Response (201 Created):** `AutomationActionDto`

---

### PUT /api/v1/automations/actions/{actionId}

> Cập nhật thông tin một hành động hiện có.

**Path Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :--- | :--------------- | :------- |
| actionId | Long | ID của hành động | Có |

**Request Body (UpdateAutomationActionDto):** Tương tự ActionDto.

**Response (200 OK):** `AutomationActionDto`

---

### DELETE /api/v1/automations/actions/{actionId}

> Xóa một hành động khỏi kịch bản.

**Response (204 No Content)**

---

## 3. System / Control Endpoints

### PATCH /api/v1/automations/{id}/status

> Bật hoặc tắt trạng thái hoạt động của kịch bản.

**Query Parameters:**
| Tên | Loại | Mô tả | Bắt buộc |
| :------- | :------ | :------------------------- | :------- |
| isActive | boolean | Trạng thái mới (true/false) | Có |

---

### POST /api/v1/automations/{id}/execute

> Thực thi ngay lập tức kịch bản tự động hóa (Manual Trigger) mà không cần chờ lịch Cron.

---

### POST /api/v1/automations/reload-job

> Tải lại hệ thống Scheduler (Quartz Jobs). Dùng khi cần đồng bộ lại toàn bộ lịch trình.

---

## Data Models

### AutomationDto

```json
{
	"id": 1,
	"name": "Tắt đèn buổi tối",
	"cronExpression": "0 18 * * ?",
	"isActive": true,
	"description": "Tự động tắt đèn vào lúc 18h",
	"createdAt": "2024-06-07T09:00:00Z",
	"updatedAt": "2024-06-07T09:00:00Z"
}
```

### AutomationActionDto

```json
{
	"id": 1,
	"targetType": "LIGHT",
	"targetId": 5,
	"actionType": "OFF",
	"parameterValue": null,
	"executionOrder": 0,
	"targetName": "Đèn phòng khách"
}
```

### Enum Values

- **JobTargetType**: `LIGHT`
- **JobActionType**: `ON`, `OFF`, `SET_VALUE`

---

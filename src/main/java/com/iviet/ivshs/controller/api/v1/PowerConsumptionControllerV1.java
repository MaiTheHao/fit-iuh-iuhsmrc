package com.iviet.ivshs.controller.api.v1;

import com.iviet.ivshs.dto.*;
import com.iviet.ivshs.service.PowerConsumptionServiceV1;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PowerConsumptionControllerV1 {

    @Autowired
    private PowerConsumptionServiceV1 powerConsumptionService;

    @GetMapping("/rooms/{roomId}/power-consumptions")
    public ResponseEntity<ApiResponseV1<PaginatedResponseV1<PowerConsumptionDtoV1>>> 
            getListByRoom(
                @PathVariable(name = "roomId") Long roomId,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "20") int size) {
        PaginatedResponseV1<PowerConsumptionDtoV1> paginated = 
            powerConsumptionService.getListByRoom(roomId, page, size);
        return ResponseEntity.ok(ApiResponseV1.ok(paginated));
    }

    @GetMapping("/power-consumptions/{id}")
    public ResponseEntity<ApiResponseV1<PowerConsumptionDtoV1>> getById(
            @PathVariable(name = "id") Long id) {
        PowerConsumptionDtoV1 dto = powerConsumptionService.getById(id);
        return ResponseEntity.ok(ApiResponseV1.ok(dto));
    }

    @PostMapping("/power-consumptions")
    public ResponseEntity<ApiResponseV1<PowerConsumptionDtoV1>> create(
            @RequestBody @Valid CreatePowerConsumptionDtoV1 dto) {
        PowerConsumptionDtoV1 created = powerConsumptionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseV1.created(created));
    }

    @PutMapping("/power-consumptions/{id}")
    public ResponseEntity<ApiResponseV1<PowerConsumptionDtoV1>> update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UpdatePowerConsumptionDtoV1 dto) {
        PowerConsumptionDtoV1 updated = powerConsumptionService.update(id, dto);
        return ResponseEntity.ok(ApiResponseV1.ok(updated));
    }

    @DeleteMapping("/power-consumptions/{id}")
    public ResponseEntity<ApiResponseV1<Void>> delete(
            @PathVariable(name = "id") Long id) {
        powerConsumptionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponseV1.success(HttpStatus.NO_CONTENT, null, 
                "Deleted successfully"));
    }
}

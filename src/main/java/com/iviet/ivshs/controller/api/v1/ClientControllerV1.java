package com.iviet.ivshs.controller.api.v1;

import com.iviet.ivshs.dto.*;
import com.iviet.ivshs.service.ClientServiceV1;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@Validated
public class ClientControllerV1 {

    @Autowired
    private ClientServiceV1 clientService;

    // --- CRUD CLIENT ---

    @GetMapping
    public ResponseEntity<ApiResponseV1<PaginatedResponseV1<ClientDtoV1>>> 
            getClients(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size) {
        PaginatedResponseV1<ClientDtoV1> paginated = 
            clientService.getAll(page, size);
        return ResponseEntity.ok(ApiResponseV1.ok(paginated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseV1<ClientDtoV1>> getById(
            @PathVariable(name = "id") Long id) {
        ClientDtoV1 dto = clientService.getById(id);
        return ResponseEntity.ok(ApiResponseV1.ok(dto));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponseV1<PaginatedResponseV1<ClientDtoV1>>> 
            getAllByRoomId(
                @PathVariable(name = "roomId") Long roomId,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size) {
        PaginatedResponseV1<ClientDtoV1> paginated = 
            clientService.getAllGatewaysByRoomId(roomId, page, size);
        return ResponseEntity.ok(ApiResponseV1.ok(paginated));
    }

    @PostMapping
    public ResponseEntity<ApiResponseV1<ClientDtoV1>> create(
            @RequestBody @Valid CreateClientDtoV1 request) {
        ClientDtoV1 created = clientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseV1.created(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseV1<ClientDtoV1>> update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UpdateClientDtoV1 request) {
        ClientDtoV1 updated = clientService.update(id, request);
        return ResponseEntity.ok(ApiResponseV1.ok(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseV1<Void>> delete(
            @PathVariable(name = "id") Long id) {
        clientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponseV1.success(HttpStatus.NO_CONTENT, null, 
                "Deleted successfully"));
    }
}

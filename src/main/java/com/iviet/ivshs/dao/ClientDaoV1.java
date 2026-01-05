package com.iviet.ivshs.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.iviet.ivshs.entities.ClientV1;
import com.iviet.ivshs.enumeration.ClientTypeV1;

import jakarta.persistence.criteria.JoinType;

@Repository
public class ClientDaoV1 extends BaseAuditEntityDaoV1<ClientV1> {

    public ClientDaoV1() {
        super(ClientV1.class);
    }

    // ======= Find by Username =======
    public Optional<ClientV1> findByUsername(String username) {
        return findOne(root -> this.getCB().equal(root.get("username"), username));
    }

    public Optional<ClientV1> findUserByUsername(String username) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("username"), username),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.USER)
        ));
    }

    public Optional<ClientV1> findGatewayByUsername(String username) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("username"), username),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
        ));
    }

    public boolean existsByUsername(String username) {
        return exists(root -> this.getCB().equal(root.get("username"), username));
    }

    // ======= Find by IP Address =======
    public Optional<ClientV1> findByIpAddress(String ipAddress) {
        return findOne(root -> this.getCB().equal(root.get("ipAddress"), ipAddress));
    }

    public Optional<ClientV1> findUserByIpAddress(String ipAddress) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("ipAddress"), ipAddress),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.USER)
        ));
    }

    public Optional<ClientV1> findGatewayByIpAddress(String ipAddress) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("ipAddress"), ipAddress),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
        ));
    }

    // ======= Find by ID =======
    public Optional<ClientV1> findById(Long id) {
        return findOne(root -> this.getCB().equal(root.get("id"), id));
    }

    public Optional<ClientV1> findUserById(Long id) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("id"), id),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.USER)
        ));
    }

    public Optional<ClientV1> findGatewayById(Long id) {
        return findOne(root -> this.getCB().and(
            this.getCB().equal(root.get("id"), id),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
        ));
    }

    // ======= Find Gateways by Room ID =======
    public List<ClientV1> findGatewaysByRoomId(Long roomId, int page, int size) {
        return findAll(
            root -> this.getCB().and(
                this.getCB().equal(root.get("room").get("id"), roomId),
                this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
            ),
            (root, cq) -> {
                root.fetch("deviceControls", JoinType.LEFT);
                cq.orderBy(this.getCB().desc(root.get("createdAt")));
            },
            page,
            size
        );
    }

    public List<ClientV1> findGatewaysByRoomId(Long roomId) {
        return findAll(
            root -> this.getCB().and(
                this.getCB().equal(root.get("room").get("id"), roomId),
                this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
            ),
            (root, cq) -> {
                root.fetch("deviceControls", JoinType.LEFT);
                cq.orderBy(this.getCB().desc(root.get("createdAt")));
            }
        );
    }

    public long countGatewaysByRoomId(Long roomId) {
        return count(root -> this.getCB().and(
            this.getCB().equal(root.get("room").get("id"), roomId),
            this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
        ));
    }

    // ======= Find Gateways by Room Code =======
    public List<ClientV1> findGatewaysByRoomCode(String roomCode, int page, int size) {
        return findAll(
            root -> this.getCB().and(
                this.getCB().equal(root.get("room").get("code"), roomCode),
                this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
            ),
            (root, cq) -> {
                root.fetch("deviceControls", JoinType.LEFT);
                cq.orderBy(this.getCB().desc(root.get("createdAt")));
            },
            page,
            size
        );
    }

    public List<ClientV1> findGatewaysByRoomCode(String roomCode) {
        return findAll(
            root -> this.getCB().and(
                this.getCB().equal(root.get("room").get("code"), roomCode),
                this.getCB().equal(root.get("clientType"), ClientTypeV1.HARDWARE_GATEWAY)
            ),
            (root, cq) -> {
                root.fetch("deviceControls", JoinType.LEFT);
                cq.orderBy(this.getCB().desc(root.get("createdAt")));
            }
        );
    }
}

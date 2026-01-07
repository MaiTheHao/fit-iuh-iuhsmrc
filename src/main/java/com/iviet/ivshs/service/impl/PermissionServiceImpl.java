package com.iviet.ivshs.service.impl;

import com.iviet.ivshs.dao.SysClientFunctionCacheDao;
import com.iviet.ivshs.service.PermissionService;
import com.iviet.ivshs.util.FunctionCodeHelper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SysClientFunctionCacheDao cacheDao;

	@Override
	@Transactional(readOnly = true)
	public boolean hasPermission(Long clientId, String functionCode) {
		if (functionCode == null || functionCode.isBlank()) return false;
		return cacheDao.hasPermission(clientId, functionCode);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasPermissions(Long clientId, List<String> functionCodes) {
		if (functionCodes == null || functionCodes.isEmpty()) return false;
		for (String functionCode : functionCodes) {
			if (!hasPermission(clientId, functionCode)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(Long clientId) {
		List<String> functionCodes = cacheDao.getFunctionCodesByClient(clientId);
		return new HashSet<>(functionCodes);
	}

	@Override
	@Transactional(readOnly = true)
	public long countPermissions(Long clientId) {
		return cacheDao.countDistinctFunctionsByClient(clientId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean canAccessFloor(Long clientId, String floorCode) {
		Set<String> permissions = getPermissions(clientId);
		if (permissions.contains(FunctionCodeHelper.buildFloorAccessCode("ALL_FLOORS"))) return true;

		String specificFunc = FunctionCodeHelper.buildFloorAccessCode(floorCode);
		return permissions.contains(specificFunc);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean canAccessRoom(Long clientId, String roomCode) {
		Set<String> permissions = getPermissions(clientId);
		if (permissions.contains(FunctionCodeHelper.buildRoomAccessCode("ALL_ROOMS"))) return true;

		String specificFunc = FunctionCodeHelper.buildRoomAccessCode(roomCode);
		return permissions.contains(specificFunc);
	}
}

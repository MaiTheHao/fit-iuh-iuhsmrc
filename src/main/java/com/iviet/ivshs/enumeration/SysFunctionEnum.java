package com.iviet.ivshs.enumeration;

public enum SysFunctionEnum {
	F_MANAGE_CLIENT("F_MANAGE_CLIENT"),
	F_MANAGE_FLOOR("F_MANAGE_FLOOR"),
	F_MANAGE_ROOM("F_MANAGE_ROOM"),
	F_MANAGE_DEVICE("F_MANAGE_DEVICE"),
	F_MANAGE_ALL("F_MANAGE_ALL"),
	F_ACCESS_FLOOR_ALL("F_ACCESS_FLOOR_ALL"),
	F_ACCESS_ROOM_ALL("F_ACCESS_ROOM_ALL"),
	F_ACCESS_FLOOR_F00("F_ACCESS_FLOOR_F00"),
	F_ACCESS_ROOM_R_MAIN_SERVER("F_ACCESS_ROOM_R-Main-Server");

	private String code;
	
	SysFunctionEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return this.getCode();
	}
}

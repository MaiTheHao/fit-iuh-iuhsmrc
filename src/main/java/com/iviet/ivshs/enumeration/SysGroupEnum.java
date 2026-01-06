package com.iviet.ivshs.enumeration;

public enum SysGroupEnum {
	G_ADMIN("G_ADMIN"),
	G_MANAGER("G_MANAGER"),
	G_USER("G_USER"),
	G_HARDWARE_GATEWAY("G_HARDWARE_GATEWAY");

	private String code;

	SysGroupEnum(String code) {
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

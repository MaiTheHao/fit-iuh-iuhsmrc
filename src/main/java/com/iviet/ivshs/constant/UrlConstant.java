package com.iviet.ivshs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlConstant {

    private static final int DEFAULT_PORT = 8080;
    private static final String BASE_URL_FORMAT = "http://%s:%d%s";

    // --- Base Paths ---
    public static final String BASE_PATH_V1 = "/api/v1";

    // --- Endpoints ---
    private static final String PATH_SETUP = "/setup/";
    private static final String PATH_HEALTH = "/health-check/";
    private static final String PATH_CONTROL = "/control/%s";

    private static final String PATH_TEMP = "/temperature/%s";
    private static final String PATH_POWER = "/power-consumption/%s";
    private static final String PATH_BATCH_TELEMETRY = "/telemetry";
    

    // --- Public API Methods ---

    /**
     * Lấy URL lấy thông tin setup từ client IP
     * @param ip
     * @return URL setup
     */
    public static String getSetupUrlV1(String ip) {
        return build(ip, BASE_PATH_V1, PATH_SETUP);
    }
    
    /**
     * Lấy URL health check theo client IP
     * @param ip
     * @return URL health check
     */
    public static String getHealthUrlV1(String ip) {
        return build(ip, BASE_PATH_V1, PATH_HEALTH);
    }

    /**
     * Lấy URL lấy giá trị nhiệt độ hiện tại của sensor theo naturalId trong client IP cụ thể
     * @param ip
     * @param naturalId
     * @return URL lấy giá trị hiện tại của sensor nhiệt độ
     */
    public static String getTelemetryTempV1(String ip, String naturalId) {
        return build(ip, BASE_PATH_V1, String.format(PATH_TEMP, naturalId));
    }

    /**
     * Lấy URL lấy giá trị công suất điện hiện tại của sensor theo naturalId trong client IP cụ thể
     * @param ip
     * @param naturalId
     * @return URL lấy giá trị hiện tại của sensor công suất điện
     */
    public static String getTelemetryPowerV1(String ip, String naturalId) {
        return build(ip, BASE_PATH_V1, String.format(PATH_POWER, naturalId));
    }

    /**
     * Lấy URL lấy tổng hợp các giá trị telemetry trong client IP cụ thể
     * @param ip
     * @return URL lấy giá trị telemetry
     */
    public static String getTelemetryByGatewayV1(String ip) {
        return build(ip, BASE_PATH_V1, PATH_BATCH_TELEMETRY);
    }

    /**
     * Lấy URL điều khiển thiết bị theo naturalId trong client IP cụ thể
     * @param ip
     * @param naturalId
     * @return URL điều khiển thiết bị
     */
    public static String getControlUrlV1(String ip, String naturalId) {
        return build(ip, BASE_PATH_V1, String.format(PATH_CONTROL, naturalId));
    }

    // --- Logic lõi xử lý nối chuỗi ---

    private static String build(String ip, String basePath, String endpoint) {
        String cleanPath = normalize(basePath);
        String host = String.format(BASE_URL_FORMAT, ip, DEFAULT_PORT, cleanPath);
        return host + endpoint;
    }

    private static String normalize(String path) {
        if (path == null || path.isBlank()) return "";
        String p = path.trim();
        if (!p.startsWith("/")) p = "/" + p;
        while (p.endsWith("/")) p = p.substring(0, p.length() - 1);
        return p;
    }
}
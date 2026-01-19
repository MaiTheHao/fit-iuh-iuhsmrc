package com.iviet.ivshs.util;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CronExpressionUtil {

    /**
     * Kiểm tra xem chuỗi cron có hợp lệ theo chuẩn Quartz hay không.
     *
     * @param cronExpression Chuỗi cron cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Validate chuỗi cron, ném ra ngoại lệ nếu không hợp lệ.
     * Dùng để tích hợp trong các logic cần fail-fast.
     *
     * @param cronExpression Chuỗi cron cần kiểm tra
     * @throws IllegalArgumentException nếu chuỗi cron không hợp lệ
     */
    public static void validate(String cronExpression) {
        if (!isValid(cronExpression)) {
            throw new IllegalArgumentException("Invalid cron expression format: " + cronExpression);
        }
    }

    /**
     * Lấy thời gian thực thi tiếp theo dựa trên chuỗi cron.
     *
     * @param cronExpression Chuỗi cron
     * @param fromDate       Thời điểm bắt đầu tính
     * @return Date thời điểm chạy tiếp theo, hoặc null nếu không còn lần chạy nào
     */
    public static Date getNextExecutionTime(String cronExpression, Date fromDate) {
        if (!isValid(cronExpression)) {
            return null;
        }
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(fromDate);
        } catch (ParseException e) {
            log.error("Error parsing cron expression: {}", cronExpression, e);
            return null;
        }
    }

    public static class Patterns {
        public static final String EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * ?";
        public static final String EVERY_HOUR = "0 0 * * * ?";
    }
}
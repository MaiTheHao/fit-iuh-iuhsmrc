package com.iviet.ivshs.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Optional;

@UtilityClass
public class LocalContextUtil {

    public final String LANG_VI = "vi";
    public final String LANG_EN = "en";
    
    public final Locale DEFAULT_LOCALE = Locale.of("vi", "VN");
    public final String DEFAULT_LANG_CODE = LANG_VI;

    @NonNull
    public Locale getCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        return (locale != null) ? locale : DEFAULT_LOCALE;
    }

    public String getCurrentLangCode() {
        return Optional.of(getCurrentLocale().getLanguage())
                .filter(StringUtils::hasText)
                .map(String::toLowerCase)
                .orElse(DEFAULT_LANG_CODE);
    }

    public String resolveLangCode(String langCode) {
        return StringUtils.hasText(langCode) 
                ? langCode.trim().toLowerCase() 
                : getCurrentLangCode();
    }
}
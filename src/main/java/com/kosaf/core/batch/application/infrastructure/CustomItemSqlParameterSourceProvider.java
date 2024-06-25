package com.kosaf.core.batch.application.infrastructure;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class CustomItemSqlParameterSourceProvider<T> extends BeanPropertyItemSqlParameterSourceProvider<T> {
    @Override
    public SqlParameterSource createSqlParameterSource(T item) {
        SqlParameterSource sqlParameterSource = super.createSqlParameterSource(item);

        if (item instanceof ReplaceKw) {
            ReplaceKw replaceKw = (ReplaceKw) item;
            String useAtValue = replaceKw.getUseAt().name();
            sqlParameterSource = new BeanPropertySqlParameterSource(replaceKw) {
                @Override
                public Object getValue(String paramName) throws IllegalArgumentException {
                    if ("useAt".equals(paramName)) {
                        return useAtValue;
                    }
                    return super.getValue(paramName);
                }
            };
        }

        return sqlParameterSource;
    }
}
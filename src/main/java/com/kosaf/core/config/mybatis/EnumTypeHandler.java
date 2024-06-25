package com.kosaf.core.config.mybatis;

import com.kosaf.core.common.CodeType;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@NoArgsConstructor
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<CodeType>{
    private Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CodeType parameter, JdbcType jdbcType) throws SQLException {
         ps.setString(i,  parameter.getCode());
    }

    @Override
    public CodeType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return (CodeType) getCodeType(code);
    }

    @Override
    public CodeType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return (CodeType) getCodeType(code);
    }

    @Override
    public CodeType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return (CodeType) getCodeType(code);
    }

    private CodeType getCodeType(String code) {
        if (code == null) {
            return null;
        }
        CodeType[] enumConstants = (CodeType[]) type.getEnumConstants();
        for (CodeType codeNum : enumConstants) {
            if (codeNum.getCode().equals(code)) {
                return codeNum;
            }
        }
        return null;
    }
}

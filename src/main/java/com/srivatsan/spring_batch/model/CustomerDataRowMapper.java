package com.srivatsan.spring_batch.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerDataRowMapper implements RowMapper<CustomerData> {
    @Override
    public CustomerData mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerData customerData = new CustomerData(
                rs.getLong("id"),
                rs.getString("MSISDN"),
                rs.getString("trnx_id")
        );
        return customerData;
    }
}

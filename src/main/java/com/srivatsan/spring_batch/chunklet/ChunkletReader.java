package com.srivatsan.spring_batch.chunklet;

import com.srivatsan.spring_batch.app.AppConfig;
import com.srivatsan.spring_batch.model.CustomerData;
import com.srivatsan.spring_batch.model.CustomerDataRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ChunkletReader implements ItemReader<JdbcPagingItemReader<CustomerData>> {

    public DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private final AppConfig appConfig;
    private CustomerDataRowMapper customerDataRowMapper;

    @Override
    public JdbcPagingItemReader<CustomerData> read() throws Exception {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("select * ");
        factoryBean.setFromClause("from customer_data");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put(appConfig.getOrderColumn(), Order.DESCENDING);
        factoryBean.setSortKeys(sortKeys);

        JdbcPagingItemReader<CustomerData> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setQueryProvider(factoryBean.getObject());
        itemReader.setPageSize(appConfig.getChunkSize());
        itemReader.setRowMapper(customerDataRowMapper);
        return itemReader;
    }
}

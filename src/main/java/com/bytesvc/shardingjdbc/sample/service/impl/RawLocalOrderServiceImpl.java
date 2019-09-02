/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytesvc.shardingjdbc.sample.service.impl;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service("rawLocalOrder")
public class RawLocalOrderServiceImpl implements IOrderService {
    
    private final DataSource dataSource;
    
    @Autowired
    public RawLocalOrderServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional
    public void createOrder(String status) {
        this.doCreateOrder(1, status);
        this.doCreateOrder(2, status);
    }
    
    private void doCreateOrder(long userId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            // conn = this.shardingDataSource.getConnection();
            stmt = conn.prepareStatement("INSERT INTO t_order (user_id, status) VALUES (?, ?)");
            stmt.setLong(1, userId);
            stmt.setString(2, status);
            stmt.executeUpdate();
        } catch (SQLException error) {
            throw new IllegalStateException(error.getMessage(), error);
        } finally {
            this.closeQuietly(stmt);
            this.closeQuietly(conn);
        }
    }
    
    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception error) {
                // ignore
            }
        }
    }

}

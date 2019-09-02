# 演示原生数据源场景
1. 调整ShardingJdbcSampleBootstrap的注解， 确保Import(RawLocalTransactionConfiguration.class)
2. 执行: curl -X POST http://127.0.0.1:8080/order/createThree

# 演示ShardingJdbc(默认)
1. (默认不用修改) 调整ShardingJdbcSampleBootstrap的注解， 确保Import(ShardingConfiguration.class)
2. 执行: curl -X POST http://127.0.0.1:8080/order/createOne

# 演示普通数据源场景
1. 调整ShardingJdbcSampleBootstrap的注解， 确保Import(GenericConfiguration.class)
2. 执行: curl -X POST http://127.0.0.1:8080/order/createTwo

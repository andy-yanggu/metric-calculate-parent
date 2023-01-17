package com.yanggu.metric_calculate.core.middle_store;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.kryo.CoreKryoFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtils;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2023/1/12 18:18
 * @description:
 */
public class DeriveMetricMiddleHbaseStore implements DeriveMetricMiddleStore {

    private Table testTable;

    private KryoPool kryoPool;

    private List<Class<? extends MergedUnit>> classList;

    @SneakyThrows
    @Override
    public void init() {
        kryoPool = KryoUtils.createRegisterKryoPool(new CoreKryoFactory(classList));

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "172.20.3.57:2182");
        //同步连接
        Connection connection = ConnectionFactory.createConnection(conf);
        testTable = connection.getTable(TableName.valueOf("TestTable"));
    }

    @SneakyThrows
    @Override
    public MetricCube get(MetricCube cube) {
        Get get = new Get(Bytes.toBytes(cube.getRealKey())).addFamily(Bytes.toBytes("testFamily"));
        Result result = testTable.get(get);
        for (Cell cell : result.rawCells()) {
            long key = Bytes.toLong(CellUtil.cloneQualifier(cell));
            byte[] bytes = CellUtil.cloneValue(cell);
            //kryoPool.
        }
        TimedKVMetricCube timedKVMetricCube = new TimedKVMetricCube<>();
        //vcTimedKVMetricCube.set
        return null;
    }

    @Override
    public void put(MetricCube cube) {

    }
}

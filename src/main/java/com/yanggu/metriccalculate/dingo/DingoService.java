package com.yanggu.metriccalculate.dingo;

import io.dingodb.sdk.client.DingoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class DingoService {

    @Value("${dingo.client.retry-times}")
    private Integer retryTimes;

    @Value("${dingo.client.coordinator-server-list}")
    private String coordinatorServerList;

    private DingoClient dingoClient;

    public DingoClient getDingoClient() {
        return dingoClient;
    }

    @PostConstruct
    public void init() {
        dingoClient = new DingoClient(coordinatorServerList, retryTimes);
        dingoClient.open();
    }

    public Object query(String tableName) {


        //new Key("DINGO", tableName, Arrays.asList("", ""))
        //List<Record> records = dingoClient.query();
        return null;
    }

    @PreDestroy
    public void close() {
        dingoClient.close();
    }

}

package com.liuhao.lemo.elasticsearch.repository;

import com.liuhao.lemo.elasticsearch.entity.User;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

//JTA 标准定义
public interface UserRepository extends ElasticsearchRepository<User,Long>{

    List<User> findByAgeBetween(Integer age1,Integer age2);

    @Query(" {\n" +
            "    \"range\": {\n" +
            "      \"age\": {\n" +
            "        \"gte\": \"?0\",\n" +
            "        \"lte\": \"?1\"\n" +
            "      }\n" +
            "    }\n" +
            "  }")
    List<User> findByDslQuery(Integer age1,Integer age2);

}

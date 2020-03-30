package com.liuhao.lemo.elasticsearch;

import com.liuhao.lemo.elasticsearch.entity.User;
import com.liuhao.lemo.elasticsearch.repository.UserRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class LiuhaoElasticsearchApplicationTests {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {

		//创建库
		elasticsearchRestTemplate.createIndex(User.class);

		//创建映射
		elasticsearchRestTemplate.putMapping(User.class);
	}

	@Test
	void addUser(){
		userRepository.save(new User(1l,"刘浩是一个傻逼",19,"12345"));
	}

	@Test
	void addUserBat(){
		List<User> userList = Arrays.asList(
		new User(1l,"刘浩是一个傻逼",19,"12345"),
		new User(2l,"李倩，麻辣隔壁，喜大普奔",33,"12345"),
		new User(3l,"风车车，重庆扛霸子",22,"12345"),
		new User(4l,"习大大，牛逼",56,"12345"),
		new User(5l,"特朗普，好孩子",78,"12345"),
		new User(6l,"李宇春，真男人",30,"12345")
		);

		userRepository.saveAll(userList);

	}

	@Test
	void finUser(){
		//userRepository.findAll().forEach(t-> System.out.println(t.getId()+" "+t.getName()));
		//userRepository.findByAgeBetween(18,30).forEach(t-> System.out.println(t));
		userRepository.findByDslQuery(20,40).forEach(t-> System.out.println(t));
	}


	//自定义查询构建器
	@Test
	void findUserByQueryBulid(){
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		//构建查询
		queryBuilder.withQuery(QueryBuilders.matchQuery("name","喜大普奔"));
		//分页
		queryBuilder.withPageable(PageRequest.of(0,2));
		//排序
		queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
		queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
		Page<User> page =  userRepository.search(queryBuilder.build());
		System.out.println("元素数量："+page.getTotalElements());
		System.out.println("页数："+page.getTotalPages());
		page.getContent().forEach(t-> System.out.println(t));
	}


}

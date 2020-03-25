package liuhao.memo.mybatiesplus.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import liuhao.memo.mybatiesplus.demo.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        /*User user = userMapper.selectById(1L);
        System.out.println(user.getAge());*/

        //List<User> list = userMapper.selectBatchIds(Arrays.asList(1l,2l,3l));
        //list.forEach(user1 -> System.out.println(user1.getName()+" "+user1.getAge()));
        User user = new User(null,"insertOne",20,"xxx@qq.com");
        //userMapper.insert(user);

        //只会更新 非null的值
        //userMapper.update(new User(null,"刘浩",18,null),new UpdateWrapper<>(new User(null,null,null,"xxx@qq.com")));

        //声明条件更新
       /* UpdateWrapper updateWrapper = new UpdateWrapper(new User(null,null,null,"xxx@qq.com"));
        updateWrapper.set("age",18);
        userMapper.update(new User(null,"刘浩",null,"testst@qq.com"),updateWrapper);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","%n%");
        queryWrapper.orderByDesc("age");
        List<User>  list1 = userMapper.selectList(queryWrapper);
        list1.forEach(user1 -> System.out.println("list1 "+user1.getName()+" "+user1.getAge()));*/


        //分页查询
       /* QueryWrapper<User> queryWrapper_1 = new QueryWrapper<>();
        queryWrapper_1.orderByDesc("age");
        IPage<User> iPage = userMapper.selectPage(new Page<User>(1,2),queryWrapper_1);
        iPage.getRecords().forEach(user1 -> System.out.println("iPage "+user1.getName()+" "+user1.getAge()));*/


    }

}

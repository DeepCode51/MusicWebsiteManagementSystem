package com.example.test;


import com.example.test.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以**，可以嫖娼，可以吸毒，可以开票，哈哈哈！";
        // 过滤之后的文本
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "这里可以☆***，可以嫖☆娼，哈哈哈";
        System.out.println(text);
    }
}

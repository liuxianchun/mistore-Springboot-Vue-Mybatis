package com.lxc.mistoreserver;

import com.lxc.dao.ScheduleDao;
import com.lxc.schedule.OutDateSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/3/10
 */
@SpringBootTest
public class test1 {

    @Autowired
    private ScheduleDao scheduleDao;

    @Test
    void test1() throws IOException {
        File user = new File("C:\\Users\\14948\\Desktop\\user.txt");
        user.delete();
        user.createNewFile();
        FileWriter fw = new FileWriter(user);
        for(int i=0;i<1000;i++){
            fw.write("user_"+i+",password\n");
        }
        fw.close();
    }

    @Test
    void test2() throws IOException {
        File sec = new File("C:\\Users\\14948\\Desktop\\seckill.txt");
        sec.delete();
        sec.createNewFile();
        FileWriter fw = new FileWriter(sec);
        for(int i=0;i<1000;i++){
            fw.write(1017+i+",1,2\n");
            fw.write(1017+i+",2,5\n");
            fw.write(1017+i+",3,15\n");
            fw.write(1017+i+",4,9\n");
        }
    }
}

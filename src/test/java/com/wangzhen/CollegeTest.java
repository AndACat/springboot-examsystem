package com.wangzhen;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.College;
import com.wangzhen.services.admin.CollegeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/7 21:55
 */
@SpringBootTest
public class CollegeTest {
    @Autowired
    private CollegeService collegeService;
    @Test
    public void insertCollege(){
        String a[] = {"软件工程","信息管理与信息系统","计算机科学与技术","物联网工程"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("计算机与信息工程学院", professionList);
    }
    @Test
    public void insertCollege1(){
        String a[] = {"农学","种子科学与工程","生物技术","环境科学"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("农学与资源环境学院", professionList);
    }
    @Test
    public void insertCollege2(){
        String a[] = {"林学","园林","园艺","设施农业科学与工程","植物保护","环境设计（备注：此专业为艺术类专业）"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("园艺园林学院", professionList);
    }
    @Test
    public void insertCollege3(){
        String a[] = {"动物科学","动物科学（设施畜牧业方向）","动物医学","动物医学（小动物医学方向）","动物药学"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("动物科学与动物医学学院", professionList);
    }
    @Test
    public void insertCollege4(){
        String a[] = {"海洋渔业科学与技术","水产养殖学","水族科学与技术","水产养殖技术"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("水产学院", professionList);
    }
    @Test
    public void insertCollege5(){
        String a[] = {"生物工程","食品科学与工程","食品质量与安全"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("食品科学与生物工程学院", professionList);
    }
    @Test
    public void insertCollege6(){
        String a[] = {"水文与水资源工程","水利水电工程","工程管理","水文与水资源工程"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("水利工程学院", professionList);
    }
    @Test
    public void insertCollege7(){
        String a[] = {"应用化学","生物制药","生物制药（材料方向）"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("基础科学学院", professionList);
    }
    @Test
    public void insertCollege8(){
        String a[] = {"测控技术与仪器","农业机械化及其自动化","新能源科学与工程","电气工程及其自动化"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("工程技术学院", professionList);
    }
    @Test
    public void insertCollege9(){
        String a[] = {"旅游管理","酒店管理","人力资源管理","文化产业管理","英语","酒店管理"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("人文学院", professionList);
    }
    @Test
    public void insertCollege10(){
        String a[] = {"公共事业管理","国际经济与贸易","会计学","农林经济管理","市场营销","物流管理"};
        String professionList = JSON.toJSONString(College.buildProfessionList(a));
        collegeService.insertCollege("经济管理学院", professionList);
    }

}

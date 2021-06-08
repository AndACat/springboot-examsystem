package com.wangzhen.services.teacher;

import com.wangzhen.dao.teacher.PaperDao;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.Paper;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.ClaService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/14 22:00
 */
@Service
public class PaperService {
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ClaService claService;

    public boolean insertPaper(Paper paper){
        return paperDao.insertPaper(paper);
    }

    public void deletePaperByUuid(String uuid){
        paperDao.deletePaperByUuid(uuid);
    }

    public List<Paper> selectMyAllPaper(String teacher_uuid){
        List<Paper> paperList = paperDao.selectMyAllPaper(teacher_uuid);
        List<Course> courseList = new ArrayList<>();
        Course c = null;
        for (Paper paper : paperList) {
            String course_uuid = paper.getCourse_uuid();
            boolean tag = false;//不包含
            for (Course course : courseList) {
                if(course_uuid.equals(course.getUuid())){
                    tag = true;
                    paper.setCourse(course);
                    c = course;
                    break;
                }
            }
            if(!tag){
                c = courseService.selectCourseByUuid(course_uuid);
                if(c != null){
                    courseList.add(c);
                    paper.setCourse(c);
                }
            }
            Cla cla = claService.selectClaByCourseUuid(c.getUuid());
            paper.setCla(cla);
        }

        return paperList;
    }

    public Paper selectPaperByPaper_uuid(String paper_uuid){
        return paperDao.selectPaperByPaper_uuid(paper_uuid);
    }
    public List<Paper> selectPaperByTeacher_uuid(String teacher_uuid){
        return paperDao.selectPaperByTeacher_uuid(teacher_uuid);
    }

    public void updatePaperInfo( boolean sort, boolean openFaceIdentity,boolean visible, Timestamp begin, Timestamp end, int during, String uuid){
        paperDao.updatePaperInfo(sort,openFaceIdentity,visible,begin,end,during,uuid);
    }

    public List<Paper> selectAllPaper(){
        return paperDao.selectAllPaper();
    }

    public Paper selectPaperByPaperName(String paperName,String teacher_uuid) {
        return paperDao.selectPaperByPaperName(paperName,teacher_uuid);
    }

    public List<Paper> selectSimplePaperForStudentToDisplay(List<String> course_uuid_list){
        if(course_uuid_list.isEmpty()){
            return new ArrayList<>();
        }
        List<Paper> papers = paperDao.selectSimplePaperForStudentToDisplay(course_uuid_list);
        for (Paper paper : papers) {
            paper.setCourse(courseService.selectCourseByUuid(paper.getCourse_uuid()));
            paper.setCourse_uuid("");
        }
        return papers;
    }

    public Paper selectPaperForStudentToExam(String uuid){
        return paperDao.selectPaperForStudentToExam(uuid);
    }

    public LinkedHashMap<String, String> getScoreOfMyAllPaper(String teacher_uuid) {
        List<Paper> scoreOfMyAllPaper = paperDao.getScoreOfMyAllPaper(teacher_uuid);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (Paper paper : scoreOfMyAllPaper) {
            map.put(paper.getUuid(),paper.getPaperName());
        }
        return map;
    }

    public int selectCountOfPaperByPaperStrategy_uuid(String uuid) {
        return paperDao.selectCountOfPaperByPaperStrategy_uuid(uuid);
    }
}

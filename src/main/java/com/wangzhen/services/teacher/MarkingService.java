package com.wangzhen.services.teacher;

import com.wangzhen.dao.StudentPaperAnswerDao;
import com.wangzhen.dao.admin.userdao.TeacherDao;
import com.wangzhen.models.Paper;
import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.admin.TeacherService;
import com.wangzhen.services.manager.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/27 16:30
 */
@Service
public class MarkingService {
    @Autowired
    private StudentPaperAnswerDao studentPaperAnswerDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PaperService paperService;
    public LinkedHashMap<String,String> getMarkingPaper(String teacher_uuid){
        LinkedHashMap<String,String> returnMap = new LinkedHashMap<>();
        List<Paper> paperList = paperService.selectPaperByTeacher_uuid(teacher_uuid);
        Map<String,String> paper_uuid_map = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < paperList.size(); i++) {
            Paper paper = paperList.get(i);
            paper_uuid_map.put(paper.getUuid(),paper.getPaperName());
            if(i!=paperList.size()-1){
                stringBuilder.append("paper_uuid = '"+paper.getUuid()+"' or ");
            }else{
                stringBuilder.append("paper_uuid = '"+paper.getUuid()+"'");
            }
        }
        List<StudentPaperAnswer> studentPaperAnswerList = studentPaperAnswerDao.selectMarkingPaper(stringBuilder.toString());//studentPaperAnswer中的待判答案
        for (StudentPaperAnswer studentPaperAnswer : studentPaperAnswerList) {
            String paper_uuid = studentPaperAnswer.getPaper_uuid();
            if(paper_uuid_map.keySet().contains(paper_uuid)){
                returnMap.put(paper_uuid,paper_uuid_map.get(paper_uuid));
            }
        }
        return returnMap;
    }

    public List<StudentPaperAnswer> selectOneRequiredMarkingPaper(String paper_uuid) {
        List<StudentPaperAnswer> studentPaperAnswerList = studentPaperAnswerDao.selectAllStudentPaperAnswerOfSinglePaper(paper_uuid);
        for (StudentPaperAnswer studentPaperAnswer : studentPaperAnswerList) {
            String student_uuid = studentPaperAnswer.getStudent_uuid();
            studentPaperAnswer.setStudent(studentService.selectStudentByUuid(student_uuid));
        }
        return studentPaperAnswerList;
    }
}

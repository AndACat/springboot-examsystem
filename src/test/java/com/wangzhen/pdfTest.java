package com.wangzhen;

import com.alibaba.fastjson.JSON;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.services.manager.ClaService;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.services.teacher.PaperStrategyService;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;
import com.wangzhen.utils.pdf.PaperToPdfUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/5 15:36
 */
public class pdfTest {
    @Autowired
    SingleChoiceService singleChoiceService;
    @Autowired
    MultipleChoiceService multipleChoiceService;
    @Autowired
    FillService fillService;
    @Autowired
    JudgeService judgeService;
    @Autowired
    ProgramService programService;
    @Autowired
    ShortService shortService;
    @Autowired
    PaperService paperService;
    @Autowired
    ClaService claService;
    @Autowired
    PaperStrategyService paperStrategyService;
    @Test
    public void s() throws FileNotFoundException {
        String account = "1677688026";
        int problemNum = 3;
//        List<SingleChoice> singleChoiceList = singleChoiceService.selectProblemWithNum(account,problemNum);
//        List<MultipleChoice> multipleChoiceList = multipleChoiceService.selectProblemWithNum(account, problemNum);
//        List<Fill> fillList = fillService.selectProblemWithNum(account, problemNum);
//        List<Judge> judgeList = judgeService.selectProblemWithNum(account, problemNum);
//        List<Program> programList = programService.selectProblemWithNum(account, problemNum);
//        List<Short> shortList = shortService.selectProblemWithNum(account, problemNum);

        //init
        File pdfFile = new File("T:\\Desktop\\1.pdf");
//        PaperStrategy paperStrategy = new PaperStrategy();
//        List<ProblemStrategy> problemStrategyList = new ArrayList<>();
//        problemStrategyList.add(new ProblemStrategy("单选题",problemNum,0));
//        problemStrategyList.add(new ProblemStrategy("多选题",problemNum,0));
//        problemStrategyList.add(new ProblemStrategy("判断题",problemNum,0));
//        problemStrategyList.add(new ProblemStrategy("填空题",problemNum,0));
//        problemStrategyList.add(new ProblemStrategy("编程题",problemNum,0));
//        problemStrategyList.add(new ProblemStrategy("简答题",problemNum,0));
//        paperStrategy.setProblemStrategyList(JSON.toJSONString(problemStrategyList));

        Paper paper = paperService.selectPaperByPaper_uuid("fe06b35b25d44d218b0059ff55f76aab");
        PaperStrategy paperStrategy = paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paper.getPaperStrategy_uuid());
        paper.setPaperStrategy(paperStrategy);

//        PaperToPdfUtil.toPdf(paper,pdfFile);


    }
    @Test
    public void ss() {
        File file = new File("C:/ExamSystem/" + Utils.randomUuid() + ".pdf");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

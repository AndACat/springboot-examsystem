package com.wangzhen.utils.pdf;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/5 15:37
 */
public class PaperToPdfUtil {
    private final String SINGLECHOICELIST = "singleChoiceList";
    private final String MULTIPLECHOICELIST = "multipleChoiceList";
    private final String FILLLIST = "fillList";
    private final String JUDGELIST = "judgeList";
    private final String PROGRAMLIST = "programList";
    private final String SHORTLIST = "shortList";
    public static synchronized boolean toPdf(Paper paper, File file) throws IOException {
        return new PaperToPdfUtil().getPdf(paper,file);
    }
    public PdfFont getCnFont() {
        try {
            return PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Text getText(String text,boolean bold, Color fontColor){
        Text t = new Text(text);
        t.setFont(getCnFont());
        if(bold) {
            t.setBold();
        }
        t.setFontColor(fontColor);
        return t;
    }
    private Text getText(String text,boolean bold){
        Text t = new Text(text);
        t.setFont(getCnFont());
        if(bold) {
            t.setBold();
        }
        return t;
    }
    private Text getText(String text){
        Text t = new Text(text);
        t.setFont(getCnFont());
        return t;
    }
    /**
     * @Description 抛出的接口
     * @date 2020/4/5 16:48
     * @param paper
     * @param file
     * @return boolean
     */
    public boolean getPdf(Paper paper, File file) throws IOException {
        PaperStrategy paperStrategy = paper.getPaperStrategy();
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
        List<SingleChoice> singleChoiceList = new ArrayList<>();
        List<MultipleChoice> multipleChoiceList = new ArrayList<>();
        List<Fill> fillList = new ArrayList<>();
        List<Judge> judgeList = new ArrayList<>();
        List<Short> shortList = new ArrayList<>();
        List<Program> programList = new ArrayList<>();
        if(paperInfo.containsKey(SINGLECHOICELIST)){
            singleChoiceList = (List<SingleChoice>) paperInfo.get(SINGLECHOICELIST);
        }
        if(paperInfo.containsKey(MULTIPLECHOICELIST)){
            multipleChoiceList = (List<MultipleChoice>) paperInfo.get(MULTIPLECHOICELIST);
        }
        if(paperInfo.containsKey(FILLLIST)){
            fillList = (List<Fill>) paperInfo.get(FILLLIST);
        }
        if(paperInfo.containsKey(JUDGELIST)){
            judgeList = (List<Judge>) paperInfo.get(JUDGELIST);
        }
        if(paperInfo.containsKey(SHORTLIST)){
            shortList = (List<Short>) paperInfo.get(SHORTLIST);
        }
        if(paperInfo.containsKey(PROGRAMLIST)){
            programList = (List<Program>) paperInfo.get(PROGRAMLIST);
        }

        //创建流对象
        PdfWriter pdfWriter = new PdfWriter(file);
        //创建文档对象
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        //创建内容文档对象
        Document document = new Document(pdfDocument, PageSize.A4);
        document.setFont(getCnFont());
        //设置试卷名
        Paragraph paragraph = new Paragraph(paper.getPaperName());
        paragraph.setBold();
        paragraph.setFontSize(25);
        paragraph.setFont(getCnFont());
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFontColor(Color.ORANGE);
        document.add(paragraph);

        List<ProblemStrategy> problemStrategyList = paperStrategy.problemStrategyList();
        int bigProblemIdx = 1;
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            String problemType = problemStrategy.getProblemType();
            switch (problemType){
                case "单选题":{
                    appendSingleChoices(document,singleChoiceList,bigProblemIdx++);
                    break;
                }
                case "多选题":{
                    appendMultipleChoices(document,multipleChoiceList,bigProblemIdx++);
                    break;
                }
                case "填空题":{
                    appendFills(document,fillList,bigProblemIdx++);
                    break;
                }
                case "判断题":{
                    appendJudges(document,judgeList,bigProblemIdx++);
                    break;
                }
                case "简答题":{
                    appendShorts(document,shortList,bigProblemIdx++);
                    break;
                }
                case "编程题":{
                    appendPrograms(document,programList,bigProblemIdx++);
                    break;
                }

            }
        }
        document.close();
        return true;
    }

    private void appendMultipleChoices(Document document, List<MultipleChoice> multipleChoiceList, int bigProblemIdx) {
        addBigProblemNo(document,bigProblemIdx,"多选题");
        int problemIdx = 1;
        for (MultipleChoice multipleChoice : multipleChoiceList) {
            appendMultipleChoice(document,multipleChoice,problemIdx++);
        }
    }
    private void appendFills(Document document, List<Fill> fillList, int bigProblemIdx) {
        addBigProblemNo(document,bigProblemIdx,"填空题");
        int problemIdx = 1;
        for (Fill fill : fillList) {
            appendFill(document,fill,problemIdx++);
        }
    }
    private void appendShorts(Document document, List<Short> shortList, int bigProblemIdx) {
        addBigProblemNo(document,bigProblemIdx,"简答题");
        int problemIdx = 1;
        for (Short sho : shortList) {
            appendShort(document,sho,problemIdx++);
        }
    }
    private void appendJudges(Document document, List<Judge> judgeList, int bigProblemIdx) {
        addBigProblemNo(document,bigProblemIdx,"判断题");
        int problemIdx = 1;
        for (Judge judge : judgeList) {
            appendJudge(document,judge,problemIdx++);
        }
    }
    private void appendPrograms(Document document, List<Program> programList, int bigProblemIdx) {
        addBigProblemNo(document,bigProblemIdx,"编程题");
        int problemIdx = 1;
        for (Program program : programList) {
            appendProgram(document,program,problemIdx++);
        }
    }



    /**
     * @Description 添加多道单选题
     * @date 2020/4/5 16:47
     * @param document
     * @param singleChoiceList
     * @param bigProblemIdx
     * @return void
     */
    private void appendSingleChoices(Document document, List<SingleChoice> singleChoiceList, int bigProblemIdx) {
        int problemIdx = 1;
        addBigProblemNo(document,bigProblemIdx,"单选题");
        for (SingleChoice singleChoice : singleChoiceList) {
            appendSingleChoice(document,singleChoice,problemIdx++);
        }
    }
    /**
     * @Description 添加大题序号
     * @date 2020/4/5 16:47
     * @param document
     * @param bigProblemIdx
     * @return void
     */
    private void addBigProblemNo(Document document,int bigProblemIdx,String problemType){
        Paragraph paragraph = new Paragraph("第" + bigProblemIdx + "大题      "+problemType);
        paragraph.setFont(getCnFont());
        paragraph.setBold();
        paragraph.setFontSize(18);
        document.add(paragraph);
    }
    /**
     * @Description 添加一道单选题
     * @date 2020/4/5 16:47
     * @param document
     * @param singleChoice
     * @param problemIdx
     * @return void
     */
    private void appendSingleChoice(Document document, SingleChoice singleChoice, int problemIdx) {
        //段落
        Paragraph paragraph = new Paragraph();
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        Text problem = getText(singleChoice.getProblem(),true,Color.BLACK);
        paragraph.setFont(getCnFont());
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add(getText("     (        )"));
        paragraph.add("\n");//换号？
        //添加 选项
        if(! singleChoice.getChoice_a().isBlank()){
            paragraph.add(getText("选项A: ",true));
            Text choice = getText(singleChoice.getChoice_a());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_b().isBlank()){
            paragraph.add(getText("选项B: ",true));
            Text choice = getText(singleChoice.getChoice_b());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_c().isBlank()){
            paragraph.add(getText("选项C: ",true));
            Text choice = getText(singleChoice.getChoice_c());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_d().isBlank()){
            paragraph.add(getText("选项D: ",true));
            Text choice = getText(singleChoice.getChoice_d());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_e().isBlank()){
            paragraph.add(getText("选项E: ",true));
            Text choice = getText(singleChoice.getChoice_e());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_f().isBlank()){
            paragraph.add(getText("选项F: ",true));
            Text choice = getText(singleChoice.getChoice_f());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_g().isBlank()){
            paragraph.add(getText("选项G: ",true));
            Text choice = getText(singleChoice.getChoice_g());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! singleChoice.getChoice_h().isBlank()){
            paragraph.add(getText("选项H: ",true));
            Text choice = getText(singleChoice.getChoice_h());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        document.add(paragraph);
    }
    private void appendMultipleChoice(Document document, MultipleChoice multipleChoice, int problemIdx) {
        //段落
        Paragraph paragraph = new Paragraph();
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        Text problem = getText(multipleChoice.getProblem(),true,Color.BLACK);
        paragraph.setFont(getCnFont());
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add(getText("     (        )"));
        paragraph.add("\n");
        if(! multipleChoice.getChoice_a().isBlank()){
            paragraph.add(getText("选项A: ",true));
            Text choice = getText(multipleChoice.getChoice_a());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_b().isBlank()){
            paragraph.add(getText("选项B: ",true));
            Text choice = getText(multipleChoice.getChoice_b());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_c().isBlank()){
            paragraph.add(getText("选项C: ",true));
            Text choice = getText(multipleChoice.getChoice_c());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_d().isBlank()){
            paragraph.add(getText("选项D: ",true));
            Text choice = getText(multipleChoice.getChoice_d());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_e().isBlank()){
            paragraph.add(getText("选项E: ",true));
            Text choice = getText(multipleChoice.getChoice_e());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_f().isBlank()){
            paragraph.add(getText("选项F: ",true));
            Text choice = getText(multipleChoice.getChoice_f());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_g().isBlank()){
            paragraph.add(getText("选项G: ",true));
            Text choice = getText(multipleChoice.getChoice_g());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        if(! multipleChoice.getChoice_h().isBlank()){
            paragraph.add(getText("选项H: ",true));
            Text choice = getText(multipleChoice.getChoice_h());
            paragraph.add(choice);
            paragraph.add("\n");
        }
        document.add(paragraph);
    }

    private void appendFill(Document document,Fill fill,int problemIdx){
        //段落
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(getCnFont());
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        String pro = fill.getProblem();
        do{
            pro = pro.replace("#{}","_____________");
        }while (pro.contains("#{}"));
        Text problem = getText(pro,true,Color.BLACK);
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add("\n");
        document.add(paragraph);
    }
    private void appendShort(Document document,Short sho,int problemIdx){
        //段落
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(getCnFont());
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        Text problem = getText(sho.getProblem(),true,Color.BLACK);
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add("\n\n\n\n\n\n\n");
        document.add(paragraph);
    }
    private Text judge_true = getText("选项A:   对\n");
    private Text judge_false = getText("选项B:   错\n");
    private void appendJudge(Document document,Judge judge,int problemIdx){
        //段落
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(getCnFont());
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        Text problem = getText(judge.getProblem(),true,Color.BLACK);
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add(getText("     (        )"));
        paragraph.add("\n");
        paragraph.add(judge_true);
        paragraph.add(judge_false);
        document.add(paragraph);
    }
    private void appendProgram(Document document,Program program,int problemIdx){
        //段落
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(getCnFont());
        //题目序号
        Text xh = getText("第"+problemIdx+"题",true,Color.RED);
        //空格
        Text tab = getText("   ");
        //题目
        Text problem = getText(program.getProblem(),true,Color.BLACK);
        paragraph.add(xh);
        paragraph.add(tab);
        paragraph.add(problem);
        paragraph.add("\n\n\n\n\n\n\n");
        document.add(paragraph);
    }
}

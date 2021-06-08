package com.wangzhen.utils;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
public class ExcelReadUtil {
    /**
    * 验证EXCEL文件
    * @param fileType 文件类型
    * @return
    */
    private static boolean validateExcel(String fileType){
        if (fileType == null || !(isExcel_xls(fileType) || isExcel_xlsx(fileType))){
            log.error("文件类型不是excel格式");
            return false;
        }
        return true;
    }
    /**
    * 读EXCEL文件
    */
    private static Workbook getExcelWorkBook(MultipartFile uploadFile) {//
        Workbook wb = null;
        String originalFilename = uploadFile.getOriginalFilename();// 获取文件名，带后缀
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();// 获取文件的后缀格式
        InputStream inputStream = null;
        if(!validateExcel(fileType))//验证文件名是否合格
        return null;
        try {
            inputStream = uploadFile.getInputStream();
            if(isExcel_xls(fileType))
                wb = new HSSFWorkbook(inputStream);
            if(isExcel_xlsx(fileType))
                wb = new XSSFWorkbook(inputStream);
        }catch (IOException e) {
            log.error(e.getMessage());
        }
        return wb;
    }

    /**
    * 读取Excel里面sheet
    * @param wb
    * @return
    */
    private static ExcelDetail getExcelInfo(Workbook wb){
        if(wb == null) return null;
        //得到第一个shell
        Sheet sheet=wb.getSheetAt(0);
        int totalRows;//Excel的行数 从1开始
        int totalCells = 0;//Excel的列数
        totalRows=sheet.getPhysicalNumberOfRows();//得到Excel的行数
        if(totalRows>=1 && sheet.getRow(0) != null){//得到Excel的列数(前提是有行数)
            totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
        }
        return new ExcelDetail(sheet,totalRows,totalCells);
    }

    private static void initSheet(int totalRows, int totalCells, Sheet sheet){
        Row row = null;
        for(int i=2; i<totalRows; ++i){
            row = sheet.getRow(i);
            for(int j=0; j<totalCells; ++j){
                Cell cell = row.getCell(j);
                if(cell!=null) cell.setCellType(CellType.STRING);
            }
        }
    }
    private static String getCellValue(Row row,int cellNum){
        if(row == null) return "";
        Cell cell = row.getCell(cellNum);
        if(cell == null) return "";
        return cell.getStringCellValue();
    }
    private static List<String> getCellArrayValue(Row row, int cellNum, String separation){
        if(row == null) return null;
        Cell cell = row.getCell(cellNum);
        if(cell == null) return null;
        String[] val = cell.getStringCellValue().split(separation);
        List<String> list = new ArrayList<>();
        for(String s : val){
            list.add(s);
        }
        return list;
    }
    private static <T> List<T> getCellArrayValue(Row row, int cellNum, Class<T> cla){
        if(row == null) return null;
        Cell cell = row.getCell(cellNum);
        if(cell == null) return null;
        return JSON.parseArray(cell.getStringCellValue(),cla);
    }
    // @描述：是否是xls的excel
    private static boolean isExcel_xls(String fileType)  {
    return fileType.equalsIgnoreCase("xls");
    }

    //@描述：是否是xlsx的excel
    private static boolean isExcel_xlsx(String fileType)  {
    return fileType.equalsIgnoreCase("xlsx");
    }
    private static String getNewProblem(StringBuffer problem, List<String> answerList){
        int beginIdx = -1;
        for (int i = 0; i < problem.length(); i++) {
            if(problem.charAt(i) == '#' && problem.charAt(i+1) == '{'){
                i++;
                beginIdx = i;
                continue;
            }
            if(problem.charAt(i) == '}'){
                if(beginIdx != -1){
                    String answer = problem.substring(beginIdx+1,i);
                    answerList.add(answer);
                    problem.delete(beginIdx+1,i);
                    i = beginIdx+1;
                    beginIdx = -1;
                }
            }
        }
        return problem.toString();//.replace("#{}","___");
    }




    private static class ExcelDetail{
        Sheet sheet = null;
        int totalRows;
        int totalCells;
        public ExcelDetail(Sheet sheet, int totalRows, int totalCells) {
            this.sheet = sheet;
            this.totalRows = totalRows;
            this.totalCells = totalCells;
        }
    }
    private static class Message{
        private Map<Integer,String> errMap = new LinkedHashMap<>();
        public void getErrMsg(StringBuffer errMsg){
            if(errMap.isEmpty()){
                errMsg.append("无错误信息");
                return;
            }
            for (Integer integer : errMap.keySet()) {
                errMsg.append("第"+integer+"行数据错误,错误原因："+errMap.get(integer)+"\n");
            }
        }
        public void addErrMsg(Integer errRow, String errMsg){
            errMap.put(errRow,errMsg);
        }
    }
    public static List<SingleChoice> getSingleChoiceList(MultipartFile excelFile,StringBuffer errMsg){
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null){
            return null;
        }
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();
        Row row = null;
        List<SingleChoice> problemList = new ArrayList<SingleChoice>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        SingleChoice build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String problem = getCellValue(row,0);
                String choice_a = getCellValue(row,1);
                String choice_b = getCellValue(row,2);
                String choice_c = getCellValue(row,3);
                String choice_d = getCellValue(row,4);
                String answer = getCellValue(row,5);
                String cellValue = getCellValue(row,6);
                float difficultyVal = Float.parseFloat(cellValue.isEmpty()?"0":cellValue);
                String analyze = getCellValue(row,7);
                List<String> knowledgeList = getCellArrayValue(row,8,"\\$");
                String choice_e = getCellValue(row,9);
                String choice_f = getCellValue(row,10);
                String choice_g = getCellValue(row,11);
                String choice_h = getCellValue(row,12);

                build = SingleChoice.builder()
                        .problem(problem)
                        .choice_a(choice_a)
                        .choice_b(choice_b)
                        .choice_c(choice_c)
                        .choice_d(choice_d)
                        .choice_e(choice_e)
                        .choice_f(choice_f)
                        .choice_g(choice_g)
                        .choice_h(choice_h)
                        .answer(answer)
                        .analysis(analyze)
                        .difficultyVal(difficultyVal)
                        .knowledgeList(knowledgeList)
                        .build();
            }catch (Exception e){
                //e.printStackTrace();
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if(build == null || build.getProblem() == null || build.getProblem().isBlank()) build = null;
            log.debug(build.toString());
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                problemList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  problemList;
    }
    public static List<Teacher> getTeacherList(MultipartFile excelFile,StringBuffer errMsg){
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;//下标从0开始
        Message message = new Message();
        Row row = null;
        List<Teacher> teacherList = new ArrayList<Teacher>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Teacher build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String name = getCellValue(row,0);
                String account = getCellValue(row,1);
                String code = getCellValue(row,2);
                String tno = getCellValue(row,3);
                String college = getCellValue(row,4);
                String profession = getCellValue(row,5);
                String birthday = getCellValue(row,6);
                boolean sex = getCellValue(row,7) == "男" ? true : false;
                String phone = getCellValue(row,8);
                String email = getCellValue(row,9);
                boolean enabled = getCellValue(row,10) == "开通" ? true : false;
                build = Teacher.builder().name(name).account(account).code(code).tno(tno).college(college)
                        .profession(profession).birthday(new java.sql.Date(new SimpleDateFormat("yyyy/mm/dd").parse(birthday).getTime()))
                        .sex(sex).phone(phone).email(email).enabled(enabled).build();
            }catch (Exception e){
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if (build != null) {
                teacherList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return teacherList;
    }
    public static List<MultipleChoice> getMultipleChoiceList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();
        Row row = null;
        List<MultipleChoice> problemList = new ArrayList<MultipleChoice>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        MultipleChoice build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String problem = getCellValue(row,0);
                String choice_a = getCellValue(row,1);
                String choice_b = getCellValue(row,2);
                String choice_c = getCellValue(row,3);
                String choice_d = getCellValue(row,4);
                List<String> answer = getCellArrayValue(row,5,"\\$");
                String cellValue = getCellValue(row,6);
                float difficultyVal = Float.parseFloat(cellValue.isEmpty()?"0":cellValue);
                String analyze = getCellValue(row,7);
                List<String> knowledgeList = getCellArrayValue(row,8,"\\$");
                String choice_e = getCellValue(row,9);
                String choice_f = getCellValue(row,10);
                String choice_g = getCellValue(row,11);
                String choice_h = getCellValue(row,12);

                build = MultipleChoice.builder()
                        .problem(problem)
                        .choice_a(choice_a)
                        .choice_b(choice_b)
                        .choice_c(choice_c)
                        .choice_d(choice_d)
                        .choice_e(choice_e)
                        .choice_f(choice_f)
                        .choice_g(choice_g)
                        .choice_h(choice_h)
                        .answer(answer)
                        .analysis(analyze)
                        .difficultyVal(difficultyVal)
                        .knowledgeList(knowledgeList)
                        .build();
            }catch (Exception e){
                //e.printStackTrace();
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if(build == null || build.getProblem() == null || build.getProblem().isBlank()) build = null;
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                problemList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  problemList;
    }
    public static List<Judge> getJudgeList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();
        Row row = null;
        List<Judge> problemList = new ArrayList<Judge>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Judge build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String problem = getCellValue(row,0);
                boolean answer = getCellValue(row,1).equals("对") ? true : false;
                String cellValue = getCellValue(row,2);
                float difficultyVal = Float.parseFloat(cellValue.isEmpty()?"0":cellValue);
                String analysis = getCellValue(row,3);
                List<String> knowledgeList = getCellArrayValue(row,4,"\\$");
                build = Judge.builder()
                        .problem(problem)
                        .answer(answer)
                        .analysis(analysis)
                        .difficultyVal(difficultyVal)
                        .knowledgeList(knowledgeList)
                        .build();
            }catch (Exception e){
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if(build == null || build.getProblem() == null || build.getProblem().isBlank()) build = null;
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                problemList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  problemList;
    }
    public static List<Fill> getFillList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();
        Row row = null;
        List<Fill> problemList = new ArrayList<Fill>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Fill build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            List<String> answerList = new ArrayList<>();
            try{
                String problem = getNewProblem(new StringBuffer(getCellValue(row,0)),answerList);
                String cellValue = getCellValue(row,1);
                float difficultyVal = Float.parseFloat(cellValue.isEmpty()?"0":cellValue);
                String analysis = getCellValue(row,2);
                List<String> knowledgeList = getCellArrayValue(row,3,"\\$");

                build = Fill.builder()
                        .problem(problem)
                        .answer(answerList)
                        .analysis(analysis)
                        .difficultyVal(difficultyVal)
                        .knowledgeList(knowledgeList)
                        .build();
            }catch (Exception e){
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if(build == null || build.getProblem() == null || build.getProblem().isBlank()) build = null;
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                problemList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  problemList;
    }

    public static List<Short> getShortList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();
        Row row = null;
        List<Short> problemList = new ArrayList<Short>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Short build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String problem = getCellValue(row,0);
                String answer = getCellValue(row,1);
                String cellValue = getCellValue(row,2);
                float difficultyVal = Float.parseFloat(cellValue.isEmpty()?"0":cellValue);
                String analysis = getCellValue(row,3);
                List<String> knowledgeList = getCellArrayValue(row, 4, "\\$");
                build = Short.builder()
                        .problem(problem)
                        .answer(answer)
                        .analysis(analysis)
                        .difficultyVal(difficultyVal)
                        .knowledgeList(knowledgeList)
                        .build();
            }catch (Exception e){
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if(build == null || build.getProblem() == null || build.getProblem().isBlank()) build = null;
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                problemList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  problemList;

    }
    public static List<Manager> getManagerList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;
        Message message = new Message();

        Row row = null;
        List<Manager> list = new ArrayList<Manager>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Manager build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String name = getCellValue(row,0);
                String account = getCellValue(row,1);
                String code = getCellValue(row,2);
                String mno = getCellValue(row,3);
                String birthday = getCellValue(row,4);
                boolean sex = getCellValue(row,5) == "男" ? true : false;
                String phone = getCellValue(row,6);
                String email = getCellValue(row,7);
                boolean enabled = getCellValue(row,8) == "开通" ? true : false;
                build = Manager.builder()
                        .name(name)
                        .account(account)
                        .code(code)
                        .mno(mno)
                        .birthday(new java.sql.Date(new SimpleDateFormat("yyyy/mm/dd").parse(birthday).getTime()))
                        .sex(sex)
                        .phone(phone)
                        .email(email)
                        .enabled(enabled)
                        .build();
            }catch (Exception e){
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if (build != null) {
                build.setUuid(Utils.randomUuid());
                list.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return  list;

    }
    public static List<Student> getStudentList(MultipartFile excelFile, StringBuffer errMsg) {
        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
        if(excelDetail == null) return null;
        Sheet sheet = excelDetail.sheet;//sheet表格
        int totalRows = excelDetail.totalRows;//Excel的行数
        int totalCells = excelDetail.totalCells;//Excel的列数
        int rowStart = 2;//下标从0开始
        Message message = new Message();
        Row row = null;
        List<Student> studentList = new ArrayList<Student>();
        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
        Student build = null;
        for(int i=rowStart; i<totalRows; ++i){
            row = sheet.getRow(i);
            try{
                String name = getCellValue(row,0);
                String account = getCellValue(row,1);
                String code = getCellValue(row,2);
                String sno = getCellValue(row,3);
                String college = getCellValue(row,4);
                String profession = getCellValue(row,5);
                String birthday = getCellValue(row,6);
                boolean sex = getCellValue(row,7) == "男" ? true : false;
                String phone = getCellValue(row,8);
                String email = getCellValue(row,9);
                boolean enabled = getCellValue(row,10) == "开通" ? true : false;
                build = Student.builder()
                        .uuid(Utils.randomUuid())
                        .name(name)
                        .account(account)
                        .code(code)
                        .sno(sno)
                        .college(college)
                        .profession(profession)
                        .birthday(new java.sql.Date(new SimpleDateFormat("yyyy/mm/dd").parse(birthday).getTime()))
                        .sex(sex)
                        .phone(phone)
                        .email(email)
                        .enabled(enabled)
                        .build();
            }catch (Exception e){
                //e.printStackTrace();
                message.addErrMsg(i-1,e.getMessage());
                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
                build = null;
            }
            if (build != null) {
                studentList.add(build);
            }
        }
        message.getErrMsg(errMsg);
        return studentList;
    }
//    public static List<Program> getProgramList(MultipartFile excelFile, StringBuffer errMsg) {
//        ExcelDetail excelDetail = getExcelInfo(getExcelWorkBook(excelFile));
//        if(excelDetail == null) return null;
//        Sheet sheet = excelDetail.sheet;//sheet表格
//        int totalRows = excelDetail.totalRows;//Excel的行数
//        int totalCells = excelDetail.totalCells;//Excel的列数
//        int rowStart = 2;//下标从0开始
//        Message message = new Message();
//        Row row = null;
//        List<Program> programList = new ArrayList<Program>();
//        initSheet(totalRows,totalCells,sheet);//设置 Cells : CellType(CellType.STRING);
//        Program build = null;
//        for(int i=rowStart; i<totalRows; ++i){
//            row = sheet.getRow(i);
//            try{
//                String name = getCellValue(row,0);
//                String account = getCellValue(row,1);
//                String code = getCellValue(row,2);
//                String sno = getCellValue(row,3);
//                String college = getCellValue(row,4);
//                String profession = getCellValue(row,5);
//                String birthday = getCellValue(row,6);
//                boolean sex = getCellValue(row,7) == "男" ? true : false;
//                String phone = getCellValue(row,8);
//                String email = getCellValue(row,9);
//                boolean enabled = getCellValue(row,10) == "开通" ? true : false;
//
//            }catch (Exception e){
//                //e.printStackTrace();
//                message.addErrMsg(i-1,e.getMessage());
//                log.error("第{}条记录错误,错误信息：{}",i-1,e.getMessage());
//                build = null;
//            }
//            if (build != null) {
//                programList.add(build);
//            }
//        }
//        message.getErrMsg(errMsg);
//        return programList;
//    }
}
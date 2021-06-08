package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.services.teacher.PaperStrategyService;
import com.wangzhen.utils.Utils;
import com.wangzhen.utils.pdf.PaperToPdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/5 17:30
 */
@Controller
@RequestMapping("/teacher")
@Slf4j
public class PaperPdfController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private PaperStrategyService paperStrategyService;
    @RequestMapping("/paperpdf/{uuid}")
    public void getPaperPdf(@PathVariable("uuid") String uuid, HttpServletResponse response) throws IOException {
        Paper paper = paperService.selectPaperByPaper_uuid(uuid);
        if(paper == null){
            return;
        }
        String fileName = paper.getPaperName() + ".pdf";
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));

        paper.setPaperStrategy(paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paper.getPaperStrategy_uuid()));
        File file = new File("C:/ExemSystem/" +Utils.randomUuid() + fileName);

        if(!file.exists()){
            file.createNewFile();
        }
        try {
            boolean b = PaperToPdfUtil.toPdf(paper, file);
            if(b){
                byte[] buff = new byte[1024];
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    os = response.getOutputStream();
                    bis = new BufferedInputStream(new FileInputStream(file));
                    int i = bis.read(buff);
                    while (i != -1) {
                        os.write(buff, 0, buff.length);
                        os.flush();
                        i = bis.read(buff);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("success");
            }
        } catch (Exception e) {
            log.error("捕捉到异常："+e.getMessage());
            e.printStackTrace();
        }
    }
}

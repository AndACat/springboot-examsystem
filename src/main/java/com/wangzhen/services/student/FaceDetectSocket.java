package com.wangzhen.services.student;

import com.alibaba.fastjson.JSON;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.enums.DetectMode;
import com.wangzhen.staticparamter.FaceIdentityStaticParamter;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.utils.Utils;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketDecoderConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/25 15:45
 */
@ServerEndpoint(path="/faceDetect",port = "81",maxFramePayloadLength = "105536000")
@Component
@Slf4j
public class FaceDetectSocket {
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    private StudentPaperAnswerService studentPaperAnswerService;
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers) throws IOException {
        session.channel().pipeline().addLast(new WebSocketServerProtocolHandler("/faceDetect",null, false, Integer.MAX_VALUE*10000, false) );
        ChannelConfig config = session.config();
        session.setAttribute("zbList",new ArrayList<>());

//        session.channel().pipeline().addBefore("11122","22233",new )
        FaceIdentityUtil videoFaceEngine = new FaceIdentityUtil(DetectMode.ASF_DETECT_MODE_VIDEO);
        session.setAttribute("videoFaceEngine",videoFaceEngine);
        System.out.println("人脸偏角度检测通道初始化成功...");
    }
    @OnClose
    public void onClose(Session session) throws IOException, InterruptedException {
        System.out.println("人脸偏角度检测通道关闭...");
        String student_uuid = session.getAttribute("student_uuid");
        String paper_uuid = session.getAttribute("paper_uuid");
        List<String> zbList = session.getAttribute("zbList");
        Thread.sleep(1000);
        studentPaperAnswerService.addZbList(student_uuid,paper_uuid,zbList);
        FaceIdentityUtil videoFaceEngine = session.getAttribute("videoFaceEngine");
        videoFaceEngine.close();
        session.close();
    }
    @OnError
    public void onError(Session session, Throwable throwable) throws InterruptedException {
        throwable.printStackTrace();
        String student_uuid = session.getAttribute("student_uuid");
        String paper_uuid = session.getAttribute("paper_uuid");
        List<String> zbList = session.getAttribute("zbList");
        Thread.sleep(1000);
        studentPaperAnswerService.addZbList(student_uuid,paper_uuid,zbList);
        FaceIdentityUtil videoFaceEngine = session.getAttribute("videoFaceEngine");
        videoFaceEngine.close();
        session.close();
    }
    @OnMessage
    public void OnMessage(Session session, String message) throws IOException {
        log.info("OnMessage:"+message);
        String[] split = message.split(":");
        if(split.length == 2 && split[0].equals("student_uuid")){
            session.setAttribute("student_uuid",split[1]);
        }
        if(split.length == 2 && split[0].equals("paper_uuid")){
            session.setAttribute("paper_uuid",split[1]);
        }
        session.sendText("msg保存成功");
    }
    @OnBinary
    public void OnBinary(Session session,byte[] bytes) throws IOException {
        System.out.println("bytes长度:" + bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        FaceIdentityUtil videoFaceEngine = session.getAttribute("videoFaceEngine");
        Long startTime = System.currentTimeMillis();
        List<Face3DAngle> face3DAngleList = videoFaceEngine.getFace3DAngle(inputStream);
        Long endTime = System.currentTimeMillis();
        System.out.println("人脸检测时间:" + (endTime - startTime));
        if (face3DAngleList.size() != 0) {
            session.sendText(JSON.toJSONString(face3DAngleList.get(0)));
        } else {
            session.sendText("没有检测到人脸");
        }
        saveZbImg(session, face3DAngleList, bytes);

    }
//        ByteArrayInputStream inputStream1= new ByteArrayInputStream(bytes);
//        Long startTime = System.currentTimeMillis();
//        List<FaceFeature> face3DAngle = FaceIdentityUtil.getFaceFeature(inputStream1);
//        Long endTime = System.currentTimeMillis();
//        System.out.println("人脸检测时间:"+(endTime-startTime));
//        if(face3DAngle.size() != 0){
//            session.sendText(JSON.toJSONString(face3DAngle.get(0)));
//        }else{
//            session.sendText("没有检测到人脸");
//        }
//    }
    private void saveZbImg(Session session,List<Face3DAngle> face3DAngleList,byte[] bytes) throws IOException {
        if(face3DAngleList.size() != 0){
            Face3DAngle face3DAngle = face3DAngleList.get(0);
            float pitch = face3DAngle.getPitch();
            float yaw = face3DAngle.getYaw();
            float roll = face3DAngle.getRoll();
            if(Math.abs(yaw) > 20f){
                //保存图片
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                String faceImgName = Utils.randomUuid() + ".jpg";
                File file = new File( uploadStaticParamter.getExamFaceDetectLocalPath() ,faceImgName);
                if(!file.exists()){
                    file.createNewFile();
                }
                OutputStream os = new FileOutputStream(file);
                is.transferTo(os);
                List<String> zbList = session.getAttribute("zbList");
                zbList.add("/examFaceDetectImgs/"+faceImgName);
                session.setAttribute("zbList",zbList);
            }
        }
        if(face3DAngleList == null || face3DAngleList.size() == 0){
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            String faceImgName = Utils.randomUuid() + ".jpg";
            File file = new File( uploadStaticParamter.getExamFaceDetectLocalPath() ,faceImgName);
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            is.transferTo(os);
            List<String> zbList = session.getAttribute("zbList");
            zbList.add("//examFaceDetectImgs/"+faceImgName);
            session.setAttribute("zbList",zbList);
        }
    }
}

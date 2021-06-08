# 二本大学2020年软件工程专业毕业设计
## 技术构成
1. springboot
2. spring
3. spring security
4. jquery
5. adminlte 前端模板 基于bootstrap
6. 一堆js插件，差不多都是bootstrap的
7. 虹软人脸识别开发包
8. pdf试卷生成
9. mysql
## 运行要求的必备环境
1. mysql8
2. jdk11
## 运行部署方法
1. 将examsystem.sql 部署到mysql8
2. 修改application.yml文件
3. 启动 java -jar jar包名      (可以直接使用start.bat)
4. 访问localhost:8080
## 开发要求的必备环境
1. mysql8
2. jdk11
3. maven 3.7
4. idea
5. 具体依赖，请自行查看pom文件
## 开发部署方法
1. 将examsystem.sql 部署到mysql8
2. 修改application.yml文件
3. idea中启动
4. 访问localhost:8080
## application.yml文件修改注释
1. 数据库账号密码记得改
2. 默认端口号是80（不是8080）
3. arcsoft-static-paramter底下的变量要配置好，其中faceEngineLibPath指向你resources/faceLib/*的绝对目录
4. upload-static-paramter底下的路径，共三个，faceFolderlocalPath、problemFolderLocalPath、examFaceDetectLocalPath、要确保你的系统中存在这些路径
## 重要提示
resources/faceLib/*是虹软人脸识别所需的dll包，没有这些包，系统中的人脸识别将会无法启动，同时还应在application.yml中正确配置开发包的绝对路径
## 项目论文
项目自带论文，在根目录下，可自行查看
## 本人长期接毕业设计（springboot相关的，网站类型的），QQ1677688026，有需要的可以联系我（价格实惠）

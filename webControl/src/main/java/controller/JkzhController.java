package controller;

import com.alibaba.fastjson.JSON;
import common.Constants;
import common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import request.JkzhRequest;
import service.JkzhService;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Api(value = "文档生成", tags = {"基坑支护"})
@RestController
@RequestMapping(value = {"/yzb/jkzh/v1"})
public class JkzhController {

    @Autowired
    private JkzhService jkzhService;

    @ApiOperation(value = "创建文档", notes = "基坑支护")
    @PostMapping(value = "/creactWord")
    public Result<Boolean> creactWord(@RequestBody JkzhRequest jkzhRequest){
        log.info("creactWord:{}",JSONObject.toJSONString(jkzhRequest));
        try {
            Result<Boolean> booleanResult = jkzhService.creactWord(jkzhRequest);
            return booleanResult;
        }catch (Exception e) {
            log.error("creactWord error:{}",e);
        }
        return Result.newSuccessResult().fail("创建文档文档失败!");
    }

    @ApiOperation(value = "文档生成并下载", notes = "基坑支护")
    @PostMapping(value = "/creactAndExportWord", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void creactAndExportWord(@RequestBody JkzhRequest jkzhRequest) throws IOException {
        log.info("creactAndExportWord jkzhRequest:{}", JSONObject.toJSONString(jkzhRequest));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = null;
        //创建输出流
        OutputStream oupstream = null;
        // 获得request对象,response对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
            Result<String> stringResult = jkzhService.creactAndExportWord(jkzhRequest);
            response.setContentType("application;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String fileName = this.getClass().getClassLoader().getResource(Constants.Download + stringResult.getData() + ".docx").getPath();//获取文件路径
            File file = new File(fileName);
            //如果文件不存在
            if (!file.exists()) {
                log.error("word is not exist!");
            }
            response.setHeader("Content-disposition", "attachment;filename=" + stringResult.getData() + ".docx");
            //读取要下载的文件，保存到文件输入流
            in = new FileInputStream(file);
            //创建输出流
            oupstream = response.getOutputStream();
            //创建缓冲区
            byte buffer[] = new byte[2048];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                //输出缓冲区的内容到浏览器，实现文件下载
                oupstream.write(buffer, 0, len);
            }
            oupstream.flush();
        } catch (Exception e) {
            log.info("下载文件失败:{}", e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        } finally {
            if (null != in) {
                //关闭文件输入流
                in.close();
            }
            if (null != oupstream) {
                //关闭输出流
                oupstream.close();
            }
        }
    }
}

package service;

import common.Result;
import request.JkzhRequest;

import java.io.IOException;

public interface JkzhService {

    /**
     * 创建基坑支护文档
     * @param jkzhRequest
     * @return
     */
    public Result<Boolean> creactWord(JkzhRequest jkzhRequest) throws IOException;

    /**
     * 创建基坑支护文档，并下载
     * @param jkzhRequest
     */
    public Result<String> creactAndExportWord(JkzhRequest jkzhRequest) throws IOException;
}

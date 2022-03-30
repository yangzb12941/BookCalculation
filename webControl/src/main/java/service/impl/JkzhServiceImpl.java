package service.impl;

import common.Constants;
import common.Result;
import domain.JkzhCalDomain;
import lombok.extern.slf4j.Slf4j;
import request.JkzhRequest;
import service.JkzhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class JkzhServiceImpl implements JkzhService {

    @Autowired
    JkzhCalDomain jkzhCalDomain;

    @Override
    public Result<Boolean> creactWord(JkzhRequest jkzhRequest) throws IOException {
        jkzhCalDomain.writeToWord(Constants.TemplateFile,Constants.Download,jkzhRequest);
        return Result.newSuccessResult();
    }

    @Override
    public Result<String> creactAndExportWord(JkzhRequest jkzhRequest) throws IOException {
        String fileName = jkzhCalDomain.writeToWord(Constants.TemplateFile, Constants.Download, jkzhRequest);
        return new Result().success(fileName);
    }
}

package com.wk.common.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.common.vo.BaseVo;

public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected void writeJsonResponse(BaseVo vo, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter w = new StringWriter();
        try {
            objectMapper.writeValue(w, vo);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Content-Encoding", "gzip");
        GZIPOutputStream out;
        try {
            out = new GZIPOutputStream(response.getOutputStream());
            out.write(w.toString().getBytes("utf-8"));
            out.finish();
            out.flush();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}

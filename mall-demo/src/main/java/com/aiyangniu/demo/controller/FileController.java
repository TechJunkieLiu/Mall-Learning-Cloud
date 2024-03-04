package com.aiyangniu.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.common.utils.FileUtil;
import com.aiyangniu.demo.dto.FileProperties;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件上传、下载、压缩、解压测试类
 *
 * @author lzq
 * @date 2024/03/01
 */
@Slf4j
@Api(value = "FileController", tags = "文件上传、下载、压缩、解压测试类")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileController {

    private final FileProperties fileProperties;

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/uploadToServer")
    public String uploadToServer(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
        JSONObject json = new JSONObject();
        try {
            Pair<Boolean, String> pair = checkFile(file);
            if (!pair.getLeft()) {
                json.put("msg", pair.getRight());
                return json.toJSONString();
            }
            boolean b = FileUtil.uploadToServer(file, fileProperties.getUploadPath(), file.getOriginalFilename());
            json.put("msg", b ? "上传成功" : "上传失败");
            return json.toJSONString();
        } catch (Exception e) {
            log.error("系统异常e:", e);
            json.put("msg", "上传失败");
            return json.toJSONString();
        }
    }

    @ApiOperation(value = "文件上传")
    @PostMapping("/newUploadToServer")
    public String newUploadToServer(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
        JSONObject json = new JSONObject();
        try {
            Pair<Boolean, String> pair = checkFile(file);
            if (!pair.getLeft()) {
                json.put("msg", pair.getRight());
                return json.toJSONString();
            }
            boolean b = FileUtil.newUploadToServer(file, fileProperties.getUploadPath(), file.getOriginalFilename());
            json.put("msg", b ? "上传成功" : "上传失败");
            return json.toJSONString();
        } catch (Exception e) {
            log.error("系统异常e:", e);
            json.put("msg", "上传失败");
            return json.toJSONString();
        }
    }

    @ApiOperation(value = "文件下载（远端下载到服务器，即通过一个地址获取到文件）")
    @GetMapping("/downloadToServer")
    public void downloadToServer() {
        String downloadUrl = "http://invoice.shenzhentong.com/downInvoice/downpdf/914403007703110594/TG643260802798850048";
        String filePath = "D:\\myFile\\download";
        String fileName = "download.pdf";
        FileUtil.downloadToServer(downloadUrl, filePath, fileName);
    }

    @ApiOperation(value = "文件下载（服务器下载到客户端，即浏览器请求服务器进行文件下载）")
    @GetMapping("/downloadToClient")
    public void downloadToClient(HttpServletRequest request, HttpServletResponse response) {
        String filePath = fileProperties.getDownloadPath();
        String fileName = "download.pdf";
        String filePathName = filePath + File.separator + fileName;
        BufferedInputStream bins = null;
        BufferedOutputStream bouts = null;
        try {
            // 同一个窗口下载多次，清除空白流
            response.reset();
            File file = new File(filePathName);
            if (!file.exists()) {
                log.error("要下载的文件不存在：{}！", filePathName);
                return;
            }
            bins = new BufferedInputStream(new FileInputStream(filePathName));
            bouts = new BufferedOutputStream(response.getOutputStream());
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            // 火狐浏览器
            if (userAgent.contains("firefox")) {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            // 设置发送到客户端的响应的内容类型
            response.setContentType("application/download");
            // 指定客户端下载的文件的名称
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bins.read(bytes)) != -1) {
                bouts.write(bytes, 0, len);
            }
            // 刷新流
            bouts.flush();
            log.info("下载完成！");
        } catch (IOException e) {
            log.error("下载文件异常:{}！", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (bouts != null) {
                    bouts.close();
                }
                if (bins != null) {
                    bins.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常！", e);
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "压缩、解压文件（单文件、单文件夹多文件）")
    @GetMapping("/zipCompress")
    public void zipCompress() {
        String inputFileName = "download.pdf";
        String zipFileName = "compress.zip";
        // 测试压缩和解压缩单文件ZIP
        File inputFile = new File(fileProperties.getCompressPath() + File.separator + inputFileName);
        // ZIP文件路径
        File zipFile = new File(fileProperties.getCompressPath() + File.separator + zipFileName);
        // ZIP解压缩路径
        File unzipFile = new File(fileProperties.getDecompressPath());
        long start = System.currentTimeMillis();
        // 压缩文件
        FileUtil.zipCompress(inputFile, zipFile);
        long end = System.currentTimeMillis();
        System.out.println("ZIP-压缩单文件耗时:" + (end - start) + "毫秒");
        start = System.currentTimeMillis();
        FileUtil.zipDecompress(zipFile, unzipFile);
        end = System.currentTimeMillis();
        System.out.println("ZIP-解压缩单文件耗时:" + (end - start) + "毫秒");
    }

    public Pair<Boolean, String> checkFile(MultipartFile file) {
        if (null == file || file.isEmpty()) {
            return Pair.of(false, "文件为空！");
        }
        // 获取
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (StrUtil.isBlank(filename)) {
            return Pair.of(false, "文件名为空！");
        }
        // 字节
        long size = file.getSize();
        log.info("收到的请求文件信息：原生文件名：{}，文件类型：{}，文件大小：{}", filename, contentType, size);
        // 获取文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 判断配置的文件列表里是否支持该文件类型
        if (!ArrayUtils.contains(fileProperties.getFileTypeArray(), suffix)) {
            return Pair.of(false, "不支持该类型文件上传！");
        }
        // 单位KB
        double fileSize = size / 1024.0;
        if (fileSize > fileProperties.getMaxFileSize()) {
            return Pair.of(false, "文件大小超过限制！");
        }
        return Pair.of(true, "验证通过！");
    }
}

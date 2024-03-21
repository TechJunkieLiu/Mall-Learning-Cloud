package com.aiyangniu.common.utils;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件上传、下载、压缩、解压工具类
 * Java自带的java.util.zip类库是一个基础的压缩和解压缩类库，适合处理小型文件或数据
 * 一些第三方的类库如 ApacheCommonsCompress、Zip4j，适合处理大型文件或数据，提供了更高级的压缩和解压缩功能以及更好的性能
 *
 * @author lzq
 * @date 2024/03/01
 */
public class FileUtil {

    private static final Logger logger = LogManager.getLogger(FileUtil.class);

    /**
     * 上传文件（原生）
     */
    public static boolean uploadToServer(MultipartFile multiFile, String uploadPath, String uploadFileName) {
        // 构建文件对象
        File file = new File(uploadPath);
        // 文件目录不存在则递归创建目录
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                logger.error("创建文件夹异常！");
                return false;
            }
        }
        InputStream ins = null;
        FileOutputStream outs = null;
        try {
            // 获取文件输入流
            ins = multiFile.getInputStream();
            // 构建文件输出流
            outs = new FileOutputStream(uploadPath + uploadFileName);
            int len;
            byte[] bytes = new byte[1024];
            // 读取一个bytes的文件内容
            while ((len = ins.read(bytes)) != -1) {
                outs.write(bytes, 0, len);
            }
            outs.close();
            logger.info("上传成功：{}", uploadPath + uploadFileName);
            return true;
        } catch (IOException e) {
            logger.error("文件上传异常！");
            e.printStackTrace();
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                logger.error("关闭流异常！");
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 上传文件（SpringBoot自带工具类FileCopyUtils）
     */
    public static boolean newUploadToServer(MultipartFile multiFile, String uploadPath, String uploadFileName) {
        // 构建文件对象
        File file = new File(uploadPath);
        // 文件目录不存在则递归创建目录
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                logger.error("创建文件夹异常！");
                return false;
            }
        }
        try {
            // 获取文件输入流
            InputStream inputStream = multiFile.getInputStream();
            // 构建文件输出流
            FileOutputStream outputStream = new FileOutputStream(uploadPath + uploadFileName);
            int copy = FileCopyUtils.copy(inputStream, outputStream);
            logger.info("上传成功，文件大小：{}", copy);
            return true;
        } catch (IOException e) {
            logger.error("文件上传异常！", e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载文件到服务器
     *
     * @param downloadUrl 下载文件地址
     * @param downloadPath 文件存储路径
     * @param downloadFileName 文件存储名称
     */
    public static boolean downloadToServer(String downloadUrl, String downloadPath, String downloadFileName) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        boolean flag = false;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            bis = new BufferedInputStream(connection.getInputStream());
            File file = new File(downloadPath);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    logger.error("创建文件目录失败！");
                    return false;
                }
            }
            String filePathName = downloadPath + File.separator + downloadFileName;
            byte[] buf = new byte[1024];
            int size;
            fos = new FileOutputStream(filePathName);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            flag = true;
            logger.info("文件下载成功，文件路径[" + filePathName + "]");
            flag = true;
        } catch (Exception e) {
            logger.error("下载文件异常！", e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                logger.error("关流异常！", e);
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 下载文件到客户端
     *
     * @param isData 是否需要根据模板导出数据
     * @param filePathName 下载文件地址(含文件名)
     * @param fileName 下载文件名称
     * @param paramMap 导出数据
     * @param request HTTP请求
     * @param response HTTP响应
     */
    public static void downloadToClient(Boolean isData, String filePathName, String fileName, Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        BufferedInputStream bins = null;
        BufferedOutputStream bouts = null;
        try {
            // 同一个窗口下载多次，清除空白流
            response.reset();
            File file = new File(filePathName);
            if (!file.exists()) {
                logger.error("要下载的文件不存在：{}！", filePathName);
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
            // 是否根据模板导出数据
            if (isData && null != paramMap) {
                XLSTransformer transformer = new XLSTransformer();
                Workbook workbook = transformer.transformXLS(bins, paramMap);
                workbook.write(bouts);
            }else {
                int len;
                byte[] bytes = new byte[1024];
                while ((len = bins.read(bytes)) != -1) {
                    bouts.write(bytes, 0, len);
                }
            }
            // 刷新流
            bouts.flush();
            logger.info("下载完成！");
        } catch (Exception e) {
            logger.error("下载文件异常:{}！", e.getMessage());
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
                logger.error("关闭流异常！", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件（支持单个文件和单个文件夹）
     *
     * @param sourceFile 被压缩文件、文件夹
     * @param zipFile zip文件
     */
    public static void zipCompress(File sourceFile, File zipFile) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // 设置压缩方法
            zos.setMethod(ZipOutputStream.DEFLATED);
            // 压缩级别，默认为-1（1-速度快，效率低 9-速度慢，效率高）
            zos.setLevel(Deflater.BEST_COMPRESSION);
            zos.setComment("zip文件说明");
            // 处理文件夹
            if (sourceFile.exists() && sourceFile.isDirectory() && Objects.nonNull(sourceFile.listFiles())){
                Arrays.stream(Objects.requireNonNull(sourceFile.listFiles())).forEach(file -> {
                    // 向ZIP中添加文件 源文件 zip输出流
                    addZipFile(file, zos);
                });
            }else{
                addZipFile(sourceFile, zos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addZipFile(File file, ZipOutputStream zos){
        if (!file.exists() || file.isDirectory()){
            throw new RuntimeException("文件不存在或该文件为文件夹，请检查！");
        }
        try {
            // 读入文件
            FileInputStream fis = new FileInputStream(file);
            // 创建压缩对象并设置一些属性
            ZipEntry entry = new ZipEntry(file.getName());
            // 压缩方法，当使用ZipEntry.STORED不压缩时，需要设置未压缩的数据大小和CRC-32校验码，否则压缩和解压缩时会出现错误
            entry.setMethod(ZipEntry.DEFLATED);
            // entry.setMethod(ZipEntry.STORED);
            // 设置未压缩的数据大小，这里设置的是文件大小
            entry.setSize(file.length());
            // 计算 CRC-32 校验码
            // byte[] data = Files.readAllBytes(file.toPath());
            // CRC32 crc = new CRC32();
            // crc.update(data);
            // 设置CRC-32校验码，用于保证压缩后的数据完整性，尽量别手动设置，可以通过CRC-32计算
            // entry.setCrc(crc.getValue());
            // 设置压缩后的数据大小，这里设置的是使用DEFLATED方法压缩后的数据大小
            entry.setCompressedSize(file.length());
            // 设置额外的数据，这里设置为空
            entry.setExtra(new byte[]{});
            // 设置ZipEntry的注释，即文件说明
            entry.setComment("file comment");
            // 设置文件的创建时间
            entry.setCreationTime(FileTime.from(Instant.now()));
            // 设置文件的最后访问时间
            entry.setLastAccessTime(FileTime.from(Instant.now()));
            // 设置文件的最后修改时间
            entry.setLastModifiedTime(FileTime.from(Instant.now()));
            // 向ZIP输出流中添加一个ZIP实体，构造方法中的name参数指定文件在ZIP包中的文件名
            zos.putNextEntry(entry);
            // 向ZIP实体中写入内容
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            // 关闭ZipEntry
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩ZIP文件
     *
     * @param zipFile ZIP文件
     * @param destDir 目标路径
     */
    public static void zipDecompress(File zipFile, File destDir) {
        byte[] buffer = new byte [1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File file = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

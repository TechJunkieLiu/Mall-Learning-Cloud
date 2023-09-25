package com.aiyangniu.common.utils;

import cn.hutool.core.img.ImgUtil;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 图片工具类
 *
 * @author lzq
 * @date 2023/09/25
 */
public class ImageUtil {

    /**
     * 校验图片文件长宽比例
     *
     * @param file   待校验文件
     * @param toCheckImageWidth  待校验图片宽度
     * @param toCheckImageHeight 待校验图图片长度
     * @return 布尔结果
     */
    public static boolean checkImageWidthAndHeight(File file, final int toCheckImageWidth, final int toCheckImageHeight) {
        if (Objects.isNull(file) || !file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImgUtil.read(file);
        int srcWidth = bufferedImage.getWidth();
        int srcHeight = bufferedImage.getHeight();

        return srcWidth <= toCheckImageWidth && srcHeight <= toCheckImageHeight;
    }

    /**
     * 图片尺寸调整，主要用于压缩处理
     *
     * @param file         待处理文件
     * @param imageWidth   处理的宽度
     * @param imageHeight  处理的高度
     */
    public static void resize(File file, final int imageWidth, final int imageHeight) {
        try {
            Thumbnails.of(file).size(imageWidth, imageHeight).toFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

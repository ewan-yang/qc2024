package com.tecpie.platform.common.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author Tanzj
 */
@Component
public class ZXingCode {

    /**
     * 默认是黑色
     */
    private static final int QR_COLOR = 0xFF000000;

    /**
     * 背景颜色
     */
    private static final int BG_WHITE = 0xFFFFFFFF;

    /**
     * 生成带logo的二维码图片
     *
     * @param content     二维码内容
     * @param logoPic     logo流
     * @param productName 商品名称   给二维码底部加内容
     * @return InputStream
     */
    public static InputStream getLogoQrCode(String content, InputStream logoPic, String productName) {
        try {
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.getQrCodeBufferedImage(content, BarcodeFormat.QR_CODE, 400, 400, zp.getDecodeHintType());
            return zp.addLogoQrCode(bim, logoPic, productName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给二维码图片添加Logo
     */
    public InputStream addLogoQrCode(BufferedImage bim, InputStream logoPic, String productName) {
        try {
            // 读取二维码图片，并构建绘图对象
            BufferedImage image = bim;
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            BufferedImage logo = ImageIO.read(logoPic);
            //  设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
            int widthLogo = Math.min(logo.getWidth(null), image.getWidth() * 3 / 10),
                    heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10) : logo.getWidth(null);
            //  logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.dispose();
            //把商品名称添加上去，商品名称不要太长哦，这里最多支持两行。太长就会自动截取啦
            if (productName != null && !"".equals(productName)) {
                //新的图片，把带logo的二维码下面加上文字
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                //画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                //画文字到新的面板
                outg.setColor(Color.BLACK);
                //字体、字型、字号
                outg.setFont(new Font("宋体", Font.BOLD, 30));
                int strWidth = outg.getFontMetrics().stringWidth(productName);
                if (strWidth > 399) {
                    // 长度过长就换行
                    String productName1 = productName.substring(0, productName.length() / 2);
                    String productName2 = productName.substring(productName.length() / 2);
                    int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
                    outg.drawString(productName1, 200 - strWidth1 / 2, image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setColor(Color.BLACK);
                    //字体、字型、字号
                    outg2.setFont(new Font("宋体", Font.BOLD, 30));
                    outg2.drawString(productName2, 200 - strWidth2 / 2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    outg.drawString(productName, 200 - strWidth / 2, image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12); //画文字
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
            }
            logo.flush();
            image.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            ImageIO.write(image, "png", baos);
            // 转换成ByteArrayInputStream流
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码bufferedImage图片
     *
     * @param content       编码内容
     * @param barcodeFormat 编码类型
     * @param width         图片宽度
     * @param height        图片高度
     * @param hints         设置参数
     * @return BufferedImage
     */
    public BufferedImage getQrCodeBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QR_COLOR : BG_WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return Map<EncodeHintType, Object>
     */
    public Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }

}

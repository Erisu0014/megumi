package com.erisu.cloud.megumi.util;

import cn.hutool.core.lang.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Description TODO
 * @Author alice
 * @Date 2021/6/16 17:15
 **/
@Component
public class ImageUtil {
    public String generateImage(String resourcePath, String text) throws IOException {
        File xcw_1 = ResourceUtils.getFile(resourcePath);
        BufferedImage image = ImageIO.read(xcw_1);
        Graphics g = image.getGraphics();
        Font font = new Font("simhei", Font.PLAIN, 16);
        g.setFont(font);
        g.setColor(Color.BLACK);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB); //消除锯齿状
        FontMetrics metrics = g.getFontMetrics(font);
//        AttributedString attributedText = new AttributedString("你是猪");
//        attributedText.addAttribute(TextAttribute.FONT, font);
//        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.black);
        int x = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int y = (image.getHeight() - metrics.getHeight()) + metrics.getAscent();
        g.drawString(text, x, y);
        UUID uuid = UUID.fastUUID();
        //  新图片地址
        String imagePath = String.format("image\\%s.jpg", uuid);
        ImageIO.write(image, "jpg", new File(imagePath));
        g.dispose();
        return imagePath;
//        ImageIO.write(image, "jpg", new File("D:\\ideaProjects\\megumiBot\\megumi\\src\\main\\resources\\emoticon\\test1.jpg"));
    }


}

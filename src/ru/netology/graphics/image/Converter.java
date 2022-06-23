package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private TextColorSchema schema = new Schema();
    private int height;
    private int width;
    private double maxRatio;

    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        double ratio = img.getWidth() / img.getHeight();
        if (maxRatio < ratio) {
            throw new BadImageSizeException(maxRatio, ratio);
        }

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        if (newWidth > width) {
            double div = newWidth / width;
            newWidth = (int) Math.floor(newWidth / div);
            newHeight = (int) Math.floor(newHeight / div);
        }
        if (newHeight > height) {
            double div = newHeight / height;
            newHeight = (int) Math.floor(newHeight / div);
            newWidth = (int) Math.floor(newWidth / div);
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder textImg = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                textImg.append(c);
                textImg.append(c);
            }
            textImg.append("\n");
        }
        return textImg.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
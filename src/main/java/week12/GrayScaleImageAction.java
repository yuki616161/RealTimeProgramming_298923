package week12;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class GrayScaleImageAction extends RecursiveAction {
    private final int row;
    private final BufferedImage image;

    public GrayScaleImageAction(int row, BufferedImage image) {
        this.row = row;
        this.image = image;
    }

    @Override
    protected void compute() {
        for (int col = 0; col < image.getWidth(); col++) {
            int rgb = image.getRGB(col, row);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            int gray = (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);
            int grayRGB = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;

            image.setRGB(col, row, grayRGB);
        }
    }
}

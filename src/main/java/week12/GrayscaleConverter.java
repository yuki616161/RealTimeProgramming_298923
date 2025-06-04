package week12;


import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class GrayscaleConverter {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            System.err.println("Usage: java GrayscaleConverter <image-file>");
            return;
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            System.err.println("Error: File not found - " + args[0]);
            return;
        }

        BufferedImage img = ImageIO.read(inputFile);
        ForkJoinPool pool = new ForkJoinPool();

        for (int row = 0; row < img.getHeight(); row++) {
            pool.execute(new GrayScaleImageAction(row, img));
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            Thread.sleep(10);
        }

        File output = new File("C:\\Users\\User\\Documents\\UUM\\Sem4\\RealTimeProgramming\\RealTimeProgramming\\photo.png");
        ImageIO.write(img, "png", output);
        System.out.println("Grayscale image written to: " + output.getAbsolutePath());

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(output);
        }
    }
}

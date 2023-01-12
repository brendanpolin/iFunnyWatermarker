import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        final File dir = new File("C:\\");
        System.out.println(Arrays.toString(dir.listFiles()));

        File sourceImageFile;
        URL watermarkImageURL = new URL("https://www.memesmonkey.com/images/memesmonkey/a2/a2ae215f26491f7aa74c203b0c107fa9.jpeg");
        File destImageFile;

        for (String fileName : getAllImages(dir, true))
        {
            sourceImageFile = new File(fileName);
            destImageFile = new File(fileName);
            addImageWatermark(watermarkImageURL, sourceImageFile, destImageFile);
            System.out.println(fileName);
        }
    }
    public static ArrayList<String> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<String> resultList = new ArrayList<String>(256);
        File[] f = directory.listFiles();
        if(f == null){
            System.out.println(directory.getName() + " is empty");
            return null;
        }
        for (File file : f) {
            if (file != null && file.getName().toLowerCase().endsWith(".jpg") && !file.getName().startsWith("tn_")) {
                resultList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<String> tmp = getAllImages(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return null;
    }
    static void addImageWatermark(URL watermarkImageURL, File sourceImageFile, File destImageFile) {
        try {
            BufferedImage sourceImage = ImageIO.read(sourceImageFile);
            BufferedImage watermarkImage = ImageIO.read(watermarkImageURL);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            //Extends the original image and puts it into a new variable called cstImg
            CustomImg cstImg = new CustomImg(sourceImage.getWidth(), sourceImage.getHeight() + watermarkImage.getHeight(), sourceImage.getType());
            Graphics g = cstImg.createGraphics();
            g.setColor(new Color(0,0,0));
            g.fillRect(0,0,cstImg.getWidth(), cstImg.getHeight());
            g.drawImage(sourceImage,0,0,null);

            // calculates the coordinate where the image is painted
            int topLeftX = cstImg.getWidth() - watermarkImage.getWidth();
            int topLeftY = cstImg.getHeight() - watermarkImage.getHeight();

            // paints the image watermark
            g.drawImage(watermarkImage, topLeftX, topLeftY, null);

            ImageIO.write(cstImg, "png", destImageFile);
            g.dispose();

            System.out.println("The image watermark is added to the image.");

        } catch (IOException ex) {
            System.err.println(ex);
        }

    }
    public static class CustomImg extends BufferedImage {
        public CustomImg(int width, int height, int type){
            super(width, height, type);
        }
    }

}
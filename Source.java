import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scnr = new Scanner(System.in);
        /*
        FileOutputStream textStream = new FileOutputStream("report.txt");

        PrintWriter outText = new PrintWriter(textStream);
//outText is going to be used to write a text file, which you can open and read.
        outText.println("I'm here! I'm alive!!!!!");
        */
        int[][] image;
        int[][] result;
        int menu = -1;
        int h = 512;
        int w = 512;

        while (menu != 0) {
            System.out.println("1.Converting RGB to Grayscale. ");
            System.out.println("2.Thresholding");
            System.out.println("3.Random dithering");
            System.out.println("4.Pattern dithering");
            System.out.println("5.Error Diffusion dithering");
            System.out.println("0.Exit Program");
            menu = scnr.nextInt();

            switch (menu) {
                case 1:
                    image = readImage("AddieColor.raw", w * 3, h);
                    result = convertToGrayScale(image);
                    saveImage("AddieGrayScale.raw", result);
                    break;
                case 2:
                    image = readImage("Addie.raw", w, h);
                    result = thresholding(image);
                    saveImage("AddieThreshold.raw", result);
                    break;
                case 3:
                    image = readImage("Addie.raw", w, h);
                    result = randomDithering(image);
                    saveImage("AddieRandom.raw", result);
                    break;
                case 4:
                    image = readImage("Addie.raw", w, h);
                    result = patternDithering(image);
                    saveImage("AddiePattern.raw", result);
                    break;
                case 5:
                    image = readImage("Addie.raw", w, h);
                    result = errorDiffusion(image);
                    saveImage("AddieErrDiff.raw", result);
                    break;
            }
        }

        //outText.close();
    }

    public static int[][] readImage(String filename, int w, int h) throws IOException {
        int pixels[][] = new int[h][w];
//inputStream will write to a binary file, which you can't open and read as text
        FileInputStream inputStream = new FileInputStream(filename);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = inputStream.read();
            }
        }
        inputStream.close();
        return pixels;
    }

    public static void saveImage(String filename, int[][] pixels) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                outputStream.write(pixels[i][j]);
            }
        }
        outputStream.close();
    }

    public static int[][] thresholding(int[][] pixels) {
        int[][] newPixels = new int[pixels.length][pixels[0].length];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] < 128) {
                    newPixels[i][j] = 0;
                } else {
                    newPixels[i][j] = 255;
                }
            }
        }

        return newPixels;
    }

    public static int[][] randomDithering(int[][] pixels) {
        Random random = new Random();

        int[][] newPixels = new int[pixels.length][pixels[0].length];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int randomValue = random.nextInt(256);
                if (randomValue > pixels[i][j]) {
                    newPixels[i][j] = 0;
                } else {
                    newPixels[i][j] = 255;
                }
            }
        }

        return newPixels;
    }

    public static int[][] patternDithering(int[][] pixels) {
        int[][] mask = {
                {1, 7, 4},
                {5, 8, 3},
                {6, 2, 9}
        };
        int[][] newPixels = new int[pixels.length][pixels[0].length];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int value = (int) (pixels[i][j] / 26.6);
                int maskValue = mask[i % 3][j % 3];

                if (value < maskValue) {
                    newPixels[i][j] = 0;
                } else {
                    newPixels[i][j] = 255;
                }
            }
        }

        return newPixels;
    }

    public static int[][] convertToGrayScale(int[][] pixels) {
        int[][] newPixels = new int[pixels.length][pixels[0].length / 3];

        for (int i = 0; i < newPixels.length; i++) {
            for (int j = 0; j < newPixels[0].length; j++) {
                int r = pixels[i][j * 3];
                int g = pixels[i][j * 3 + 1];
                int b = pixels[i][j * 3 + 2];

                newPixels[i][j] = (int) (0.3 * r + 0.59 * g + 0.11 * b);
            }
        }

        return newPixels;
    }

    public static int[][] errorDiffusion(int[][] pixels) {
        int[][] newPixels = new int[pixels.length][pixels[0].length];

        for (int i = 0; i < pixels.length - 1; i++) {
            for (int j = 1; j < pixels[0].length - 1; j++) {
                int newValue = 0;

                if (pixels[i][j] / 255.0 >= 0.5)
                    newValue = 1;

                int error = pixels[i][j] - newValue;

                newPixels[i][j + 1] = (int) (pixels[i][j + 1] + (7.0 / 16.0) * error);
                newPixels[i + 1][j - 1] = (int) (pixels[i + 1][j - 1] + (3.0 / 16.0) * error);
                newPixels[i + 1][j] = (int) (pixels[i + 1][j] + (5.0 / 16.0) * error);
                newPixels[i + 1][j + 1] = (int) (pixels[i + 1][j + 1] + (1.0 / 16.0) * error);
            }
        }

        return newPixels;
    }
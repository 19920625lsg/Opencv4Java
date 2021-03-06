package com.huawei.l00379880.middle;

import edu.princeton.cs.algs4.Stopwatch;

import java.awt.image.BufferedImage;


/**************************************************************************
 * @Description : 利用数学原理给图片美白,改变beta的值即可,以长雀斑的女人照片为例
 *                图片地址:https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=1a2476fbeff81a4c2667e4cfe71a4c61/3812b31bb051f819900fc7c5deb44aed2e73e713.jpg
 * @author      : 梁山广
 * @date        : 2017/11/19 20:23
 * @email       : liangshanguang2@gmail.com
 *************************************************************************/
public class Lesson06MathWhiteImage {
    public static String imgPath = CommonPanel.ROOT_PATH + "example.jpg";

    public static void process(BufferedImage image) {

        Stopwatch timer = new Stopwatch();
        // 图像美白的参数,越接近1美白效果越好但是为1的话就会一片黑了
        double beta = 1.02;

        // 图像的的实际操作
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        int index = 0;
        // 获取全部的像素
        CommonMethods.getRGB(image, 0, 0, width, height, pixels);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                // 1、读像素
                // 得到当前点的下标.32位二进制数,每8bit依次代表A、R、G、B四个通道
                index = row * width + col;
                // RGB三通道混合在一起的
                int pixel = pixels[index];
                // 右移24位,相当于把32位中最高8位的值(Alpha通道)移到最低8bit,
                // &0xff是为了取出最低8bit,把结果转换为0~255之间的值,
                int channelA = (pixel >> 24) & 0xff;
                // R、G、B依次类推,分别为17~24,9~16,1~8位
                int channelR = (pixel >> 16) & 0xff;
                int channelG = (pixel >> 8) & 0xff;
                int channelB = pixel & 0xff;

                channelR = imgMath(channelR, beta);
                channelG = imgMath(channelG, beta);
                channelB = imgMath(channelB, beta);
                pixels[index] = (channelA << 24) | (channelR << 16) | (channelG << 8) | (channelB);
            }
        }
        CommonMethods.setRGB(image, 0, 0, width, height, pixels);
        System.out.println("每次都处理的美白耗费时间为:" + timer.elapsedTime() + "s");
    }

    public static int imgMath(int pixel, double beta) {
        // 把结果归一化到0~255之间
        double scale = 255 / Math.log(255 * (beta - 1) + 1) * Math.log(beta);
        double numerator = Math.log(pixel * (beta - 1) + 1);
        double denominator = Math.log(beta);

        return (int) (scale * numerator / denominator);
    }
}

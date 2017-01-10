package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public interface ImageUtils {
	public static Mat Convert_BufferedImage2Mat_BGR(BufferedImage image)
	{
		if(image == null) return null;
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		imageMat.put(0, 0, pixels);
		return imageMat;
	}
}

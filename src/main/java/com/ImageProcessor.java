package com;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Handles image resizing with aspect ratio adjustments and color conversions.
 */
public final class ImageProcessor {

    private ImageProcessor() {}

    /**
     * Resizes the source image so that the output matches targetWidth while correcting
     * for vertical font stretching in standard monospace terminal fonts.
     */
    public static BufferedImage prepareImage(BufferedImage source, int targetWidth, double fontAspectRatio) {
        int originalWidth = source.getWidth();
        int originalHeight = source.getHeight();

        // Calculate aspect ratio corrected target height:
        // Height = (Original Height / Original Width) * (Target Width / Font Aspect Ratio)
        double scale = (double) originalHeight / originalWidth;
        int targetHeight = Math.max(1, (int) Math.round((targetWidth * scale) / fontAspectRatio));

        // Use TYPE_INT_RGB to drop alpha channels and simplify bit manipulation
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();

        // Apply high-quality bicubic downsampling to preserve edge details
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resized;
    }

    /**
     * Calculates relative luminance using ITU-R BT.601 standard weights:
     * Luminance = (0.299 * R) + (0.587 * G) + (0.114 * B)
     */
    public static double calculateLuminance(int red, int green, int blue) {
        return (0.299 * red) + (0.587 * green) + (0.114 * blue);
    }
}
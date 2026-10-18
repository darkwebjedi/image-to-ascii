package com;

import java.awt.image.BufferedImage;

/**
 * Pipeline coordinating pixel sampling and text output generation.
 */
public class AsciiConverter {

    private static final String ANSI_RESET = "\u001B[0m";

    private final AsciiConfig config;

    public AsciiConverter(AsciiConfig config) {
        this.config = config;
    }

    /**
     * Converts a pre-scaled image into an ASCII or Unicode string representation.
     */
    public String convert(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder sb = new StringBuilder(height * (width + 1));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extract 8-bit channels using bit shifts
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                double luminance = ImageProcessor.calculateLuminance(r, g, b);
                char glyph = config.ramp().getCharacter(luminance, config.invert());

                if (config.colorize()) {
                    // ANSI 24-bit TrueColor foreground sequence: \u001B[38;2;R;G;Bm
                    sb.append(String.format("\u001B[38;2;%d;%d;%dm%c", r, g, b, glyph));
                } else {
                    sb.append(glyph);
                }
            }

            if (config.colorize()) {
                sb.append(ANSI_RESET);
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
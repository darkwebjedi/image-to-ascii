package com;

import java.nio.file.Path;

/**
 * Immutable configuration options parsed from CLI arguments.
 */
public record AsciiConfig(
    Path inputPath,
    Path outputPath,
    int targetWidth,
    CharacterRamp ramp,
    boolean invert,
    boolean colorize,
    double fontAspectRatio // Terminal character height-to-width ratio (typical default: 2.0)
) {
    public static final double DEFAULT_ASPECT_RATIO = 2.0;
    public static final int DEFAULT_WIDTH = 100;
}
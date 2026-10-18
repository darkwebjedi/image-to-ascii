package com;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            printHelp();
            return;
        }

        AsciiConfig config;
        try {
            config = parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Argument Error: " + e.getMessage());
            printHelp();
            System.exit(1);
            return;
        }

        try {
            BufferedImage sourceImage = ImageLoader.load(config.inputPath());
            BufferedImage preparedImage = ImageProcessor.prepareImage(
                sourceImage,
                config.targetWidth(),
                config.fontAspectRatio()
            );

            AsciiConverter converter = new AsciiConverter(config);
            String result = converter.convert(preparedImage);

            // Print directly to console
            System.out.print(result);

            // Write to disk if output path is provided
            if (config.outputPath() != null) {
                // If writing to a file, strip ANSI codes if necessary, or preserve as text
                String fileContent = config.colorize()
                    ? result.replaceAll("\u001B\\[[;\\d]*m", "") // Strip ANSI escape sequences for clean text files
                    : result;

                Files.writeString(config.outputPath(), fileContent);
                System.out.printf("%n[Art saved successfully to: %s]%n", config.outputPath());
            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            System.exit(2);
        }
    }

    private static AsciiConfig parseArgs(String[] args) {
        Path inputPath = null;
        Path outputPath = null;
        int width = AsciiConfig.DEFAULT_WIDTH;
        CharacterRamp ramp = CharacterRamp.ASCII;
        boolean invert = false;
        boolean colorize = false;
        double aspectRatio = AsciiConfig.DEFAULT_ASPECT_RATIO;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i", "--input" -> {
                    if (++i < args.length) inputPath = Paths.get(args[i]);
                }
                case "-o", "--output" -> {
                    if (++i < args.length) outputPath = Paths.get(args[i]);
                }
                case "-w", "--width" -> {
                    if (++i < args.length) width = Integer.parseInt(args[i]);
                }
                case "-r", "--ramp" -> {
                    if (++i < args.length) ramp = CharacterRamp.valueOf(args[i].toUpperCase());
                }
                case "--invert" -> invert = true;
                case "-c", "--color" -> colorize = true;
                case "--ratio" -> {
                    if (++i < args.length) aspectRatio = Double.parseDouble(args[i]);
                }
                default -> throw new IllegalArgumentException("Unknown flag: " + args[i]);
            }
        }

        if (inputPath == null) {
            throw new IllegalArgumentException("Missing required parameter: -i / --input <path>");
        }

        return new AsciiConfig(inputPath, outputPath, width, ramp, invert, colorize, aspectRatio);
    }

    private static void printHelp() {
        System.out.println("""
            Image to ASCII / Unicode Converter
            -----------------------------------
            Usage:
              java com.asciiart.Main -i <path_to_image> [options]

            Options:
              -i, --input <file>     Path to the source image (PNG, JPEG, BMP) [Required]
              -o, --output <file>    Path to write the text art (.txt)
              -w, --width <int>      Output column width (default: 100)
              -r, --ramp <type>      Character set: ASCII or BLOCK (default: ASCII)
              --invert               Inverts glyph ramp for light-background terminals
              -c, --color            Enable ANSI 24-bit TrueColor output
              --ratio <double>       Font height:width ratio for aspect correction (default: 2.0)
            """);
    }
}
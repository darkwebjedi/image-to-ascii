# Image-to-ASCII Converter

A lightweight, modular Java CLI tool that converts raster images (JPEG, PNG, BMP, GIF) into ASCII and Unicode text art. Designed with aspect-ratio correction, ITU-R luminance weighting, multiple character sets, file export, and optional 24-bit ANSI TrueColor terminal rendering.

---

## Features

- **Accurate Luminance Conversion**: Evaluates perceived brightness using the standard ITU-R BT.601 formula:
  
  $$\text{Luminance} = (0.299 \times R) + (0.587 \times G) + (0.114 \times B)$$

- **Font Aspect Ratio Correction**: Dynamically compensates for rectangular terminal character cells (~2:1 vertical height-to-width ratio) to prevent distorted, stretched output.
- **Multiple Density Ramps**:
  - **Standard ASCII**: Classic ramp mapping from heavy to light characters (`@%#*+=-:. `).
  - **Unicode Block Elements**: Smooth gradient rendering via block symbols (`█▓▒░ `).
- **Luminance Inversion**: Invert brightness mapping to match light or dark terminal themes.
- **ANSI TrueColor Output**: Render ASCII art in full 24-bit RGB terminal color directly in supported terminal emulators.
- **Text File Export**: Export clean ASCII output directly to `.txt` files with terminal color escapes automatically stripped.

---

## Project Structure

```text
image-to-ascii/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        └── java/
            └── com/
                ├── AsciiConfig.java       # Configuration record & CLI flag parsing
                ├── AsciiConverter.java    # Core pixel-to-glyph conversion engine
                ├── CharacterRamp.java     # Ramp definitions & density quantization
                ├── ImageLoader.java       # Image IO & path validation
                ├── ImageProcessor.java    # Scaling, downsampling, & luminance math
                └── Main.java              # Application entry point
```

---

## Requirements

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.8+

---

## Installation & Build

### 1. Clone the Repository

```bash
git clone https://github.com/darkwebjedi/image-to-ascii.git
cd image-to-ascii
```

### 2. Package the Application

Build the executable standalone JAR using Maven:

```bash
mvn clean package
```

The compiled JAR binary will be generated at:
```text
target/image-to-ascii-1.0.0.jar
```

---

## Usage

Execute the JAR file using your Java 17+ runtime:

```bash
java -jar target/image-to-ascii-1.0.0.jar -i "path/to/image.jpg" [options]
```

### Command-Line Arguments

| Flag | Parameter | Description | Default |
| :--- | :--- | :--- | :--- |
| `-i`, `--input` | `<path>` | **Required.** Path to the source image file (`.png`, `.jpg`, `.bmp`). | — |
| `-o`, `--output` | `<path>` | Path to save the output text file. | Standard Out |
| `-w`, `--width` | `<int>` | Target output width in character columns. | `100` |
| `-r`, `--ramp` | `ASCII` \| `BLOCK` | Character density preset to apply. | `ASCII` |
| `--invert` | — | Inverts brightness values (recommended for light terminals). | `false` |
| `-c`, `--color` | — | Enables 24-bit ANSI TrueColor terminal formatting. | `false` |
| `--ratio` | `<double>` | Font height-to-width correction ratio. | `2.0` |

---

## Examples

### 1. Standard Terminal Output
Render an image to the console with the default 100-character width:

```bash
java -jar target/image-to-ascii-1.0.0.jar -i "sample.jpg"
```

### 2. TrueColor Output with Unicode Blocks
Render using smooth Unicode block characters with 24-bit RGB color:

```bash
java -jar target/image-to-ascii-1.0.0.jar -i "sample.png" -r BLOCK -c
```

### 3. Light Terminal Background
Invert luminance values to prevent dark/light inversion on light-themed terminals:

```bash
java -jar target/image-to-ascii-1.0.0.jar -i "sample.jpg" --invert
```

### 4. Custom Width & File Export
Render an image at 140 columns wide and export the text art directly to a file:

```bash
java -jar target/image-to-ascii-1.0.0.jar -i "sample.png" -w 140 -o "art.txt"
```

---

## License

This project is open-source and available under the [MIT License](LICENSE).
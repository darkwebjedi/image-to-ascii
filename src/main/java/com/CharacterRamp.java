package com;

/**
 * Defines character density ramps and provides mapping from 
 * luminance values (0-255) to character glyphs.
 */
public enum CharacterRamp {
    // Ordered from densest (darkest glyph) to lightest (empty space)
    ASCII("@%#*+=-:. "),
    BLOCK("█▓▒░ ");

    private final String characters;

    CharacterRamp(String characters) {
        this.characters = characters;
    }

    /**
     * Maps an 8-bit luminance value (0-255) to an index within the selected ramp.
     *
     * @param luminance 0.0 (pitch black) to 255.0 (pure white)
     * @param invert    If true, reverses ramp ordering for light terminal themes
     * @return The representative character glyph
     */
    public char getCharacter(double luminance, boolean invert) {
        // Normalize luminance into a [0.0, 1.0] range
        double normalized = Math.max(0.0, Math.min(255.0, luminance)) / 255.0;

        // Invert luminance mapping if requested
        if (invert) {
            normalized = 1.0 - normalized;
        }

        // Quantize normalized float into discrete character bucket indices:
        // index = floor(normalized * (length - 1))
        int index = (int) Math.round(normalized * (characters.length() - 1));
        return characters.charAt(index);
    }
}
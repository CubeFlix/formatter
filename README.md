# TODO

- [x] join adjacent runs and words on the same line, if they have the same styling
- [x] hyphenation of words
- [x] when joining adjacent runs, handle objects in between runs. also handle zero-width objects.
- [x] when adding dash to the end of hyphenated text, ignore invisible objects
- [x] word splitter has issue with space at end of run
- [x] optional hyphenation
- [x] if nothing fits on the line, try to hyphenate, and if that doesn't work, just overflow
- [x] should dashes be placed within the text?
- [x] test hyphenation and optional hyphenation
- [x] spacers alongside words and inline text interface
- [x] word break
- [x] newline
- [x] the last line in a justified paragraph should be left-aligned
- [x] section (multi-paragraph) formatter that handles multiple pages
- [x] don't place hyphen if last character is space
- [x] when hyphenation cannot fit a single character, its creating an empty run and adding a hyphen. If hyphenation doesn’t fit anything, it should quit
- [x] page break
- [x] de-hyphenate line before adding to unfit words
- [x] weird infinite loop
- [ ] text styles (color, kerning, underlines, etc.)
- [ ] in ParagraphStyle, add setting for very last line
- [ ] draw border and background with padding on paragraph stream regions
- [ ] handle columns
- [ ] XML format reading

# FUTURE TODO

- [ ] try only calling newline once in renderParagraph
- [ ] orphan handling
- [ ] automatic syllable hyphenation
- [ ] path-bounded paragraph layout
- [ ] image and object word wrap (floating objects)
- [ ] tables
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
- [ ] section (multi-paragraph) formatter that handles multiple pages
- [ ] page break
- [ ] text styles (color, kerning, underlines, etc.)
- [ ] handle columns
- [ ] XML format reading

# FUTURE TODO

- [ ] try only calling newline once in renderParagraph
- [ ] orphan handling
- [ ] automatic syllable hyphenation
- [ ] path-bounded paragraph layout
- [ ] image and object word wrap (floating objects)
- [ ] tables
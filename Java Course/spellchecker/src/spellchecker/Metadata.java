package spellchecker;

public record Metadata(int characters, int words, int mistakes) {
    private static final String CHARACTERS = " characters, ";
    private static final String WORDS = " words, ";
    private static final String MISTAKES = " spelling issue(s) found";

    @Override
    public String toString(){
        return characters + CHARACTERS + words + WORDS + mistakes + MISTAKES;
    }
}

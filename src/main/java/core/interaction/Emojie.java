package core.interaction;

public enum Emojie {

    WHITE_CHECK_MARK("✅"),
    X("❌");

    private final String unicode;

    Emojie(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return this.unicode;
    }

}

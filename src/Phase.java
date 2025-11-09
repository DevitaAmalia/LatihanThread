public enum Phase {
    WORK,
    SHORT_BREAK,
    LONG_BREAK;

    public String title() {
        switch (this) {
        case WORK: return "WORK";
        case SHORT_BREAK: return "SHORT BREAK";
        case LONG_BREAK: return "LONG BREAK";
        default: return "";
        }
    }
}
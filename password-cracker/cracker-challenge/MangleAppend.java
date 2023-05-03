public class MangleAppend implements IMangle {
    private String c;

    public MangleAppend(String c) {
        this.c = c;
    }
    public String mangle(String s) {
        return s + c;
    }    
}

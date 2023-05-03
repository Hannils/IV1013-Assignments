public class MangleAppend implements IMangle {
    private String c;

    public MangleAppend(String c) {
        this.c = c;
    }
    @Override
    public String mangle(String s) {
        return s + c;
    }    
}

public class ManglePrepend implements IMangle {
    private String c;

    public ManglePrepend(String c) {
        this.c = c;
    }
    public String mangle(String s) {
        return c + s;
    }    
}

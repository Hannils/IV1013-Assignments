public class MangleCombiner implements IMangle{
    private IMangle[] mangleArray;

    public MangleCombiner(IMangle[] mangleArray) {
        this.mangleArray = mangleArray;
    }

    public String mangle(String s) {
        for(int i = 0; i < mangleArray.length; i++) {
            if (s.length() == 1) return s;
            s = mangleArray[i].mangle(s);
        };
        return s;
    }
}

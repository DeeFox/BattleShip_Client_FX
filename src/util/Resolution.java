package util;

public enum Resolution {
    QHD        (640, 360),
    QHD2       (960, 540),
    WXGA      (1024, 576),
    HD        (1280, 720),
    WXGA2     (1366, 768),
    WSXGA     (1600, 900),
    FHD      (1920, 1080),
    QWXGA    (2048, 1152),
    WQHD     (2560, 1440),
    QHDPLUS  (3200, 1800),
    UHD      (3840, 2160),
    UHDPLUS  (5120, 2880),
    FUHD     (7680, 4320),
    QUHD    (15360, 8640);
    
    private Resolution(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public final int x;
    public final int y;
    
    public Pair<Integer, Integer> getDimension() {
        return new Pair<Integer, Integer>(x, y);
    }
    
}

package io.github.knightmareleon.shared.constants;

public enum TimeSetting {
    THIRTY_SECONDS("00:30",30),
    ONE_MINUTE("01:00",60),
    THREE_MINUTES("03:00",180),
    FIVE_MINUTES("05:00",300),
    TEN_MINUTES("10:00",600);

    private final String text;
    private final int seconds;
    TimeSetting(String text, int seconds){
        this.text = text;
        this.seconds = seconds;
    }

    public String text(){
        return this.text;
    }

    public int seconds(){
        return this.seconds;
    }

}

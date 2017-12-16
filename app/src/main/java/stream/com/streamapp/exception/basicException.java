package stream.com.streamapp.exception;

/**
 * Created by Alan on 2017/12/16.
 */

public class basicException extends Throwable {
    private String except="";

    public String getExcept() {
        return except;
    }

    public void setExcept(String except) {
        this.except = except;
    }
}

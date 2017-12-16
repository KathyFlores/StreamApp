package stream.com.streamapp.exception;

/**
 * Created by Alan on 2017/12/16.
 */

public class internetError extends basicException {
    private internetError(){}
    public internetError(String error)
    {
        setExcept(error);
    }
    public void getException()
    {
        getExcept();
    }
}

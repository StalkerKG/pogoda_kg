package kg.ram.weatherkg;

/**
 * Created by RAM on 10.08.2017.
 */

public interface LoaderCallback<T> {
    void onStartLoad();
    void onSuccess(T t);
    void onFailure(Exception e);
}

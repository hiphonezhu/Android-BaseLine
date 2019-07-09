package library.common.framework.logic;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import library.common.framework.logic.net.RetrofitManager;
import library.common.framework.task.Task;
import library.common.framework.task.TaskExecutor;
import library.common.util.LogUtils;
import retrofit2.Retrofit;

/**
 * '数据'模块统一出口, Retrofit基本封装
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class BaseLogic extends EventLogic {
    protected Retrofit retrofit;
    private IRetryHandler retryHandler;

    public BaseLogic() {
        this(null);
    }

    /**
     * 构造函数
     *
     * @param subscriber 最终订阅者
     */
    public BaseLogic(Object subscriber) {
        super(subscriber);
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl());
    }

    /**
     * 重新读取配置
     */
    public void rebuildConfig() {
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl());
    }

    /**
     * 重试
     *
     * @param retryHandler
     */
    public void retryWhen(IRetryHandler retryHandler) {
        this.retryHandler = retryHandler;
    }

    /**
     * create api service
     *
     * @param service
     * @param <T>
     * @return
     */
    protected <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 发送请求(一般情况是可以拿到结果的最终请求,如需要'map、flatMap、doOnNext'等在BaseLogic的子类做好处理)
     *
     * @param observable
     * @param what       请求标示
     */
    protected Disposable sendRequest(final Observable observable, final int what) {
        return sendRequest(observable, null, what);
    }

    /**
     * 发送请求(一般情况是可以拿到结果的最终请求,如需要'map、flatMap、doOnNext'等在BaseLogic的子类做好处理)
     *
     * @param observable
     * @param what          请求标示
     * @param errorConsumer 异常自定义处理
     * @return
     */
    protected <T> Disposable sendRequest(final Observable observable, final ErrorConsumer<T> errorConsumer, final int what) {
        Observable observableSource = observable;
        if (retryHandler != null) {
            observableSource = observable.flatMap(new Function() {
                @Override
                public Object apply(Object o) throws Exception {
                    if (retryHandler.needRetry(o)) {
                        return retryHandler.beforeSource().flatMap(new Function() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                if (retryHandler.isBeforeSourceSuccess(o)) {
                                    return observable;
                                } else {
                                    return Observable.just(o);
                                }
                            }
                        });
                    }
                    return Observable.just(o);
                }
            });
        }
        return observableSource.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        onResult(what, o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable, null, null);
                        if (errorConsumer != null) {
                            onResult(what, errorConsumer.onError(throwable));
                        } else {
                            onResult(what, throwable);
                        }
                    }
                });
    }

    /**
     * 执行本地任务
     *
     * @param task
     */
    protected void executeTask(Task task) {
        TaskExecutor.getInstance().execute(task);
    }

    /**
     * API根地址
     *
     * @return
     */
    protected abstract String getBaseUrl();
}

package com.zhiyangstudio.commonlib.utils;

import com.orhanobut.logger.Logger;
import com.zhiyangstudio.commonlib.exception.ApiException;
import com.zhiyangstudio.commonlib.wrapper.ApiResponse;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by example on 2018/2/7.
 */

public class RxUtils {
    /**
     * 统一线程处理 仅支持Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxFlowableSchedulerHelper() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 指定上游为io线程
     * 下游为主线程
     */
    public static ObservableTransformer io_main() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一线程处理
     * 指定上游为io线程
     * 下游为主线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxObservableSchedulerHelper() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理 仅支持Flowable<ApiResponse<T>>
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<ApiResponse<T>, T> handleFlowableResult() {
        return new FlowableTransformer<ApiResponse<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<ApiResponse<T>> result) {
                Logger.e("handleFlowableResult -> FlowableTransformer apply");
                return result.switchMap(new Function<ApiResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(ApiResponse<T> tApiResponse) throws Exception {
                        Logger.e("switchMap apply");
                        if (tApiResponse.code == 200) {
                            Logger.e("sucess");
                            return createFlowableData(tApiResponse.body);
                        } else {
                            Logger.e("error");
                            return Flowable.error(new ApiException(tApiResponse.errorMessage, tApiResponse.code));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createFlowableData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                Logger.e("subscribe");
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        }, BackpressureStrategy.LATEST);
    }

    /**
     * 统一返回结果处理 仅支持Observable<ApiResponse<T>>
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<ApiResponse<T>, T> handleObservableResult() {
        return new ObservableTransformer<ApiResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ApiResponse<T>> result) {
                Logger.e("handleFlowableResult -> FlowableTransformer apply");
                return result.switchMap(new Function<ApiResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(ApiResponse<T> tApiResponse) throws Exception {
                        Logger.e("switchMap apply");
                        if (tApiResponse.code == 200) {
                            Logger.e("sucess");
                            return createObservableData(tApiResponse.body);
                        } else {
                            Logger.e("error");
                            return Observable.error(new ApiException(tApiResponse.errorMessage,
                                    tApiResponse.code));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Observable
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createObservableData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Logger.e("subscribe");
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        });
    }

    /**
     * 统一返回结果处理 仅支持Observable<T>
     * 用不上，暂时废弃
     */
    @Deprecated
    public static <T> ObservableTransformer<T, T> handleCommonObservableResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> result) {
                Logger.e("handleFlowableResult -> FlowableTransformer apply");
                return result.switchMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(T t) throws Exception {
                        return createObservableData(t);
                    }
                });
            }
        };
    }

    /**
     * 统一返回结果处理 仅支持Observable<T>
     * 用不上，暂时废弃
     */
    @Deprecated
    public static <T> FlowableTransformer<T, T> handleCommonFlowableResult() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> result) {
                Logger.e("handleFlowableResult -> FlowableTransformer apply");
                return result.switchMap(new Function<T, Flowable<? extends T>>() {
                    @Override
                    public Flowable<? extends T> apply(T t) throws Exception {
                        return createFlowableData(t);
                    }
                });
            }
        };
    }

    public static <T> Observable<T> getObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> safeObservable(Observable<T> observable) {
        return observable.doOnError(Throwable::printStackTrace);
    }

    public static <T> Single<T> getSingle(Single<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

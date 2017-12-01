package com.dogvip.giannis.dogviprefactored.utilities.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import javax.inject.Inject;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by giannis on 4/11/2017.
 */

public class UIUtls {

    private Context mContext;

    @Inject
    public UIUtls(Context context) {
        this.mContext = context;
    }

    public Maybe<Boolean> showSnackBar(final View view, final String msg, final String action, final int length) {
        return Maybe.using(
                () -> getStyledSnackBar(view, msg, length),
                new Function<Snackbar, MaybeSource<? extends Boolean>>() {
                    @Override
                    public MaybeSource<? extends Boolean> apply(@NonNull final Snackbar snackbar) throws Exception {
                        return Maybe.create(new MaybeOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@NonNull final MaybeEmitter<Boolean> e) throws Exception {
                                snackbar.setAction(action, v -> {
                                    if (!e.isDisposed()) e.onSuccess(true);
                                });
                                snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        if (!e.isDisposed()) e.onComplete();
                                    }
                                });
                                snackbar.show();
                            }
                        });
                    }
                },
                snackbar -> snackbar.addCallback(null)
        );
    }

    private Snackbar getStyledSnackBar(View view, String msg, int length) {
        Snackbar snackbar = Snackbar.make(view, msg, length);
        snackbar.setActionTextColor(ContextCompat.getColor(mContext, android.R.color.black));
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        return snackbar;
    }

    public void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

package com.meuorcamento.app.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.meuorcamento.R;

public abstract class DialogFragmentAbstract extends DialogFragment implements IDialogFragment {

    public IDialogFragmentCallback mDialogFragment = null;
    private static final String TAG = "DialogFragment";

    private View mView;
    public void attachActionBar(View view) {
        mView = view;
        View btnFiltrar = view.findViewById(R.id.btnConfirmar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositiveClick();
                dismiss();

            }
        });
        View btnCancelar = view.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView();
                onNegativeClick();
                dismiss();
            }
        });
    }

    public void initilizeDialogFragment(IDialogFragmentCallback dialogFragment) {
        mDialogFragment = dialogFragment;
    }

    @Override
    public void onNegativeClick() {
        mDialogFragment.onNegativeClick();
    }

    @Override
    public void onPositiveClick() {
        mDialogFragment.onPositiveClick();
    }

    @Override
    public void showDialog() {

        if(mDialogFragment == null)
            throw new IllegalStateException("DialogFragment is not initialized, execute initilizeDialogFragment(IDialogFragment dialogFragment, View dialogView) first.");

        FragmentManager fragmentManager = mDialogFragment.getFragmentManager();

        if(mView != null && mView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup)mView.getParent();
            viewGroup.removeView(mView);
        }

        fragmentManager.executePendingTransactions();

        Fragment f = fragmentManager.findFragmentByTag(getViewTag());

        if(f != null)
            return;

        show(fragmentManager, getViewTag());

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //getDialog().setCanceledOnTouchOutside(false);

        return dialog;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

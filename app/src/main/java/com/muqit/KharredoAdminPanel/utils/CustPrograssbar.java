package com.muqit.KharredoAdminPanel.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CustPrograssbar {
    ProgressDialog progressDialog;

    public void PrograssCreate(Context context, String str) {
        try {
            if (this.progressDialog == null || !this.progressDialog.isShowing()) {
                ProgressDialog progressDialog2 = new ProgressDialog(context);
                this.progressDialog = progressDialog2;
                progressDialog2.setMessage(str);
                this.progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClosePrograssBar() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null) {
            try {
                progressDialog2.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

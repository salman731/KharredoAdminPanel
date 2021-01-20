package com.muqit.KharredoAdminPanel.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.muqit.KharredoAdminPanel.Models.AffiliateMarketersData;
import com.muqit.KharredoAdminPanel.Models.BannersResponse;
import com.muqit.KharredoAdminPanel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class AffiliateMarketersRecyclerViewAdapter extends Adapter<AffiliateMarketersRecyclerViewAdapter.AffiliateMarketersItemsViewHolder> implements Filterable {
    public String IMAGE_BASE_URL = "https://kharredo.com/affiliateMarketer/";
    private ArrayList<AffiliateMarketersData> affiliateMarketersDataArrayList;
    private List<AffiliateMarketersData> affiliateMarketersDataArrayFullList;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private Filter AM_Filter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                arrayList.addAll(AffiliateMarketersRecyclerViewAdapter.this.affiliateMarketersDataArrayFullList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (AffiliateMarketersData affiliateMarketersData : AffiliateMarketersRecyclerViewAdapter.this.affiliateMarketersDataArrayFullList) {
                    if (SalesRecyclerViewAdapter.blankIfNull(affiliateMarketersData.getFullName()).toLowerCase().contains(trim) ) {
                        arrayList.add(affiliateMarketersData);
                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AffiliateMarketersRecyclerViewAdapter.this.affiliateMarketersDataArrayList.clear();
            AffiliateMarketersRecyclerViewAdapter.this.affiliateMarketersDataArrayList.addAll((List) filterResults.values);
            AffiliateMarketersRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    };

    public AffiliateMarketersRecyclerViewAdapter(ArrayList<AffiliateMarketersData> arrayList, Context context2) {
        this.affiliateMarketersDataArrayList = arrayList;
        this.context = context2;
        this.affiliateMarketersDataArrayFullList = new ArrayList<>(arrayList);
    }

    public AffiliateMarketersItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AffiliateMarketersItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.affiliate_marketing_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(AffiliateMarketersItemsViewHolder affiliateMarketersItemsViewHolder, int i) {
        final AffiliateMarketersData affiliateMarketersData = (AffiliateMarketersData) this.affiliateMarketersDataArrayList.get(i);
        affiliateMarketersItemsViewHolder.Name.setText(affiliateMarketersData.getFullName());
        Picasso picasso = Picasso.get();
        StringBuilder sb = new StringBuilder();
        sb.append(this.IMAGE_BASE_URL);
        sb.append(affiliateMarketersData.getPhotoURL());
        picasso.load(sb.toString()).into(affiliateMarketersItemsViewHolder.PhotoIV);
        affiliateMarketersItemsViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = LayoutInflater.from(AffiliateMarketersRecyclerViewAdapter.this.context).inflate(R.layout.affiliate_marketing_detail_layout, null);
                MaterialTextView materialTextView = (MaterialTextView) inflate.findViewById(R.id.af_phone_no);
                MaterialTextView materialTextView2 = (MaterialTextView) inflate.findViewById(R.id.af_city);
                MaterialTextView materialTextView3 = (MaterialTextView) inflate.findViewById(R.id.af_state);
                MaterialTextView materialTextView4 = (MaterialTextView) inflate.findViewById(R.id.af_address);
                MaterialTextView materialTextView5 = (MaterialTextView) inflate.findViewById(R.id.af_email);
                MaterialTextView materialTextView6 = (MaterialTextView) inflate.findViewById(R.id.af_availible_date);
                MaterialTextView materialTextView7 = (MaterialTextView) inflate.findViewById(R.id.af_cnic_no);
                MaterialTextView materialTextView8 = (MaterialTextView) inflate.findViewById(R.id.af_pakistan_citizen);
                MaterialTextView materialTextView9 = (MaterialTextView) inflate.findViewById(R.id.af_affiliate_marketing_knowledge);
                MaterialTextView materialTextView10 = (MaterialTextView) inflate.findViewById(R.id.af_affilate_training);
                MaterialTextView materialTextView11 = (MaterialTextView) inflate.findViewById(R.id.af_criminal_conviction);
                MaterialTextView materialTextView12 = (MaterialTextView) inflate.findViewById(R.id.af_criminal_conviction_explain);
                MaterialTextView materialTextView13 = (MaterialTextView) inflate.findViewById(R.id.af_social_media_experience);
                MaterialTextView materialTextView14 = (MaterialTextView) inflate.findViewById(R.id.af_social_media_experience_explain);
                MaterialTextView materialTextView15 = (MaterialTextView) inflate.findViewById(R.id.af_bank_name);
                MaterialTextView materialTextView16 = (MaterialTextView) inflate.findViewById(R.id.af_bank_branch);
                MaterialTextView materialTextView17 = (MaterialTextView) inflate.findViewById(R.id.af_account_title);
                MaterialTextView materialTextView18 = (MaterialTextView) inflate.findViewById(R.id.af_account_no);
                MaterialTextView materialTextView19 = (MaterialTextView) inflate.findViewById(R.id.af_payment_medium);
                MaterialTextView materialTextView20 = (MaterialTextView) inflate.findViewById(R.id.af_date_birth);
                ImageView imageView = (ImageView) inflate.findViewById(R.id.af_image);
                ImageView imageView2 = (ImageView) inflate.findViewById(R.id.af_cnic_image);
                View view2 = inflate;
                ((MaterialTextView) inflate.findViewById(R.id.af_name)).setText(affiliateMarketersData.getFullName());
                materialTextView.setText(affiliateMarketersData.getPhoneNo());
                materialTextView2.setText(affiliateMarketersData.getCity());
                materialTextView3.setText(affiliateMarketersData.getState());
                materialTextView4.setText(affiliateMarketersData.getAddress());
                materialTextView5.setText(affiliateMarketersData.getEmail());
                materialTextView6.setText(affiliateMarketersData.getAvailibleDate());
                materialTextView7.setText(affiliateMarketersData.getCNICNumber());
                materialTextView8.setText(affiliateMarketersData.getCitizenPakistan());
                materialTextView9.setText(affiliateMarketersData.getAffiliateMarketingKnowledge());
                materialTextView10.setText(affiliateMarketersData.getAffliateTraining());
                materialTextView11.setText(affiliateMarketersData.getCriminalConviction());
                materialTextView12.setText(affiliateMarketersData.getIfCriminalConviction());
                materialTextView13.setText(affiliateMarketersData.getWorkonSocialMedia());
                materialTextView14.setText(affiliateMarketersData.getMentionifyouWorkonSocialMedia());
                materialTextView15.setText(affiliateMarketersData.getBankName());
                materialTextView16.setText(affiliateMarketersData.getBankBranch());
                materialTextView17.setText(affiliateMarketersData.getAccountTitle());
                materialTextView18.setText(affiliateMarketersData.getAccountNumber());
                materialTextView19.setText(affiliateMarketersData.getPaymentOnOtherMedium());
                materialTextView20.setText(affiliateMarketersData.getDateOfBirth());
                Picasso picasso = Picasso.get();
                StringBuilder sb = new StringBuilder();
                sb.append(AffiliateMarketersRecyclerViewAdapter.this.IMAGE_BASE_URL);
                sb.append(affiliateMarketersData.getPhotoURL());
                RequestCreator load = picasso.load(sb.toString());
                ImageView imageView3 = imageView;
                load.into(imageView3);
                Picasso picasso2 = Picasso.get();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(AffiliateMarketersRecyclerViewAdapter.this.IMAGE_BASE_URL);
                sb2.append(affiliateMarketersData.getCNICImage());
                picasso2.load(sb2.toString()).into(imageView2);
                if (imageView3.getDrawable() == null) {
                    imageView3.setImageResource(R.drawable.noimage);
                }
                if (imageView2.getDrawable() == null) {
                    imageView2.setImageResource(R.drawable.noimage);
                }
                AffiliateMarketersRecyclerViewAdapter.this.materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AffiliateMarketersRecyclerViewAdapter.this.context).setView(view2).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AffiliateMarketersRecyclerViewAdapter.this.materialAlertDialogBuilder.show();
            }
        });
    }

    public int getItemCount() {
        return this.affiliateMarketersDataArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return AM_Filter;
    }

    public class AffiliateMarketersItemsViewHolder extends ViewHolder {
        public MaterialTextView Name;
        public ImageView PhotoIV;

        public AffiliateMarketersItemsViewHolder(View view) {
            super(view);
            this.PhotoIV = (ImageView) view.findViewById(R.id.affiliate_marketing_image);
            this.Name = (MaterialTextView) view.findViewById(R.id.affiliate_marketing_name);
        }
    }
}

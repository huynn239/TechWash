//package com.example.techwash.Adapter;
//
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.techwash.Model.Auto;
//import com.example.techwash.R;
//import com.example.techwash.UI.DetailHotelActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> implements Filterable {
//    List<Auto> list;
//    List<Auto> listfull;
//    private final Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            ArrayList<Auto> newslist = new ArrayList<>();
//            if (charSequence == null || charSequence.length() == 0) {
//                newslist.addAll(listfull);
//            } else {
//                String newslist2 = charSequence.toString().toLowerCase(Locale.getDefault()).trim();
//
//                for (Auto ks : listfull) {
//                    if (ks.getTenks().toLowerCase(Locale.getDefault()).contains(newslist2))
//                        newslist.add(ks);
//                    if (ks.getDiachi().toLowerCase().contains(newslist2))
//                        newslist.add(ks);
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = newslist;
//            results.count = newslist.size();
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            list.clear();
//            list.addAll((ArrayList) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };
//    FirebaseAuth auth;
//    Boolean isMyFavorites = false;
//    FirebaseUser firebaseUser;
//
//
//    public RoomAdapter(List<Auto> list) {
//        this.listfull = list;
//        this.list = new ArrayList<>(listfull);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garage, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Auto ks = list.get(position);
//
//        holder.tenks.setText(ks.getTenks());
//        holder.diachi.setText(ks.getDiachi());
//
//        Locale localeVN = new Locale("vi", "VN");
//        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
//        String get_gia = currencyVN.format(Integer.parseInt(ks.getGia()));
//        holder.gia.setText(get_gia);
//
//        if (ks.getHinh().equals("")) {
//            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_image_24);
//            holder.img.setImageDrawable(drawable);
//        } else {
//            Picasso.get().load(ks.getHinh())
//                    .placeholder(R.drawable.ic_baseline_image_24)
//                    .fit()
//                    .noFade()
//                    .centerCrop()
//                    .into(holder.img);
//        }
//
//        auth = FirebaseAuth.getInstance();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                isMyFavorites = snapshot.child("Favorites").child(ks.getTenks()).exists();
//                if (isMyFavorites) {
//                    Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_favorite_24);
//                    holder.favorite.setImageDrawable(drawable);
//                } else {
//                    Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_favorite_border_24);
//                    holder.favorite.setImageDrawable(drawable);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentbk = new Intent(view.getContext(), DetailHotelActivity.class);
//                intentbk.putExtra("clickdetail", ks);
//                view.getContext().startActivity(intentbk);
//            }
//        });
//    }
//
//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView img, favorite;
//        TextView tenks, diachi, gia;
//        RelativeLayout ln_linear;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            favorite = itemView.findViewById(R.id.favorite);
//            img = itemView.findViewById(R.id.img1);
//            tenks = itemView.findViewById(R.id.tenkstext);
//            diachi = itemView.findViewById(R.id.diachitext);
//            gia = itemView.findViewById(R.id.giatext);
//            ln_linear = itemView.findViewById(R.id.ln_linear);
//        }
//    }
//}

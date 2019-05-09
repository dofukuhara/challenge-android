package br.com.douglas.fukuhara.lodjinha.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.douglas.fukuhara.lodjinha.R;
import br.com.douglas.fukuhara.lodjinha.network.vo.ProductDataVo;

public class HomeBestSellerAdapter extends RecyclerView.Adapter<HomeBestSellerAdapter.HomeBestSellerViewHolder> {

    private List<ProductDataVo> mBestSellerProductsList;
    private HomeBestSellerClickListener mListener;

    public HomeBestSellerAdapter(List<ProductDataVo> bestSellerProductsList, HomeBestSellerClickListener listener) {
        mBestSellerProductsList = bestSellerProductsList;
        mListener = listener;
    }

    @NonNull
    @Override
    public HomeBestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HomeBestSellerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.best_seller_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBestSellerViewHolder holder, int position) {
        if (getItemCount() > 0) {
            holder.onBind(mBestSellerProductsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mBestSellerProductsList == null ? 0 : mBestSellerProductsList.size();
    }

    public interface HomeBestSellerClickListener {
        void onBestSellerItemClick(ProductDataVo productDataVo);
    }

    class HomeBestSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewGroup mViewGroup;
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvPrevPrice;
        private TextView tvFinalPrice;

        public HomeBestSellerViewHolder(@NonNull View itemView) {
            super(itemView);

            mViewGroup = (ViewGroup) itemView;
            ivProductImage = itemView.findViewById(R.id.iv_best_seller_list_image);
            tvProductName = itemView.findViewById(R.id.tv_best_seller_list_prod_name);
            tvPrevPrice = itemView.findViewById(R.id.tv_best_seller_list_prev_price);
            tvFinalPrice = itemView.findViewById(R.id.tv_best_seller_list_final_price);

            itemView.setOnClickListener(this);
        }

        public void onBind(ProductDataVo productDataVo) {
            String urlImagem = productDataVo.getUrlImagem();
            String productName = productDataVo.getNome();
            String productPrecoDe = productDataVo.getPrecoDe().toString().replace(".",",");
            String productPrecoPor = productDataVo.getPrecoPor().toString().replace(".",",");
            // TODO: Configure: fallback, onError, placeholder
            Glide
                    .with(mViewGroup)
                    .load(urlImagem)
                    .into(ivProductImage);

            tvProductName.setText(productName);
            tvPrevPrice.setText(mViewGroup.getContext().getString(R.string.best_seller_prev_price, productPrecoDe));
            tvFinalPrice.setText(mViewGroup.getContext().getString(R.string.best_seller_final_price, productPrecoPor));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onBestSellerItemClick(mBestSellerProductsList.get(clickedPosition));
        }
    }
}

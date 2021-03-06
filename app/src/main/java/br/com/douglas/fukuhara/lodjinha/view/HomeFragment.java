package br.com.douglas.fukuhara.lodjinha.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import br.com.douglas.fukuhara.lodjinha.R;
import br.com.douglas.fukuhara.lodjinha.adapter.HomeBannerViewPagerAdapter;
import br.com.douglas.fukuhara.lodjinha.adapter.ProductsListAdapter;
import br.com.douglas.fukuhara.lodjinha.adapter.HomeCategoryAdapter;
import br.com.douglas.fukuhara.lodjinha.interfaces.HomeContract;
import br.com.douglas.fukuhara.lodjinha.network.RetrofitImpl;
import br.com.douglas.fukuhara.lodjinha.network.vo.BannerDataVo;
import br.com.douglas.fukuhara.lodjinha.network.vo.CategoryDataVo;
import br.com.douglas.fukuhara.lodjinha.network.vo.ProductDataVo;
import br.com.douglas.fukuhara.lodjinha.presenter.HomePresenter;

public class HomeFragment extends Fragment
    implements HomeContract.View {

    private HomeContract.Presenter mPresenter;
    private ViewPager mVpHomeBanner;
    private RecyclerView mRvCategoryList;
    private RecyclerView mRvBestSellerList;
    private ProgressBar mPbLoadingBanner;
    private ProgressBar mPbLoadingCategory;
    private ProgressBar mPbLoadingBestSellerProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewCreated = inflater.inflate(R.layout.home_fragment, container, false);

        setLayoutViews(viewCreated);

        mPresenter = new HomePresenter(this, RetrofitImpl.getInstance());
        mPresenter.fetchContent();

        return viewCreated;
    }

    private void setLayoutViews(View viewCreated) {
        mVpHomeBanner = viewCreated.findViewById(R.id.vp_home_banner);
        mRvCategoryList = viewCreated.findViewById(R.id.rv_home_category);
        mRvBestSellerList = viewCreated.findViewById(R.id.rv_home_best_sellers);
        mPbLoadingBanner = viewCreated.findViewById(R.id.pb_loading_banner);
        mPbLoadingCategory = viewCreated.findViewById(R.id.pb_loading_category);
        mPbLoadingBestSellerProducts = viewCreated.findViewById(R.id.pb_loading_best_sellers_products);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.disposeAll();
    }

    @Override
    public void onBannerContentLoaded(List<BannerDataVo> listOfData) {
        PagerAdapter pagerAdapter = new HomeBannerViewPagerAdapter(listOfData, this::onBannerItemClick);
        mVpHomeBanner.setAdapter(pagerAdapter);
    }

    private void onBannerItemClick(BannerDataVo bannerDataVo) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                bannerDataVo.getLinkUrl(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBannerContentFailed(String message) {
        showSnackBarWithErrorMessage(getString(R.string.snack_banner_server_error, message));
    }

    @Override
    public void onBannerContentFailedGenericError() {
        showSnackBarWithErrorMessage(getString(R.string.snack_banner_server_generic_error));
    }

    @Override
    public void onCategoryContentLoaded(List<CategoryDataVo> listOfData) {
        HomeCategoryAdapter categoryAdapter = new HomeCategoryAdapter(listOfData, this::onCategoryItemClick);
        mRvCategoryList.setAdapter(categoryAdapter);
    }

    private void onCategoryItemClick(CategoryDataVo categoryDataVo) {
        startActivity(ProductsListByCategoryActivity
                .newIntent(getContext(), categoryDataVo.getId(), categoryDataVo.getDescricao()));
    }

    @Override
    public void onCategoryContentFailed(String message) {
        showSnackBarWithErrorMessage(getString(R.string.snack_category_server_error, message));
    }

    @Override
    public void onCategoryContentFailedGenericError() {
        showSnackBarWithErrorMessage(getString(R.string.snack_category_server_generic_error));
    }

    @Override
    public void onBestSellerContentLoaded(List<ProductDataVo> listOfData) {
        ProductsListAdapter bestSellerAdapter = new ProductsListAdapter(listOfData, this::onBestSellerItemClick);
        mRvBestSellerList.addItemDecoration(
                new DividerItemDecoration(mRvBestSellerList.getContext(), DividerItemDecoration.VERTICAL));
        mRvBestSellerList.setAdapter(bestSellerAdapter);
    }

    private void onBestSellerItemClick(ProductDataVo productDataVo) {
        startActivity(ProductDetailActivity.newIntent(getContext(), productDataVo));
    }

    @Override
    public void onBestSellerContentFailed(String message) {
        showSnackBarWithErrorMessage(getString(R.string.snack_bestseller_server_error, message));
    }

    @Override
    public void onBestSellerContentFailedGenericError() {
        showSnackBarWithErrorMessage(getString(R.string.snack_bestseller_server_generic_error));
    }

    @Override
    public void showBannerLoading() {
        mPbLoadingBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBannerLoading() {
        mPbLoadingBanner.setVisibility(View.GONE);
    }

    @Override
    public void showCategoryLoading() {
        mPbLoadingCategory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCategoryLoading() {
        mPbLoadingCategory.setVisibility(View.GONE);
    }

    @Override
    public void showBestSellerProductsLoading() {
        mPbLoadingBestSellerProducts.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBestSellerProductsLoading() {
        mPbLoadingBestSellerProducts.setVisibility(View.GONE);
    }

    private void showSnackBarWithErrorMessage(String message) {
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor((getResources().getColor(R.color.tomato)));
        snackBar.show();
    }
}

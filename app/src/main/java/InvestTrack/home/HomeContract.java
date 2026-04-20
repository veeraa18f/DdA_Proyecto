package InvestTrack.home;

import java.lang.ref.WeakReference;
import java.util.List;

import InvestTrack.data.Asset;

public interface HomeContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(HomeViewModel viewModel);

        void navigateToDetailScreen();

        void showError(String message);
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onCreateCalled();

        void onSearchQueryChanged(String query);

        void onFilterSelected(String filter);

        void onAssetClicked(String assetId);

        void onDestroyCalled();
    }

    interface Model {
        List<Asset> getAssets();
    }
}

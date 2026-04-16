package InvestTrack.manage;

import java.lang.ref.WeakReference;

<<<<<<< ours
public interface ManageInvestmentContract {

  interface View {
    void injectPresenter(Presenter presenter);

    void onRefreshViewWithUpdatedData(ManageInvestmentViewModel viewModel);

    void navigateToNextScreen();
    void navigateToPreviousScreen();
  }

  interface Presenter {
    void injectView(WeakReference<View> view);
    void injectModel(Model model);

    void onResumeCalled();
    void onCreateCalled();
    void onRecreateCalled();
    void onBackButtonPressed();
    void onPauseCalled();
    void onDestroyCalled();
  }

  interface Model {
    String getStoredData();
    String getSavedData();

    String getCurrentData();
    void setCurrentData(String data);

    void onUpdatedDataFromRecreatedScreen(String data);
    void onUpdatedDataFromNextScreen(String data);
    void onUpdatedDataFromPreviousScreen(String data);
  }

}
=======
public interface
ManageInvestmentContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void onRefreshViewWithUpdatedData(ManageInvestmentViewModel viewModel);

        void navigateToNextScreen();

        void navigateToPreviousScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);

        void injectModel(Model model);

        void onResumeCalled();

        void onCreateCalled();

        void onRecreateCalled();

        void onBackButtonPressed();

        void onPauseCalled();

        void onDestroyCalled();
    }

    interface Model {
        String getStoredData();

        String getSavedData();

        String getCurrentData();

        void setCurrentData(String data);

        void onUpdatedDataFromRecreatedScreen(String data);

        void onUpdatedDataFromNextScreen(String data);

        void onUpdatedDataFromPreviousScreen(String data);
    }

}
>>>>>>> theirs

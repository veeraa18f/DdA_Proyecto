package InvestTrack.register;

import java.lang.ref.WeakReference;

import android.util.Log;

public class RegisterPresenter implements RegisterContract.Presenter {

    public static String TAG = "InvesTrackSource.RegisterPresenter";

    private WeakReference<RegisterContract.View> view;
    private AppMediator mediator;
    private RegisterContract.Model model;
    private RegisterState state;

    public RegisterPresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new RegisterState();


        // use saved state if is necessary
        SavedPreviousRegisterState savedState = getStateFromPreviousScreen();
        if (savedState != null) {

            // update the model if is necessary
            model.onUpdatedDataFromPreviousScreen(savedState.data);

        }

    }

    @Override
    public void onRecreateCalled() {
        Log.e(TAG, "onRecreateCalled()");

        // call the mediator to initialize the state
        state = getSavedScreenState();

        // update the model if is necessary
        model.onUpdatedDataFromRecreatedScreen(state.data);
    }

    @Override
    public void onResumeCalled() {
        Log.e(TAG, "onResumeCalled()");


        // use passed state if is necessary
        SavedNextRegisterState savedState = getStateFromNextScreen();
        if (savedState != null) {

            // update the model if is necessary
            model.onUpdatedDataFromNextScreen(savedState.data);

        }

        // call the model and initialize the state
        state.data = model.getCurrentData();

        // update the view
        view.get().onRefreshViewWithUpdatedData(state);

    }

    @Override
    public void onBackButtonPressed() {
        Log.e(TAG, "onBackButtonPressed()");

    }

    @Override
    public void onPauseCalled() {
        Log.e(TAG, "onPauseCalled()");

        // save the state
        saveScreenState();
    }

    @Override
    public void onDestroyCalled() {
        Log.e(TAG, "onDestroyCalled()");

        // reset the state if is necessary
        //resetScreenState();
    }

    private RegisterState getSavedScreenState() {
        return mediator.getRegisterScreenState();
    }

    private void saveScreenState() {
        mediator.setRegisterScreenState(state);
    }


  /*
  private void resetScreenState() {
    mediator.resetRegisterScreenState();
  }
  */

    private SavedNextRegisterState getStateFromNextScreen() {
        return mediator.getNextRegisterScreenState();
    }

    private void passStateToNextScreen(NewNextRegisterState state) {
        mediator.setNextRegisterScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousRegisterState state) {
        mediator.setPreviousRegisterScreenState(state);
    }

    private SavedPreviousRegisterState getStateFromPreviousScreen() {
        return mediator.getPreviousRegisterScreenState();
    }

    @Override
    public void injectView(WeakReference<RegisterContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(RegisterContract.Model model) {
        this.model = model;
    }

}

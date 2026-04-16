package InvestTrack.profile;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class ProfileScreen {

<<<<<<< ours
  public static void configure(ProfileContract.View view) {

    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);


    AppMediator mediator = AppMediator.getInstance();
    ProfileContract.Presenter presenter = new ProfilePresenter(mediator);

    String data = context.get().getString(R.string.app_name);
    ProfileContract.Model model = new ProfileModel(data);

    presenter.injectModel(model);
    presenter.injectView(new WeakReference<>(view));
    view.injectPresenter(presenter);

  }
=======
    public static void configure(ProfileContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        ProfileContract.Presenter presenter = new ProfilePresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        ProfileContract.Model model = new ProfileModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
>>>>>>> theirs
}

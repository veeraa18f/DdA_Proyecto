package InvestTrack.home;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class HomeScreen {

<<<<<<< ours
  public static void configure(HomeContract.View view) {

    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);


    AppMediator mediator = AppMediator.getInstance();
    HomeContract.Presenter presenter = new HomePresenter(mediator);

    String data = context.get().getString(R.string.app_name);
    HomeContract.Model model = new HomeModel(data);

    presenter.injectModel(model);
    presenter.injectView(new WeakReference<>(view));
    view.injectPresenter(presenter);

  }
=======
    public static void configure(HomeContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        HomeContract.Presenter presenter = new HomePresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        HomeContract.Model model = new HomeModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
>>>>>>> theirs
}

package InvestTrack.favorites;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class FavoritesScreen {

<<<<<<< ours
  public static void configure(FavoritesContract.View view) {

    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);


    AppMediator mediator = AppMediator.getInstance();
    FavoritesContract.Presenter presenter = new FavoritesPresenter(mediator);

    String data = context.get().getString(R.string.app_name);
    FavoritesContract.Model model = new FavoritesModel(data);

    presenter.injectModel(model);
    presenter.injectView(new WeakReference<>(view));
    view.injectPresenter(presenter);

  }
=======
    public static void configure(FavoritesContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        FavoritesContract.Presenter presenter = new FavoritesPresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        FavoritesContract.Model model = new FavoritesModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
>>>>>>> theirs
}

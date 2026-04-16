package InvestTrack.manage;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class ManageInvestmentScreen {

<<<<<<< ours
  public static void configure(ManageInvestmentContract.View view) {

    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);


    AppMediator mediator = AppMediator.getInstance();
    ManageInvestmentContract.Presenter presenter = new ManageInvestmentPresenter(mediator);

    String data = context.get().getString(R.string.app_name);
    ManageInvestmentContract.Model model = new ManageInvestmentModel(data);

    presenter.injectModel(model);
    presenter.injectView(new WeakReference<>(view));
    view.injectPresenter(presenter);

  }
=======
    public static void configure(ManageInvestmentContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        ManageInvestmentContract.Presenter presenter = new ManageInvestmentPresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        ManageInvestmentContract.Model model = new ManageInvestmentModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
>>>>>>> theirs
}

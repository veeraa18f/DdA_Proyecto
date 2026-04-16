package InvestTrack.register;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class RegisterScreen {

    public static void configure(RegisterContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        RegisterContract.Presenter presenter = new RegisterPresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        RegisterContract.Model model = new RegisterModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
}

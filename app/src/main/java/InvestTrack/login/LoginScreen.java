package InvestTrack.login;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class LoginScreen {

    public static void configure(LoginContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        AppMediator mediator = AppMediator.getInstance();
        LoginContract.Presenter presenter = new LoginPresenter(mediator);

        String data = context.get().getString(R.string.app_name);
        LoginContract.Model model = new LoginModel(data);

        presenter.injectModel(model);
        presenter.injectView(new WeakReference<>(view));
        view.injectPresenter(presenter);

    }
}

package com.warunya.ricenearby.ui.register;

public interface RegisterContract {

    interface Presenter {
        void register(String username, String email, String password, String confirmPassword);
    }

    interface View {
        void showUsernameInvalid();

        void showEmailInvalid();

        void showPasswordInvalid();

        void showConfirmPasswordNotMatch();

        void registerSuccess();

        void registerFailed();
    }
}

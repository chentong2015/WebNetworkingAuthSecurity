package login_demo.form;

import lombok.Getter;

@Getter
public class SignInForm {

    private String login;
    private String password;

    private String captchaResponse;
}

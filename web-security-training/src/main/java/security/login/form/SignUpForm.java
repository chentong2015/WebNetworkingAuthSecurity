package security.login.form;

import lombok.Getter;

@Getter
public class SignUpForm {

    private String login;
    private String password;

    private String captchaResponse;
}

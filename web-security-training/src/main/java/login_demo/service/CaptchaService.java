package login_demo.service;

// TODO. 设计Captcha验证码逻辑:
// - 数据库中维护一张有效的字符或图形验证码表，动态更新 + 加密存储 ?
// - 用户提交时验证是否有效，是否存在表中
// - 用户选择刷新时自动获取下一个有效码，提供给用户
// - 码表中数据用完时自动生成并插入表中，提供给用户
// - 不同的用户可以提交相同的验证码(只要正确)，无需根据用户定制
public class CaptchaService {

    public void processResponse(String captchaResponse) {
        if (!captchaResponse.equals("2345KAQ")) {
            throw new RuntimeException();
        }
    }
}
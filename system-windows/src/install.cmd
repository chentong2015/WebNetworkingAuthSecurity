echo Window平台cmd发送http请求的指令, 注意请求配置的参数
wget --header="authorization: Basic xxxxxxxxxx" ^
  --header="content-type: application/x-www-form-urlencoded" ^
  --post-data="scope=openid&response_type=code&client_id=1&redirect_uri=..." ^
  -O ^
  http://www.ctong.com

IF EXIST output\nul (
    rmdir /s /q output
)

echo Copy指定的目录
xcopy /y /s /q packaging\resources\target\resources-%VERSION%-dist output\primary\

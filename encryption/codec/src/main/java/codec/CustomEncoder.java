package codec;

// 自定义字符串的随机编码算法
public class CustomEncoder {

    public static void main(String[] args) {
        byte[] bytes = encodeStringText("2147483647001234");
        String byteStr = new String(bytes);
        System.out.println(byteStr);
    }

    public static byte[] encodeStringText(String strText) {
        strText = strText.trim();
        byte[] bytes = new byte[strText.length() / 4];
        for (int i = 0; i < strText.length(); i += 4) {
            String str = strText.substring(i, i + 4);
            byte temp;
            try {
                temp = Byte.parseByte(str, 16);
            } catch (NumberFormatException var8) {
                int iByte = Integer.parseInt(str, 16);
                temp = (byte) (iByte - 256);
            }
            bytes[i / 4] = temp;
        }
        return bytes;
    }
}

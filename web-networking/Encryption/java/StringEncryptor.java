// 自定义String字符串加密算法：随机性+难破解性
public class StringEncryptor {

    public static byte[] changeToDecryptedString(String strText) {
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

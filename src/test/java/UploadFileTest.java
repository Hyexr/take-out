import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : PACKAGE_NAME
 * @ClassName : UploadFileTest.java
 * @createTime : 2023/3/22 11:20
 */
public class UploadFileTest {
    @Test
    public void test1(){
        String originalFilename = "11.jpg";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println(suffix);
    }
}

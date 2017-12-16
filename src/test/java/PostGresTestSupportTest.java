import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class PostGresTestSupportTest extends TestSupport {

    @Test
    public void testInsertIntoDB() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("title", "Cool API Demo!");
        }};

        verifyQuery("postgres_add_table.json", "POST", "/talk", parameters);
    }

}

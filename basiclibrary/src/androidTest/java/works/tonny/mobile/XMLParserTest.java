package works.tonny.mobile;

import android.app.Application;
import android.test.ApplicationTestCase;

import works.tonny.mobile.utils.XMLParser;

/**
 * Created by tonny on 2016/3/21.
 */
public class XMLParserTest extends ApplicationTestCase<Application> {
    public XMLParserTest(Class<Application> applicationClass) {
        super(applicationClass);
    }


    public void test() throws Exception {
        String xml = "";
        XMLParser parser = new XMLParser();
        parser.parse(xml);

    }

}

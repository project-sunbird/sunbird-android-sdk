import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.AppContext;

/**
 * Created by shriharsh on 14/4/17.
 */

public class Test {

    private void initGenie() {
    GenieService.init((new AppContext.Builder(null,"")).build());
}
}


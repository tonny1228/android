package works.tonny.mobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by tonny on 2015/6/24.
 */
public class IntentUtils {

    public static Intent newInstance(Context context, String intentCategory, String intentAction, String intentData, String intentClass) {
        Intent intent = new Intent();
        if (intentClass != null) {
            intent.setClassName(context, intentClass);
            return intent;
        }
        if (intentCategory != null)
            intent.addCategory(intentCategory);
        if (intentAction != null) {
            intent.setAction(intentAction);
        }
        if (intentData != null) {
            intent.setData(Uri.parse(intentData));
        }
        return intent;
    }

    public static Intent newInstance(Context context, String intentCategory, String intentAction, String intentData) {
        Intent intent = new Intent();

        if (intentCategory != null)
            intent.addCategory(intentCategory);
        if (intentAction != null) {
            intent.setAction(intentAction);
        }
        if (intentData != null) {
            intent.setData(Uri.parse(intentData));
        }
        return intent;
    }

    public static Intent newInstance(Context context, Class intentClass, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        if (intentClass != null) {
            intent.setClass(context, intentClass);
            return intent;
        }
        return intent;
    }

    public static Intent newInstance(Context context, Class intentClass, Map<String, Object> bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                intent.putExtra(key, (Serializable) bundle.get(key));
            }
        }
        if (intentClass != null) {
            intent.setClass(context, intentClass);
            return intent;
        }
        return intent;
    }

    public static Intent newInstance(Context context, Class intentClass, String... params) {
        Intent intent = new Intent();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                intent.putExtra(params[i++], params[i]);
            }
        }
        if (intentClass != null) {
            intent.setClass(context, intentClass);
            return intent;
        }
        return intent;
    }
}

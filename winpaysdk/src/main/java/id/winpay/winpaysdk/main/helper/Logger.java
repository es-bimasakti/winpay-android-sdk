package id.winpay.winpaysdk.main.helper;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class Logger {

    private static Logger logger;
    private final boolean is_loggable;

    private Logger(Context context) {
        AppConfig config = AppConfig.getConfig(context);

        is_loggable = config.is_loggable();// && config.is_sandbox_mode();
    }

    public static Logger getLogger(Context context) {
        if (logger == null) {
            logger = new Logger(context);
        }

        return logger;
    }

    public void log(String tag, Object o) {
        log(tag, o, null);
    }

    public void log(String tag, Object o, Throwable th) {
        if (is_loggable) {
            String str;
            if (o instanceof Serializable) {
                str = o.toString();
            } else {
                str = printFields(o);
            }
            if (th != null) {
                Log.e(tag, str, th);
            } else {
                Log.e(tag, str);
            }
        }
    }

    private static String printFields(Object obj) {
        StringBuilder result = new StringBuilder();
        Class<?> objClass = obj.getClass();

        Field[] fields = objClass.getFields();
        for (Field field : fields) {
            String name = field.getName();
            Object value;
            try {
                value = field.get(obj);
                result.append(name).append(": ").append(value).append("\n");
            } catch (IllegalAccessException ignored) {
            } catch (NullPointerException ignored) {
            }
        }

        return result.toString();
    }

}

package id.winpay.winpaysdk.main.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.winpay.winpaysdk.util.Var;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public class SuperMessage extends HashMap<String, Object> {

    public static final String INDEX_RC = "rc";
    public static final String INDEX_DESC = "rd";
    public static final String INDEX_DATA = "data";

    public static final String RESULT_OK = "00";
    public static final String RESULT_DATA_NOT_FOUND = "04";
    public static final String RESULT_NOK = "05";
    public static final String RESULT_INVALID_REQ = "99";
    public static final String RESULT_UNPARSED = "NOT";
    public static final String RESULT_UNKNOWN = "IDK";
    public static final String RESULT_CANCELLED = "XYZ";

    SuperMessage() {
        super();
    }

    SuperMessage(Map<String, Object> map) {
        this();

        putAll(map);
    }

    public boolean isOK() {
        return getResponse_code().equals(RESULT_OK);
    }

    public String getResponse_code() {
        return Var.toString(getItem(INDEX_RC), RESULT_NOK);
    }

    public void setResponse_code(String response_code) {
        setItem(INDEX_RC, response_code);
    }

    public String getMessageDescription() {
        return Var.toString(getItem(INDEX_DESC), "");
    }

    public void setMessageDescription(String message) {
        setItem(INDEX_DESC, message);
    }

    protected void setItem(String key, Object value) {
        put(key, value);
    }

    protected Object getItem(String key) {
        return get(key);
    }

    public Map getDataAsMap() {
        return Var.toHashMap(getItem("data"));
    }

    public List getDataAsList() {
        return Var.toArrayList(getItem("data"));
    }

}

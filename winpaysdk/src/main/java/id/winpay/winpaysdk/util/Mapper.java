package id.winpay.winpaysdk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class Mapper {

    public static <K, V> K getKeyFromValue(Map<K, V> hm, V value) {
        for (K o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static JSONArray arrayToJSON(Object data) throws JSONException {
        if (!data.getClass().isArray()) {
            throw new JSONException("Not a primitive data: " + data.getClass());
        }
        final int length = Array.getLength(data);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < length; ++i) {
            jsonArray.put(wrap(Array.get(data, i)));
        }

        return jsonArray;
    }

    public static JSONArray collectionToJSON(Collection<?> data) {
        JSONArray jsonArray = new JSONArray();
        if (data != null) {
            for (Object aData : data) {
                jsonArray.put(wrap(aData));
            }
        }
        return jsonArray;
    }

    public static JSONObject mapToJSON(Map<?, ?> message) {
        JSONObject object = new JSONObject();

        for (Map.Entry<?, ?> entry : message.entrySet()) {
            /*
             * Deviate from the original by checking that keys are non-null and
             * of the proper type. (We still defer validating the values).
             */
            String key = (String) entry.getKey();
            if (key == null) {
                throw new NullPointerException("key == null");
            }
            try {
                object.put(key, wrap(entry.getValue()));
            } catch (JSONException ignored) {
            }
        }

        return object;
    }

    public static String mapToQueryString(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            if (entry.getValue() instanceof List) {
                sb.append(listToQueryString(entry.getKey().toString(), (List<?>) entry.getValue()));
            } else {
                sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue() != null ? entry.getValue().toString() : "")));
            }
        }
        return sb.toString();
    }

    public static String listToQueryString(String key, List<?> list) {
        key = urlEncodeUTF8(key);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof Map) {
                Map ee = (Map) o;
                for (Object j : ee.keySet()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(String.format(Locale.getDefault(), "%s[%d][%s]=%s",
                            key, i,
                            urlEncodeUTF8(j.toString()),
                            urlEncodeUTF8(ee.get(j) != null ? ee.get(j).toString() : "")));
                }
            } else if (o instanceof List) {
                List ee = (List) o;
                for (int j = 0; j < ee.size(); j++) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(String.format(Locale.getDefault(), "%s[%d][%d]=%s",
                            key, i, j,
                            urlEncodeUTF8(ee.get(j) != null ? ee.get(j).toString() : "")));
                }
            } else {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(String.format(Locale.getDefault(), "%s[%d]=%s",
                        key, i, urlEncodeUTF8(o != null ? o.toString() : "")));
            }
        }
        return sb.toString();
    }

    public static LinkedHashMap<String, Object> jsonToMap(String root) {
        try {
            return jsonToMap(new JSONObject(root));
        } catch (JSONException e) {
            return new LinkedHashMap<>();
        }
    }

    public static LinkedHashMap<String, Object> jsonToMap(JSONObject root) throws JSONException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Iterator<String> keys = root.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object obj = root.get(key);

            if (obj == null) {
                map.put(key, "");
            } else if (obj instanceof JSONObject) {
                map.put(key, jsonToMap((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                map.put(key, jsonToList((JSONArray) obj));
            } else {
                map.put(key, obj.toString());
            }
        }
        return map;
    }

    public static ArrayList<Object> jsonToList(JSONArray root) throws JSONException {
        ArrayList<Object> arr = new ArrayList<>();
        for (int i = 0; i < root.length(); i++) {
            Object obj = root.get(i);

            if (obj == null) {
                arr.add("");
            } else if (obj instanceof JSONObject) {
                arr.add(jsonToMap((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                arr.add(jsonToList((JSONArray) obj));
            } else {
                arr.add(obj.toString());
            }
        }
        return arr;
    }

    public static Map<String, String> queryStringToMap(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    @SuppressWarnings("rawtypes")
    private static Object wrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return collectionToJSON((Collection) o);
            } else if (o.getClass().isArray()) {
                return arrayToJSON(o);
            }
            if (o instanceof Map) {
                return mapToJSON((Map) o);
            }
            if (o instanceof Boolean || o instanceof Byte || o instanceof Character
                    || o instanceof Double || o instanceof Float || o instanceof Integer
                    || o instanceof Long || o instanceof Short || o instanceof String) {
                return o;
            }
            if (o instanceof CharSequence) {
                return o.toString();
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}

package id.winpay.winpaysdk.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class Var {
    public static double toDouble(Object obj, double def) {
        if (obj instanceof Double) {
            return (double) obj;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue();
        } else if (obj instanceof String) {
            try {
                return Double.parseDouble(obj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        return def;
    }

    public static int toInt(Object obj, int def) {
        if (obj instanceof Integer) {
            return (int) obj;
        } else if (obj instanceof Double) {
            return ((Double) obj).intValue();
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        return def;
    }

    public static long toLong(Object obj, long def) {
        if (obj instanceof Long) {
            return (long) obj;
        } else if (obj instanceof String) {
            try {
                return Long.parseLong(obj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        return def;
    }

    public static float toFloat(Object obj, float def) {
        if (obj instanceof Float) {
            return (float) obj;
        } else if (obj instanceof String) {
            try {
                return Float.parseFloat(obj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        return def;
    }

    public static boolean toBoolean(Object obj, boolean def) {
        if (obj instanceof Boolean) {
            return (boolean) obj;
        } else if (obj instanceof String) {
            try {
                return Boolean.parseBoolean(obj.toString());
            } catch (Exception ignored) {
            }
        }

        return def;
    }

    public static String toString(Object obj, String def) {
        if (obj instanceof String || obj instanceof CharSequence) {
            return obj.toString();
        } else if (obj instanceof Serializable) {
            return obj.toString();
        }

        return def;
    }

    @SuppressWarnings("unchecked")
    public static <V> ArrayList<V> toArrayList(Object obj) {
        if (obj instanceof ArrayList) {
            try {
                return (ArrayList<V>) obj;
            } catch (Exception ignored) {
            }
        } else if (obj instanceof List) {
            try {
                return new ArrayList<>((List<V>) obj);
            } catch (Exception ignored) {
            }
        }

        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> toHashMap(Object obj) {
        if (obj instanceof HashMap) {
            try {
                return (HashMap<K, V>) obj;
            } catch (Exception ignored) {
            }
        } else if (obj instanceof Map) {
            try {
                return new HashMap<>((Map<K, V>) obj);
            } catch (Exception ignored) {
            }
        }

        return new HashMap<>();
    }

    public static String getIgnoreBound(String[] arr, int index, String def) {
        if (index >= arr.length) {
            return def;
        } else {
            return toString(arr[index], def);
        }
    }

    public static double getIgnoreBound(double[] arr, int index, double def) {
        if (index >= arr.length) {
            return def;
        } else {
            return arr[index];
        }
    }

    public static boolean getIgnoreBound(boolean[] arr, int index, boolean def) {
        if (index >= arr.length) {
            return def;
        } else {
            return arr[index];
        }
    }

    public static int getIgnoreBound(int[] arr, int index, int def) {
        if (index >= arr.length) {
            return def;
        } else {
            return arr[index];
        }
    }

    /**
     * Checks whether any of the given parameters are null
     *
     * @param object the object/objects/array of objects to check
     * @return true if any give parameter is null
     */
    public static boolean isNull(Object... object) {
        for (Object object1 : object) {
            if (object1 == null) {
                return true;
            }
        }
        return false;
    }
}

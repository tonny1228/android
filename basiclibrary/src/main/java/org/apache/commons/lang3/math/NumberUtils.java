//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.commons.lang3.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;

public class NumberUtils {
    public static final Long LONG_ZERO = Long.valueOf(0L);
    public static final Long LONG_ONE = Long.valueOf(1L);
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    public static final Short SHORT_ZERO = Short.valueOf((short)0);
    public static final Short SHORT_ONE = Short.valueOf((short)1);
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
    public static final Byte BYTE_ZERO = Byte.valueOf((byte)0);
    public static final Byte BYTE_ONE = Byte.valueOf((byte)1);
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte)-1);
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0D);
    public static final Double DOUBLE_ONE = Double.valueOf(1.0D);
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0D);
    public static final Float FLOAT_ZERO = Float.valueOf(0.0F);
    public static final Float FLOAT_ONE = Float.valueOf(1.0F);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0F);

    public NumberUtils() {
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(String str, int defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static long toLong(String str, long defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException var4) {
                return defaultValue;
            }
        }
    }

    public static float toFloat(String str) {
        return toFloat(str, 0.0F);
    }

    public static float toFloat(String str, float defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    public static double toDouble(String str) {
        return toDouble(str, 0.0D);
    }

    public static double toDouble(String str, double defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException var4) {
                return defaultValue;
            }
        }
    }

    public static byte toByte(String str) {
        return toByte(str, (byte)0);
    }

    public static byte toByte(String str, byte defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Byte.parseByte(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    public static short toShort(String str) {
        return toShort(str, (short)0);
    }

    public static short toShort(String str, short defaultValue) {
        if(str == null) {
            return defaultValue;
        } else {
            try {
                return Short.parseShort(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    public static Number createNumber(String str) throws NumberFormatException {
        if(str == null) {
            return null;
        } else if(StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else if(str.startsWith("--")) {
            return null;
        } else if(!str.startsWith("0x") && !str.startsWith("-0x") && !str.startsWith("0X") && !str.startsWith("-0X")) {
            char lastChar = str.charAt(str.length() - 1);
            int decPos = str.indexOf(46);
            int expPos = str.indexOf(101) + str.indexOf(69) + 1;
            String mant;
            String dec;
            if(decPos > -1) {
                if(expPos > -1) {
                    if(expPos < decPos || expPos > str.length()) {
                        throw new NumberFormatException(str + " is not a valid number.");
                    }

                    dec = str.substring(decPos + 1, expPos);
                } else {
                    dec = str.substring(decPos + 1);
                }

                mant = str.substring(0, decPos);
            } else {
                if(expPos > -1) {
                    if(expPos > str.length()) {
                        throw new NumberFormatException(str + " is not a valid number.");
                    }

                    mant = str.substring(0, expPos);
                } else {
                    mant = str;
                }

                dec = null;
            }

            String exp;
            if(!Character.isDigit(lastChar) && lastChar != 46) {
                if(expPos > -1 && expPos < str.length() - 1) {
                    exp = str.substring(expPos + 1, str.length() - 1);
                } else {
                    exp = null;
                }

                String allZeros1 = str.substring(0, str.length() - 1);
                boolean nfe2 = isAllZeros(mant) && isAllZeros(exp);
                switch(lastChar) {
                    case 'D':
                    case 'd':
                        break;
                    case 'F':
                    case 'f':
                        try {
                            Float e = createFloat(allZeros1);
                            if(e.isInfinite() || e.floatValue() == 0.0F && !nfe2) {
                                break;
                            }

                            return e;
                        } catch (NumberFormatException var15) {
                            break;
                        }
                    case 'L':
                    case 'l':
                        if(dec == null && exp == null && (allZeros1.charAt(0) == 45 && isDigits(allZeros1.substring(1)) || isDigits(allZeros1))) {
                            try {
                                return createLong(allZeros1);
                            } catch (NumberFormatException var11) {
                                return createBigInteger(allZeros1);
                            }
                        }

                        throw new NumberFormatException(str + " is not a valid number.");
                    default:
                        throw new NumberFormatException(str + " is not a valid number.");
                }

                try {
                    Double e1 = createDouble(allZeros1);
                    if(!e1.isInfinite() && ((double)e1.floatValue() != 0.0D || nfe2)) {
                        return e1;
                    }
                } catch (NumberFormatException var14) {
                    ;
                }

                try {
                    return createBigDecimal(allZeros1);
                } catch (NumberFormatException var13) {
                    throw new NumberFormatException(str + " is not a valid number.");
                }
            } else {
                if(expPos > -1 && expPos < str.length() - 1) {
                    exp = str.substring(expPos + 1, str.length());
                } else {
                    exp = null;
                }

                if(dec == null && exp == null) {
                    try {
                        return createInteger(str);
                    } catch (NumberFormatException var12) {
                        try {
                            return createLong(str);
                        } catch (NumberFormatException var10) {
                            return createBigInteger(str);
                        }
                    }
                } else {
                    boolean allZeros = isAllZeros(mant) && isAllZeros(exp);

                    try {
                        Float nfe = createFloat(str);
                        if(!nfe.isInfinite() && (nfe.floatValue() != 0.0F || allZeros)) {
                            return nfe;
                        }
                    } catch (NumberFormatException var17) {
                        ;
                    }

                    try {
                        Double nfe1 = createDouble(str);
                        if(!nfe1.isInfinite() && (nfe1.doubleValue() != 0.0D || allZeros)) {
                            return nfe1;
                        }
                    } catch (NumberFormatException var16) {
                        ;
                    }

                    return createBigDecimal(str);
                }
            }
        } else {
            return createInteger(str);
        }
    }

    private static boolean isAllZeros(String str) {
        if(str == null) {
            return true;
        } else {
            for(int i = str.length() - 1; i >= 0; --i) {
                if(str.charAt(i) != 48) {
                    return false;
                }
            }

            return str.length() > 0;
        }
    }

    public static Float createFloat(String str) {
        return str == null?null:Float.valueOf(str);
    }

    public static Double createDouble(String str) {
        return str == null?null:Double.valueOf(str);
    }

    public static Integer createInteger(String str) {
        return str == null?null:Integer.decode(str);
    }

    public static Long createLong(String str) {
        return str == null?null:Long.decode(str);
    }

    public static BigInteger createBigInteger(String str) {
        return str == null?null:new BigInteger(str);
    }

    public static BigDecimal createBigDecimal(String str) {
        if(str == null) {
            return null;
        } else if(StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else {
            return new BigDecimal(str);
        }
    }

    public static long min(long[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            long min = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(array[i] < min) {
                    min = array[i];
                }
            }

            return min;
        }
    }

    public static int min(int[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            int min = array[0];

            for(int j = 1; j < array.length; ++j) {
                if(array[j] < min) {
                    min = array[j];
                }
            }

            return min;
        }
    }

    public static short min(short[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            short min = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(array[i] < min) {
                    min = array[i];
                }
            }

            return min;
        }
    }

    public static byte min(byte[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            byte min = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(array[i] < min) {
                    min = array[i];
                }
            }

            return min;
        }
    }

    public static double min(double[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double min = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(Double.isNaN(array[i])) {
                    return 0.0D / 0.0;
                }

                if(array[i] < min) {
                    min = array[i];
                }
            }

            return min;
        }
    }

    public static float min(float[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float min = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(Float.isNaN(array[i])) {
                    return 0.0F / 0.0F;
                }

                if(array[i] < min) {
                    min = array[i];
                }
            }

            return min;
        }
    }

    public static long max(long[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            long max = array[0];

            for(int j = 1; j < array.length; ++j) {
                if(array[j] > max) {
                    max = array[j];
                }
            }

            return max;
        }
    }

    public static int max(int[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            int max = array[0];

            for(int j = 1; j < array.length; ++j) {
                if(array[j] > max) {
                    max = array[j];
                }
            }

            return max;
        }
    }

    public static short max(short[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            short max = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(array[i] > max) {
                    max = array[i];
                }
            }

            return max;
        }
    }

    public static byte max(byte[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            byte max = array[0];

            for(int i = 1; i < array.length; ++i) {
                if(array[i] > max) {
                    max = array[i];
                }
            }

            return max;
        }
    }

    public static double max(double[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double max = array[0];

            for(int j = 1; j < array.length; ++j) {
                if(Double.isNaN(array[j])) {
                    return 0.0D / 0.0;
                }

                if(array[j] > max) {
                    max = array[j];
                }
            }

            return max;
        }
    }

    public static float max(float[] array) {
        if(array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if(array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float max = array[0];

            for(int j = 1; j < array.length; ++j) {
                if(Float.isNaN(array[j])) {
                    return 0.0F / 0.0F;
                }

                if(array[j] > max) {
                    max = array[j];
                }
            }

            return max;
        }
    }

    public static long min(long a, long b, long c) {
        if(b < a) {
            a = b;
        }

        if(c < a) {
            a = c;
        }

        return a;
    }

    public static int min(int a, int b, int c) {
        if(b < a) {
            a = b;
        }

        if(c < a) {
            a = c;
        }

        return a;
    }

    public static short min(short a, short b, short c) {
        if(b < a) {
            a = b;
        }

        if(c < a) {
            a = c;
        }

        return a;
    }

    public static byte min(byte a, byte b, byte c) {
        if(b < a) {
            a = b;
        }

        if(c < a) {
            a = c;
        }

        return a;
    }

    public static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    public static long max(long a, long b, long c) {
        if(b > a) {
            a = b;
        }

        if(c > a) {
            a = c;
        }

        return a;
    }

    public static int max(int a, int b, int c) {
        if(b > a) {
            a = b;
        }

        if(c > a) {
            a = c;
        }

        return a;
    }

    public static short max(short a, short b, short c) {
        if(b > a) {
            a = b;
        }

        if(c > a) {
            a = c;
        }

        return a;
    }

    public static byte max(byte a, byte b, byte c) {
        if(b > a) {
            a = b;
        }

        if(c > a) {
            a = c;
        }

        return a;
    }

    public static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    public static boolean isDigits(String str) {
        if(StringUtils.isEmpty(str)) {
            return false;
        } else {
            for(int i = 0; i < str.length(); ++i) {
                if(!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumber(String str) {
        if(StringUtils.isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == 45?1:0;
            int i;
            if(sz > start + 1 && chars[start] == 48 && chars[start + 1] == 120) {
                i = start + 2;
                if(i == sz) {
                    return false;
                } else {
                    while(i < chars.length) {
                        if((chars[i] < 48 || chars[i] > 57) && (chars[i] < 97 || chars[i] > 102) && (chars[i] < 65 || chars[i] > 70)) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for(i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if(chars[i] >= 48 && chars[i] <= 57) {
                        foundDigit = true;
                        allowSigns = false;
                    } else if(chars[i] == 46) {
                        if(hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if(chars[i] != 101 && chars[i] != 69) {
                        if(chars[i] != 43 && chars[i] != 45) {
                            return false;
                        }

                        if(!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if(hasExp) {
                            return false;
                        }

                        if(!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                return i < chars.length?(chars[i] >= 48 && chars[i] <= 57?true:(chars[i] != 101 && chars[i] != 69?(chars[i] == 46?(!hasDecPoint && !hasExp?foundDigit:false):(allowSigns || chars[i] != 100 && chars[i] != 68 && chars[i] != 102 && chars[i] != 70?(chars[i] != 108 && chars[i] != 76?false:foundDigit && !hasExp && !hasDecPoint):foundDigit)):false)):!allowSigns && foundDigit;
            }
        }
    }
}

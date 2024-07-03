package de.neemann.digital.hdl.hgs;

import static de.neemann.digital.hdl.hgs.Value.*;

public class ValueOperation {
    /**
     * Compares two values
     *
     * @param a a value
     * @param b a value
     * @return true if both values are equal
     */
    public static boolean equals(Object a, Object b) {
        if (a instanceof Double || b instanceof Double)
            return a.equals(b);
        else if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() == ((Number) b).longValue();
        else if (a instanceof String || b instanceof String)
            return a.toString().equals(b.toString());
        else
            return a.equals(b);
    }

    /**
     * Adds two values
     *
     * @param a a value
     * @param b a value
     * @return the sum
     * @throws HGSEvalException HGSEvalException
     */
    public static Object add(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) + toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() + ((Number) b).longValue();
        if (a instanceof String || b instanceof String)
            return a.toString() + b.toString();
        throw new HGSEvalException("arguments must be int or string, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }

    /**
     * Subtracts two values
     *
     * @param a a value
     * @param b a value
     * @return the sum
     * @throws HGSEvalException HGSEvalException
     */
    public static Object sub(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) - toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() - ((Number) b).longValue();
        throw new HGSEvalException("arguments must be int or double, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }

    /**
     * Multiplies two values
     *
     * @param a a value
     * @param b a value
     * @return the product
     * @throws HGSEvalException HGSEvalException
     */
    public static Object mul(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) * toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() * ((Number) b).longValue();
        throw new HGSEvalException("arguments must be int or double, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }

    /**
     * Divides two numbers
     *
     * @param a a value
     * @param b a value
     * @return the quotient
     * @throws HGSEvalException HGSEvalException
     */
    public static Object div(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) / toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() / ((Number) b).longValue();
        throw new HGSEvalException("arguments must be int or double, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }

    /**
     * Performs an or operation
     *
     * @param a a value
     * @param b a value
     * @return the or'ed values
     * @throws HGSEvalException HGSEvalException
     */
    public static Object or(Object a, Object b) throws HGSEvalException {
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() | ((Number) b).longValue();
        return toBool(a) || toBool(b);
    }

    /**
     * Performs an xor operation
     *
     * @param a a value
     * @param b a value
     * @return the xor'ed values
     * @throws HGSEvalException HGSEvalException
     */
    public static Object xor(Object a, Object b) throws HGSEvalException {
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() ^ ((Number) b).longValue();
        return toBool(a) ^ toBool(b);
    }

    /**
     * Performs an and operation
     *
     * @param a a value
     * @param b a value
     * @return the and'ed values
     * @throws HGSEvalException HGSEvalException
     */
    public static Object and(Object a, Object b) throws HGSEvalException {
        if (a instanceof Number && b instanceof Number)
            return ((Number) a).longValue() & ((Number) b).longValue();
        return toBool(a) && toBool(b);
    }

    /**
     * Performs a not operation
     *
     * @param value a value
     * @return the inverted value
     * @throws HGSEvalException HGSEvalException
     */
    public static Object not(Object value) throws HGSEvalException {
        if (value instanceof Number)
            return ~((Number) value).longValue();
        return !toBool(value);
    }

    /**
     * Changes the sign of the given value
     *
     * @param value the value
     * @return value with changed sign
     * @throws HGSEvalException HGSEvalException
     */
    public static Object neg(Object value) throws HGSEvalException {
        if (value instanceof Double)
            return -(Double) value;
        return -toLong(value);
    }

    /**
     * Helper compare two values
     *
     * @param a a value
     * @param b a value
     * @return true if a&lt;b
     * @throws HGSEvalException HGSEvalException
     */
    public static boolean less(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) < toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return toLong(a) < toLong(b);
        if (a instanceof String && b instanceof String)
            return a.toString().compareTo(b.toString()) < 0;
        throw new HGSEvalException("arguments must be int, double or string, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }

    /**
     * Helper compare two values
     *
     * @param a a value
     * @param b a value
     * @return true if a<=b
     * @throws HGSEvalException HGSEvalException
     */
    public static boolean lessEqual(Object a, Object b) throws HGSEvalException {
        if (a instanceof Double || b instanceof Double)
            return toDouble(a) <= toDouble(b);
        if (a instanceof Number && b instanceof Number)
            return toLong(a) <= toLong(b);
        if (a instanceof String && b instanceof String)
            return a.toString().compareTo(b.toString()) <= 0;
        throw new HGSEvalException("arguments must be int, double or string, not " + a.getClass().getSimpleName() + "+" + b.getClass().getSimpleName());
    }
}

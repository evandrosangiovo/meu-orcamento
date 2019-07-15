package com.meuorcamento.data;



import com.meuorcamento.utils.formatters.Formatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Example:
 *   SelectionQueryBuilder q = new SelectionQueryBuilder()
 *      .expr("is_awesome", EQ, true)
 *      .expr("money", GT, 50.0f)
 *      .expr("speed", LT, 21.1f)
 *      .expr("door_number", EQ, 123)
 *      .opt("apples", EQ, 0);
 *
 */
public class Criteria {
    public interface Op {
        public String EQ = " = ";
        public String NEQ = " != ";
        public String GT = " > ";
        public String LT = " < ";
        public String GTEQ = " >= ";
        public String LTEQ = " <= ";
        public String IN = " IN ";
        public String IS = " IS ";
        public String ISNOT = " IS NOT ";
        public String REGEXP = " REGEXP ";
    }

    public enum like {
        exact,
        start,
        end,
        all
    }

    private static final String AND = " AND ";
    private static final String OR = " OR ";


    private StringBuilder mBuilder;
    private List<String> mArgs = new ArrayList<>();
    private String mNextOp = null;
    private String orderBy = null;
    private String groupBy = null;
    private int limitStart = -1;
    private int limitEnd = -1;



    public List<String> getArgs() {
        return mArgs;
    }

    public String[] getArgsArray() {
        return mArgs.toArray(new String[0]);
    }

    public Criteria() {
        mBuilder = new StringBuilder();
    }

    public int getLimitStart() {
        return limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    private Criteria expr(String column, String op, String arg, String castArg) {
        ensureOp();
        mBuilder.append(column)
                .append(op)
                .append(castArg);
        mArgs.add(arg);
        mNextOp = null;

        return this;
    }

    private Criteria expr(String column, String op) {
        ensureOp();
        mBuilder.append(column)
                .append(op);
        mNextOp = null;

        return this;
    }

    public Criteria groupBy(String column) {
        if(column == null || column.equalsIgnoreCase(""))
            return this;


        groupBy = column;
        return this;
    }

    public Criteria orderBy(String column) {
        orderByASC(column);
        return this;
    }
    public Criteria orderByASC(String column) {
        if(column == null || column.equalsIgnoreCase(""))
            return this;

        if(orderBy == null) {
            orderBy = column + " asc";
        }
        else
            orderBy += ", " + column + " asc";

        return this;
    }
    public Criteria orderByDESC(String column) {
        if(column == null || column.equalsIgnoreCase(""))
            return this;

        if(orderBy == null)
            orderBy = column + " desc";
        else {
            orderBy += ", " + column + " desc";
        }
        return this;
    }
    public Criteria expr(Criteria builder) {

        List<String> args = builder.getArgs();

        if(args.size() > 0) {
            ensureOp();
            mBuilder.append("(").append(builder).append(")");
            mArgs.addAll(args);
        }

        mNextOp = null;

        return this;
    }

    public Criteria expr(String column, like likeOp, String arg) {

        switch (likeOp) {
            case exact: {
                return expr(column, " like ", arg, "?");
            }
            case start: {
                return expr(column, " like ",String.format("%s%s", "%", arg), "?");
            }
            case end: {
                return expr(column, " like ", String.format("%s%s",arg, "%"), "?");
            }
            case all: {
                return expr(column, " like ", String.format("%s%s%s","%", arg, "%"), "?");
            }
            default:
                return null;
        }
    }

    public Criteria isNull(String column) {
        return expr(column, " IS NULL ");
    }

    public Criteria isNotNull(String column) {
        return expr(column, " IS NOT NULL ");
    }

    public Criteria expr(String column, boolean useNot, like likeOp, String arg) {

        if(useNot) {
            switch (likeOp) {
                case exact: {
                    return expr(column, " not like ", arg, "?");
                }
                case start: {
                    return expr(column, " not like ", String.format("%s%s", "%", arg), "?");
                }
                case end: {
                    return expr(column, " not like ", String.format("%s%s", arg, "%"), "?");
                }
                case all: {
                    return expr(column, " not like ", String.format("%s%s%s", "%", arg, "%"), "?");
                }
                default:
                    return null;
            }

        } else {
            switch (likeOp) {
                case exact: {
                    return expr(column, " like ", arg, "?");
                }
                case start: {
                    return expr(column, " like ", String.format("%s%s", "%", arg), "?");
                }
                case end: {
                    return expr(column, " like ", String.format("%s%s", arg, "%"), "?");
                }
                case all: {
                    return expr(column, " like ", String.format("%s%s%s", "%", arg, "%"), "?");
                }
                default:
                    return null;
            }
        }
    }


    public Criteria expr(String column, String op, boolean arg) {
        return expr(column, op, arg ? "1" : "0", "cast(? as boolean)");
    }

    public Criteria expr(String column, String op, int arg) {
        return expr(column, op, String.valueOf(arg), "cast(? as integer)");
    }

    public Criteria expr(String column, String op, String arg) {
        return expr(column, op, arg, "?");
    }

    public Criteria expr(String column, String op, long arg) {
        return expr(column, op, String.valueOf(arg), "cast(? as integer)");
    }

    public Criteria expr(String column, String op, float arg) {
        return expr(column, op, String.valueOf(arg), "cast(? as decimal)");
    }

    public Criteria expr(String column, String op, double arg) {
        return expr(column, op, String.valueOf(arg), "cast(? as decimal)");
    }

    public Criteria expr(String column, String op, BigDecimal arg) {
        return expr(column, op, arg.toString(), "cast(? as decimal)");
    }

    public Criteria expr(String column, String op, Date arg) {
        return expr(column, op, Formatter.getInstance(arg, Formatter.FormatTypeEnum.DATE_DATABASE).format(), "date(?)");
    }

    public Criteria expr(String column, String op, boolean isDateTime, Date arg) {
        if(isDateTime)
            return expr(column, op, Formatter.getInstance(arg, Formatter.FormatTypeEnum.DATETIME_DATABASE).format(), "datetime(?)");
        else
            return expr(column, op, Formatter.getInstance(arg, Formatter.FormatTypeEnum.DATE_DATABASE).format(), "date(?)");
    }

    public Criteria expr(String column, String op, Date arg, boolean onlyTime) {
        if(onlyTime)
            return expr(column, op, Formatter.getInstance(arg, Formatter.FormatTypeEnum.TIME_NO_DATE).format(), "time(?)");

        return expr(column, op, Formatter.getInstance(arg, Formatter.FormatTypeEnum.DATE_DATABASE).format(), "date(?)");
    }

    public Criteria and() {
        mNextOp = AND;

        return this;
    }

    public Criteria or() {
        mNextOp = OR;

        return this;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }


    public Criteria limit(int limit) {
        limitStart = 0;
        limitEnd = limit;

        return this;
    }

    public Criteria limit(int start, int end) {
        limitStart = start;
        limitEnd = end;

        return this;
    }


    private void ensureOp() {
        if(mBuilder.length() == 0) {
            return;
        }

        if(mNextOp == null) {
            mBuilder.append(AND);
        } else {
            mBuilder.append(mNextOp);
            mNextOp = null;
        }
    }

    public String getSQLString() {

        String groupByStr = "";
        if(groupBy != null)
            groupByStr = String.format("group by %s", getGroupBy());

        String orderByStr = "";
        if(orderBy != null)
            orderByStr = String.format("order by %s", getOrderBy());

        String whereStr = "";
        if(!mBuilder.toString().isEmpty())
            whereStr = String.format("where %s", mBuilder.toString());


        return String.format("%s %s %s", whereStr, groupByStr, orderByStr);
    }

    @Override
    public String toString() {
        return mBuilder.toString();
    }
}
package com.tecpie.platform.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 农历计算工具类
 *
 * @author Tanzj
 * @date 2021/8/17 19:06
 */
public class LunarCalendar {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        today.setTime(chineseDateFormat.parse("2022-04-20"));
        LunarCalendar lunar = new LunarCalendar(today);
        System.out.println("大写年份："+ lunar.getCapitalYear());
        System.out.println("数字农历日期："+ lunar.result());
        System.out.println("完整农历日期："+ lunar.toString());
        System.out.println("农历一年的总天数："+ LunarCalendar.yearDays(today.get(Calendar.YEAR)));
        System.out.println("传回农历y年闰月的天数："+ LunarCalendar.leapDays(today.get(Calendar.YEAR)));
        System.out.println("传回农历y年闰月1-12, 非闰年为0："+ LunarCalendar.leapMonth(today.get(Calendar.YEAR)));
        System.out.println("农历每月的1、10日等转为初一、初十等："+ LunarCalendar.getChinaDayString(20));
        System.out.println("干支年："+ lunar.cyclical());
        System.out.println("生肖：" + lunar.animalsYear());
        System.out.println("农历干支信息："+ lunar.getGanZhi());
        System.out.println("干支年："+ lunar.getYearGanZhi() + "年");
        System.out.println("干支月："+ lunar.getMonthGanZhi() + "月");
        System.out.println("干支日："+ lunar.getDayGanZhi() + "日");
        System.out.println("星期数字："+ lunar.getWeek(today));
        System.out.println("星期中文："+ lunar.weekConvert(today));
        System.out.println("农历月和日："+ lunar.getNl());
        System.out.println("农历月份："+ lunar.getNlMonth());
        System.out.println("农历日："+ lunar.getNlDay());
        System.out.println("是否是闰年："+ lunar.judgeYear(2019));
    }

    private int year;

    private int month;

    private int day;

    private boolean leap;
    /**
     * 对于年月日的天干地支
     */
    private int yearGz;
    private int monthGz;
    private int dayGz;

    final static String[] CHINESE_NUMBER = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    final static String[] CAPITAL_YEAR = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

    final static String[] ANIMALS = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    /**
     * 甲、乙、丙、丁、戊、己、庚、辛、壬、癸
     */
    final static String[] GAN = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};

    /**
     * 子、丑、寅、卯、辰、巳、午、未、申、酉、戌、亥
     */
    final static String[] ZHI = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    final static long[] LUNAR_INFO = new long[]{0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
            0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
            0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
            0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
            0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
            0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};

    /**
     * 传回农历 y年的总天数
     */
    private static int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[y - 1900] & i) != 0) {
                sum += 1;
            }
        }
        return (sum + leapDays(y));
    }

    /**
     * 传回农历 y年闰月的天数
     */
    private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((LUNAR_INFO[y - 1900] & 0x10000) != 0) {
                return 30;
            } else {
                return 29;
            }
        } else {
            return 0;
        }
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     */
    private static int leapMonth(int y) {
        return (int) (LUNAR_INFO[y - 1900] & 0xf);
    }

    /**
     * 传回农历 y年m月的总天数
     */
    private static int monthDays(int y, int m) {
        if ((LUNAR_INFO[y - 1900] & (0x10000 >> m)) == 0) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 大写年份
     */
    final public String getCapitalYear() {
        String curYear = String.valueOf(year);
        char[] yearChar = curYear.toCharArray();
        StringBuilder capitalYearStr = new StringBuilder();
        for (char yearNum : yearChar) {
            String yearIdx = String.valueOf(yearNum);
            capitalYearStr.append(CAPITAL_YEAR[Integer.parseInt(yearIdx)]);
        }
        return capitalYearStr.toString();
    }

    /**
     * 传回农历 y年的生肖
     */
    final public String animalsYear() {
        return ANIMALS[(year - 4) % 12];
    }

    /**
     * 干支纪年法
     */
    public static String cyclical(int realYear) {
        return (GAN[realYear % 10] + ZHI[realYear % 12]);
    }

    /**
     * 传入 offset 传回干支, 0=甲子
     */
    final public String cyclical() {
        int num = year - 1900 + 36;
        return (cyclical(num));
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数 * dayCyl5:与1900年1月31日相差的天数,再加40 ?
     */
    public LunarCalendar(Calendar cal) {
        // 初始化天地干支
        initGanZhi(cal);
        int leapMonth;
        Date baseDate = null;
        try {
            SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            baseDate = chineseDateFormat.parse("1900-1-31");
        } catch (ParseException e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }
        // 求出和1900年1月31日相差的天数
        assert baseDate != null;
        int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        // 农历年份
        year = iYear;
        // 闰哪个月,1-12
        leapMonth = leapMonth(iYear);
        leap = false;
        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else {
                daysOfMonth = monthDays(year, iMonth);
            }
            offset -= daysOfMonth;
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1)) {
                leap = false;
            }
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }
        month = iMonth;
        day = offset + 1;
    }

    /**
     * 初始化年月日对应的天干地支
     */
    private void initGanZhi(Calendar calendarNow) {
        long dateNow = calendarNow.getTime().getTime();
        //获取1900-01-31的时间
        Calendar calendarAgo = Calendar.getInstance();
        calendarAgo.set(1900, Calendar.JANUARY, 31);
        long dateAgo = calendarAgo.getTime().getTime();
        //86400000 = 24 * 60 * 60 * 1000
        long daysDistance = (dateNow - dateAgo) / 86400000L;
        float remainder = (dateNow - dateAgo) % 86400000L;
        //余数大于0算一天
        if (remainder > 0) {
            daysDistance += 1;
        }
        //都是从甲子开始算起以1900-01-31为起点
        //1899-12-21是农历1899年腊月甲子日  40：相差1900-01-31有40天
        dayGz = (int) daysDistance + 40;
        //1898-10-01是农历甲子月  14：相差1900-01-31有14个月
        monthGz = 14;
        int daysOfYear = 0;
        int i;
        for (i = 1900; i < 2050 && daysDistance > 0; i++) {
            daysOfYear = yearDays(i);
            daysDistance -= daysOfYear;
            monthGz += 12;
        }
        if (daysDistance < 0) {
            daysDistance += daysOfYear;
            i--;
            monthGz -= 12;
        }
        //农历年份
        int myYear = i;
        //1864年是甲子年
        yearGz = myYear - 1864;
        //哪个月是闰月
        long leap = LUNAR_INFO[myYear - 1900] & 0xf;
        boolean isLeap = false;
        int daysOfLeapMonth;
        for (i = 1; i < 13 && daysDistance > 0; i++) {
            //闰月
            if (leap > 0 && i == (leap + 1) && !isLeap) {
                isLeap = true;
                if ((LUNAR_INFO[myYear - 1900] & 0xf) != 0) {
                    daysOfLeapMonth = (LUNAR_INFO[myYear - 1900] & 0x10000) == 0 ? 29 : 30;
                } else {
                    daysOfLeapMonth = 0;
                }
                --i;
            } else {
                daysOfLeapMonth = (LUNAR_INFO[myYear - 1900] & (0x10000 >> i)) == 0 ? 29 : 30;
            }
            //设置非闰月
            if (isLeap && i == (leap + 1)) {
                isLeap = false;
            }
            daysDistance -= daysOfLeapMonth;
            if (!isLeap) {
                monthGz++;
            }
        }
        if (daysDistance == 0 && leap > 0 && i == leap + 1 && !isLeap) {
            --monthGz;
        }
        if (daysDistance < 0) {
            --monthGz;
        }
    }

    /**
     * 将年月日转化为天干地支的显示方法
     */
    public String ganZhi(int index) {
        return GAN[index % 10] + ZHI[index % 12];
    }

    /**
     * 获取天干地支
     */
    public String getGanZhi() {
        return "农历" + ganZhi(yearGz) + "年 " + ganZhi(monthGz) + "月 " + ganZhi(dayGz) + "日";
    }

    /**
     * 获取【年】天干地支
     */
    public String getYearGanZhi() {
        return ganZhi(yearGz);
    }

    /**
     * 获取【月】天干地支
     */
    public String getMonthGanZhi() {
        return ganZhi(monthGz);
    }

    /**
     * 获取【日】天干地支
     */
    public String getDayGanZhi() {
        return ganZhi(dayGz);
    }

    /**
     * 格式化农历每月的1、10、20、30日为初一、初十、廿十、卅十
     */
    public static String getChinaDayString(int day) {
        String[] chineseTen = {"初", "十", "廿", "卅"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30) {
            return "";
        }
        String chinaDayString;
        if (day == 10) {
            chinaDayString = "初十";
        } else {
            chinaDayString = chineseTen[day / 10] + CHINESE_NUMBER[n];
        }
        if("卅十".equals(chinaDayString)){
            chinaDayString = chinaDayString.replace("十", "");
        }
        return chinaDayString;
    }

    @Override
    public String toString() {
        return cyclical() + "年" + (leap ? "闰" : "") + CHINESE_NUMBER[month - 1] + "月" + getChinaDayString(day);
    }

    /**
     * 不包含干支年
     */
    public String getNl() {
        return (leap ? "闰" : "") + CHINESE_NUMBER[month - 1] + "月" + getChinaDayString(day);
    }

    /**
     * 获取农历月
     */
    public String getNlMonth() {
        return (leap ? "闰" : "") + CHINESE_NUMBER[month - 1] + "月";
    }

    /**
     * 获取农历月
     */
    public String getNlDay() {
        return getChinaDayString(day);
    }

    /**
     * 获取农历年月日
     */
    public String result() {
        String m = (month < 10) ? "0" + month : month + "";
        String d = (day < 10) ? "0" + day : day + "";
        return year + "-" + m + "-" + d;
    }

    /**
     * 判断是否是闰年
     */
    public boolean judgeYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if ((year % 4 == 0) && (year % 100 != 0)) {
            return true;
        }
        return false;
    }

    /**
     * 获取星期
     */
    public int getWeek(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int week = -1;
        int[] monthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isLeapYear;
        int sumDay = 0;
        if (year >= 1900) {
            for (int i = 1900; i < year; i++) {
                if (judgeYear(i)) {
                    sumDay += 366;
                } else {
                    sumDay += 365;
                }
            }
            for (int i = 0; i < month - 1; i++) {
                sumDay += monthDay[i];
            }
            if (month > 2) {
                isLeapYear = judgeYear(year);
                if (isLeapYear) {
                    sumDay++;
                }
            }
            sumDay += day;

            week = sumDay % 7;
        }
        return week;
    }

    public String weekConvert(Calendar cal) {
        String[] weekName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekName[getWeek(cal)];
    }

}

<?php

namespace App\Helpers;

use Carbon\Carbon;

class DateHelper
{
    /**
     * Get all dates between given months_ago starting from now
     *
     * @param int $months_ago
     *
     * @return Carbon
     */

    public static function getSomeMonthsAgoFromNow(int $months_ago): Carbon
    {
        return now()->startOfMonth()->subMonths($months_ago);
    }

    /**
     * Get all dates between given weeks_ago starting from now
     *
     * @param int $weeks_ago
     *
     * @return Carbon
     */

    public static function getSomeWeeksAgoFromNow(int $weeks_ago): Carbon
    {
        return now()->startOfWeek()->subWeek($weeks_ago);
    }

    /**
     * Get all weeks in current month starting from now
     *
     *
     * @return Carbon
     */

    public static function getWeeksInThisMonth(): Carbon
    {
        return now()->startOfMonth();
    }

    /**
     * Get all months in current years starting from now
     *
     *
     * @return Carbon
     */

    public static function getMonthsInThisYear(): Carbon
    {
        return now()->startOfYear();
    }

    /**
     * Get all dates between given days_ago starting from now
     *
     * @param int $days_ago
     *
     * @return Carbon
     */

    public static function getSomeDaysAgoFromNow(int $days_ago): Carbon
    {
        return now()->startOfDay()->subDays($days_ago);
    }

    /**
     * Get all dates between given days_ago starting from now
     *
     * @param int $hours_ago
     *
     * @return Carbon
     */

    public static function getSomeHoursAgoFromNow(int $hours_ago): Carbon
    {
        return now()->startOfHour()->subHours($hours_ago);
    }

    /**
     * Get all dates between given years_ago starting from now
     *
     * @param int $years_ago
     *
     * @return Carbon
     */

    public static function getSomeYearsAgoFromNow(int $years_ago): Carbon
    {
        return now()->startOfYear()->subYears($years_ago);
    }

    /**
     * Get the last month & date in current year
     *
     * @return Carbon
     */

    public static function getLastMonthOfYear(): Carbon
    {
        return now()->endOfYear();
    }

    /**
     * Get current timestamp
     *
     * @param string $format
     *
     * @return mixed
     */

    public static function getCurrentTimestamp(string $format): mixed
    {
        return now()->format($format);
    }

    /**
     * Parse given date to formatted datetime
     *
     * @param mixed $data
     * @param mixed $format
     *
     * @return mixed
     */

    public static function parseDate(mixed $data, mixed $format): mixed
    {
        return Carbon::parse($data)->translatedFormat($format);
    }

    /**
     * Get all 12 months in this year
     *
     * @param int $month
     * @return array
     */

    public static function getAllMonths(int $month): array
    {
        $period = now()->subMonths($month)->monthsUntil(now());

        $data = [];
        foreach ($period as $date) {
            $data[] = $date->shortMonthName . ' ' . $date->year;
        }

        return $data;
    }
}

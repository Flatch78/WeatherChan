package com.guillaumek.weatherchannel.Network;

import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.R;

/**
 * Created by flatch on 07/11/15.
 */
public class NetworkAPIWeather {
    private final static String APIKEY = AppWeatherChan.getAppContext().getString(R.string.OpenWeatherMapKey);
    private final static String WEBSITEAPI = "api.openweathermap.org";
    private final static String WEBSITEUV = "api.owm.io";
    private final static String WEBSITE = "openweathermap.org";
    private final static String PREFIXAPI = "/data/2.5";
    private final static String PREFIXUV = "/air/1.0/uvi";
    private final static String WEATHER = "/weather";
    private final static String FORECAST = "/forecast";
    private final static String CURRENTUV = "/current";
    private final static String HISTUV = "/current";
    private final static String PREFIXIMG = "/img/w";
    private final static String SUN = "https://c1.staticflickr.com/3/2206/2967081141_f8ecd7faa8_z.jpg";
    private final static String SUN_N = "https://c2.staticflickr.com/4/3323/3250177675_aec25e89e4_z.jpg";
    private final static String FEW_CLOUDS = "https://c1.staticflickr.com/9/8393/8624171642_ab124176e3_z.jpg";
    private final static String FEW_CLOUDS_N = "https://c2.staticflickr.com/6/5021/5729512563_0fc8fed9e1_b.jpg";
    private final static String CLOUDS = "https://c2.staticflickr.com/8/7343/9074032569_b2c1013e2b_z.jpg";
    private final static String CLOUDS_N = "https://c2.staticflickr.com/8/7296/11988885683_c927002abb_c.jpg";
    private final static String BROKEN_CLOUDS = "https://c1.staticflickr.com/5/4129/5059890281_6eacf5c027_z.jpg";
    private final static String BROKEN_CLOUDS_N = "https://c1.staticflickr.com/3/2903/14643318567_933f41b5f7_z.jpg";
    private final static String RAIN = "https://c1.staticflickr.com/9/8545/8685843210_19404d3eae_z.jpg";
    private final static String RAIN_N = "https://c2.staticflickr.com/6/5328/8812928672_ca99730f13_h.jpg";
    private final static String THUNDERSTORM = "https://c1.staticflickr.com/3/2865/9083913090_3907d0ac20_c.jpg";
    private final static String THUNDERSTORM_N = "https://c1.staticflickr.com/9/8226/8556078999_226b320742_c.jpg";
    private final static String SNOW = "https://c2.staticflickr.com/8/7455/11721144654_55dfcd8231_z.jpg";
    // https://c2.staticflickr.com/6/5289/5238720254_46f90ce1e7.jpg
    private final static String SNOW_N = "https://c1.staticflickr.com/9/8683/16531118411_15c2332398_z.jpg";
    // https://c2.staticflickr.com/8/7310/9239234552_dec91f2960_z.jpg
    private final static String FOG = "https://c2.staticflickr.com/6/5684/20597103963_57f862461e_z.jpg";
    private final static String FOG_N = "https://c1.staticflickr.com/9/8682/16069627989_a766afefe3_z.jpg";


    /* PUBLIC */

    // Weather
    static public String getURLWeatherCoord(double lat, double lon) {
        return addAPIKEY(getPrefixApiWeather() + getLatAndLong(lat, lon));
    }
    static public String getURLWeatherNameCity(String cityName) {
        return addAPIKEY(getPrefixApiWeather() + "q=" + cityName);
    }
    // Forecast
    static public String getURLForecastCoord(double lat, double lon) {
        return addAPIKEY(getPrefixApiForecast() + getLatAndLong(lat, lon));
    }
    static public String getURLForecastNameCity(String cityName) {
        return addAPIKEY(getPrefixApiForecast() + "q=" + cityName);
    }
    // UV
    static public String getURLCurrentUVCoord(double lat, double lon) {
        return addAPIKEY(getPrefixApiCurrentUV() + getLatAndLong(lat, lon));
    }
    static public String getURLHistUVCoord(double lat, double lon) {
        return addAPIKEY(getPrefixApiHistUV() + getLatAndLong(lat, lon));
    }

    // Icon
    static public String getURLWeatherIcon(String icon) {
        return getPrefixImg() + icon + ".png";
    }

    // Pictures
    static public String getURLSun() {
        return SUN;
    }
    static public String getURLSunNight() {
        return SUN_N;
    }
    static public String getURLFewClouds() {
        return FEW_CLOUDS;
    }
    static public String getURLFewCloudsNight() {
        return FEW_CLOUDS_N;
    }
    static public String getURLClouds() {
        return CLOUDS;
    }
    static public String getURLCloudsNight() {
        return CLOUDS_N;
    }
    static public String getURLBrokenClouds() {
        return BROKEN_CLOUDS;
    }
    static public String getURLBrokenCloudsNight() {
        return BROKEN_CLOUDS_N;
    }
    static public String getURLRain() {
        return RAIN;
    }
    static public String getURLRainNight() {
        return RAIN_N;
    }
    static public String getURLThunderstorm() {
        return THUNDERSTORM;
    }
    static public String getURLThunderstormNight() {
        return THUNDERSTORM_N;
    }
    static public String getURLSnow() {
        return SNOW;
    }
    static public String getURLSnowNight() {
        return SNOW_N;
    }
    static public String getURLFog() {
        return FOG;
    }
    static public String getURLFogNight() {
        return FOG_N;
    }


    static public String getURLbyIconName(String iconName) {
        switch (iconName) {
            case "01d": return getURLSun();
            case "01n": return getURLSunNight();
            case "02d": return getURLFewClouds();
            case "02n": return getURLFewCloudsNight();
            case "03d": return getURLClouds();
            case "03n": return getURLCloudsNight();
            case "04d": return getURLBrokenClouds();
            case "04n": return getURLBrokenCloudsNight();
            case "09d": return getURLRain();
            case "09n": return getURLRainNight();
            case "10d": return getURLRain();
            case "10n": return getURLRainNight();
            case "11d": return getURLThunderstorm();
            case "11n": return getURLThunderstormNight();
            case "13d": return getURLSnow();
            case "13n": return getURLSnowNight();
            case "50d": return getURLFog();
            case "50n": return getURLFogNight();
            default:
                return null;
        }
    }

    /* PRIVATE */

    // WEATHER
    static private String getPrefixApiWeather() {
        return "http://" + WEBSITEAPI + PREFIXAPI + WEATHER + "?";
    }
    // FORECAST
    static private String getPrefixApiForecast() {
        return "http://" + WEBSITEAPI + PREFIXAPI + FORECAST + "?";
    }
    // UV
    static private String getPrefixApiCurrentUV() {
        return "http://" + WEBSITEUV + PREFIXUV + CURRENTUV + "?";
    }
    static private String getPrefixApiHistUV() {
        return "http://" + WEBSITEUV + PREFIXUV + HISTUV + "?";
    }
    // ICON
    static private String getPrefixImg() {
        return "http://" + WEBSITE + PREFIXIMG + "/";
    }

    static private String addAPIKEY(String URL) {
        return URL + "&" + "APPID=" + APIKEY;
    }

    static private String getLatAndLong(double lat, double lon) {
        return String.format("lat=%f&lon=%f", lat, lon);
    }

}

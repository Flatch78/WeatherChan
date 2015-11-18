package com.guillaumek.weatherchannel.Network;

import com.guillaumek.weatherchannel.Network.Object.MainObject;
import com.guillaumek.weatherchannel.Network.Object.WeatherObject;

import java.util.List;

/**
 * Created by flatch on 08/11/15.
 */
public class ForecastWeather {

    public class Coord {
        public double lon;
        public double lat;
    }

    public class Sys {
        public int population;
    }

    public class City {
        public int id;
        public String name;
        public Coord coord;
        public String country;
        public int population;
        public Sys sys;
    }

    public class Clouds {
        public int all;
    }

    public class Wind {
        public double speed;
        public double deg;
    }

    public class Rain {
        public double __invalid_name__3h;
    }

    public class Sys2 {
        public String pod;
    }

    public class FtWeather {
        public int dt;
        public MainObject main;
        public List<WeatherObject> weather;
        public Clouds clouds;
        public Wind wind;
        public Rain rain;
        public Sys2 sys;
        public String dt_txt;
    }

    public City city;
    public String cod;
    public double message;
    public int cnt;
    public List<FtWeather> list;
}

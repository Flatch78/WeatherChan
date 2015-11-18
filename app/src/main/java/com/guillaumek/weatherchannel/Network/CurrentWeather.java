package com.guillaumek.weatherchannel.Network;

import com.guillaumek.weatherchannel.Network.Object.MainObject;
import com.guillaumek.weatherchannel.Network.Object.WeatherObject;

import java.util.List;

/**
 * Created by flatch on 07/11/15.
 */
public class CurrentWeather {


    public class Coord {
        public double lon;
        public double lat;
    }


    public class Wind {
        public double speed;
        public double deg;
    }

    public class Rain {
        public double __invalid_name__3h;
    }

    public class Clouds {
        public int all;
    }

    public class Sys {
        public double message;
        public String country;
        public int sunrise;
        public int sunset;
    }

    public Coord coord;
    public List<WeatherObject> weather;
    public String base;
    public MainObject main;
    public Wind wind;
    public Rain rain;
    public Clouds clouds;
    public int dt;
    public Sys sys;
    public int id;
    public String name;
    public int cod;
}

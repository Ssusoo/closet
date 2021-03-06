package com.ssu.closet.service.weather;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssu.closet.domain.region.Region;
import com.ssu.closet.domain.region.RegionRepository;
import com.ssu.closet.domain.weather.Weather;
import com.ssu.closet.domain.weather.WeatherRepository;
import com.ssu.closet.dto.weather.WeatherListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    final int API_TYPE_WEATHER = 1;
    final int API_TYPE_TEMPERATURE = 2;

    private final RestTemplateBuilder restTemplateBuilder;
    private final RegionRepository regionRepository;
    private final WeatherRepository weatherRepository;


    /**
     * Get Weather(), Get Temperature()
     * Save Temp Date(), save Real Data()
     */
    @PostConstruct
    @Transactional
    public void Init() {
        insertRegion();

        String regionId = "";   // location API
        List<Region> regions = regionRepository.findAll();
//        Region region = regionRepository.findByRegionIdLike(regionId);

        for (Region region : regions) {
            try {
                JsonParser jp = new JsonParser();

                String weather_result = callWeatherApi(API_TYPE_WEATHER, region.getRegionWeatherId());
                JsonObject weather_json = jp.parse(weather_result)
                        .getAsJsonObject()
                        .getAsJsonObject("response")
                        .getAsJsonObject("body")
                        .getAsJsonObject("items")
                        .getAsJsonArray("item")
                        .get(0)
                        .getAsJsonObject();

                String temperature_result = callWeatherApi(API_TYPE_TEMPERATURE, region.getRegionTemperatureId());
                JsonObject temperature_json = jp.parse(temperature_result)
                        .getAsJsonObject()
                        .getAsJsonObject("response")
                        .getAsJsonObject("body")
                        .getAsJsonObject("items")
                        .getAsJsonArray("item")
                        .get(0)
                        .getAsJsonObject();

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        String wt = "";
                        String temp = "";
                        if (i == 0) {
                            wt = "??????";
                            temp = "15";
                        } else if (i == 1) {
                            wt = "??????";
                            temp = "12";
                        } else if (i == 2) {
                            wt = "???";
                            temp = "13";
                        }

                        String meridiem = "";
                        if (j == 0) {
                            meridiem = "AM";
                        } else {
                            meridiem = "PM";
                        }
                        Weather weather = Weather.builder()
                                .baseDate(getAddDate(i))
                                .city(region.getCity())
                                .meridiem(meridiem)
                                .localWeather(wt)
                                .temperature(Long.parseLong(temp))
                                .build();

                        weatherRepository.save(weather);
                    }
                }

                for (int i = 3; i < 11; i++) {
                    for (int j = 0; j < 2; j++) {
                        // set weather key
                        String meridiem = "";
                        String weather_key = "wf" + i;
                        if (j == 0) {
                            meridiem = "Am";
                            if (i < 8) {
                                weather_key += meridiem;
                            }
                        } else {
                            meridiem = "Pm";
                            if (i < 8) {
                                weather_key += meridiem;
                            }
                        }
                        meridiem = meridiem.toUpperCase();

                        String temperature_key = "taMax" + i;

                        Weather weather = Weather.builder()
                                .baseDate(getAddDate(i))
                                .city(region.getCity())
                                .meridiem(meridiem)
                                .localWeather(weather_json.get(weather_key).getAsString())
                                .temperature(temperature_json.get(temperature_key).getAsLong())
                                .build();

                        weatherRepository.save(weather);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAddDate(int day) {
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        date = c.getTime();

        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");

        return format.format(date);
    }

    public String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd:HHmm");

        Date date = new Date();

        return format.format(date);
    }

    public String getMeridiem(String currentDate) {
        if (Integer.parseInt(currentDate.split(":")[1]) >= 0000
                && Integer.parseInt(currentDate.split(":")[1]) < 1200) {
            return "am";
        } else {
            return "pm";
        }
    }

    public String getBatchTime(String currentDate) {
        if (Integer.parseInt(currentDate.split(":")[1]) >= 0600
                && Integer.parseInt(currentDate.split(":")[1]) < 1800) {
            return currentDate.split(":")[0] + "0600";
        } else if (Integer.parseInt(currentDate.split(":")[1]) >= 1800
                && Integer.parseInt(currentDate.split(":")[1]) < 2400) {
            return currentDate.split(":")[0] + "1800";
        } else {
            return getAddDate(-1) + "1800";
        }
    }

    private void insertRegion() {
        regionRepository.saveAll(List.of(
                Region.builder().city("??????_??????_?????????").regionTemperatureId("11B10101").regionWeatherId("11B00000").build(),
                Region.builder().city("??????_??????_????????????").regionTemperatureId("11H20201").regionWeatherId("11D10000").build(),
                Region.builder().city("??????_????????????").regionTemperatureId("11H10701").regionWeatherId("11D20000").build(),
                Region.builder().city("??????_????????????").regionTemperatureId("11F20501").regionWeatherId("11C20000").build(),
                Region.builder().city("????????????").regionTemperatureId("11F10201").regionWeatherId("11F20000").build(),
                Region.builder().city("??????_??????_????????????").regionTemperatureId("11C20401").regionWeatherId("11F10000").build(),
                Region.builder().city("????????????").regionTemperatureId("11C10301").regionWeatherId("11H10000").build(),
                Region.builder().city("?????????").regionTemperatureId("11D10301").regionWeatherId("11H20000").build(),
                Region.builder().city("?????????").regionTemperatureId("11G00201").regionWeatherId("11G00000").build())
        );
    }

    private String callWeatherApi(int apiType, String regionId) throws Exception {
        String api_url = "";
        if (apiType == API_TYPE_WEATHER) {
            // ??????
            api_url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        } else {
            // ??????
            api_url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
        }

        StringBuilder urlBuilder = new StringBuilder(api_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("/tpu/YUap+AKJVZ8wBRmoDcQjhE1QN01m5tAt0uNquuFM7au5V4OsdaTslntZyw1hpcWY2kCq3G973tnedGbuw==", "UTF-8")); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*???????????????*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*??? ????????? ?????? ???*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*??????????????????(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(regionId, "UTF-8")); /*?????? ???????????? ??????*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(getBatchTime(getCurrentDate()), "UTF-8")); /*-??? 2???(06:00,18:00)??? ?????? ?????? ??????????????? ??????-?????? 24?????? ????????? ??????*/

        System.out.println(urlBuilder.toString());
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        return sb.toString();
    }

    @Transactional
    public List<WeatherListResponse> findAllDesc() {
        return weatherRepository.findAllDesc().stream()
                .map(WeatherListResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public List<WeatherListResponse> findByWeatherCity( String weatherCity) {
        return weatherRepository.findByWeatherCityAndBaseDateAndMeridiemOrderByBaseDateDesc(
                weatherCity, getMeridiem(getCurrentDate()).toUpperCase(),
                getCurrentDate().split(":")[0], getAddDate(10)
        )
                .stream()
                .map(WeatherListResponse::new)
                .collect(Collectors.toList());
    }

    public List<Weather> findCurrentLocalWeather(String city){
        return weatherRepository.findByCurrentLocalWeather(city);
    }
    public List<Weather> findCurrentDateTemperature(String currentdate, String city, String meridien) {
        return weatherRepository.findCurrentDateTemperature(currentdate, city, meridien);
    }
}
package com.app.gest.immo.config;

import jakarta.servlet.http.HttpServletRequest;

public class tools {

    public static  String GetIp(HttpServletRequest request){
            String Ip = request.getHeader("X-Forwarded-For");
            if (Ip == null) {
                Ip = request.getHeader("Proxy-Client-IP");
            }
            if (Ip == null) {
                Ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (Ip != null && !Ip.isEmpty() && !"unknown".equalsIgnoreCase(Ip)) {
                // X-Forwarded-For peut contenir plusieurs IPs : on prend la première
                return Ip.split(",")[0].trim();
            }

            Ip = request.getHeader("X-Real-IP");
            if (Ip != null && !Ip.isEmpty() && !"unknown".equalsIgnoreCase(Ip)) {
                return Ip;
            }

            return request.getRemoteAddr(); // sinon, IP directe


    }

}

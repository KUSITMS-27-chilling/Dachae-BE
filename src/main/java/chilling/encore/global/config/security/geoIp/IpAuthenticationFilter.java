package chilling.encore.global.config.security.geoIp;

import chilling.encore.global.config.slack.SlackMessage;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class IpAuthenticationFilter implements Filter {
    private final DatabaseReader databaseReader;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = HttpRequestUtils.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = null;
        log.info("attempt ip : {}", ipAddress);
        try {
            country = databaseReader.country(inetAddress).getCountry().getName();
        } catch (GeoIp2Exception e) {
        }
        if (checkIp((HttpServletRequest) request, (HttpServletResponse) response, ipAddress, country))
            return;
        chain.doFilter(request, response);
    }

    private static boolean checkIp(HttpServletRequest request, HttpServletResponse response, String ipAddress, String country) {
        if (country == null || !country.equals("South Korea")) {
            HttpServletResponse httpServletResponse = response;
            httpServletResponse.setStatus(HttpStatus.SC_BAD_GATEWAY);
            HttpServletRequest httpServletRequest = request;
            log.warn("Access Reject : {}, {}, {}", ipAddress, country, httpServletRequest.getRequestURI());
            return true;
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("IP Authentication Filter Init..");
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter Destroy..");
    }
}

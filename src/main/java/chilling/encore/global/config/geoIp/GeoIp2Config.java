package chilling.encore.global.config.geoIp;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeoIp2Config {
    @Value("${geoip2.database.path}")
    private String path;

    @Bean
    public DatabaseReader databaseReader() throws IOException{
        File resource = new File(path);
        return new DatabaseReader.Builder(resource).build();
    }
}
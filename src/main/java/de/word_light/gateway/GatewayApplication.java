package de.word_light.gateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.word_light.gateway.config.ApplicationInitializer;


@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {

        new ApplicationInitializer(args).init();
		SpringApplication.run(GatewayApplication.class, args);
	}

            
    /**
     * @return the version of this api from {@code build.gradle} or and empty String if 'version' prop is not found
     * 
     * @since 0.0.9
     */
    public static String getApiVersion() {

        String fileName = "build.gradle";
        String propName = "version";

        try (InputStream in = new FileInputStream(fileName)) {
            Properties props = new Properties();
            props.load(in);
            Object versionProp = props.get(propName);

            String version = versionProp != null ? versionProp.toString().replace("'", "") 
                                                 : "Failed to get version. Could not find '" + propName + "' attribute in '" + fileName + "' file.";

            return version;

        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to get version. Could not find file '" + fileName + "'";
        }
    }
}
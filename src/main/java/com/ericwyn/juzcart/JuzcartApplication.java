package com.ericwyn.juzcart;

import com.ericwyn.juzcar.utils.PackageUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class JuzcartApplication {

    public static void main(String[] args) {
        SpringApplication.run(JuzcartApplication.class, args);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Class> juzcarClasses = PackageUtils.scannerAllController(JuzcartApplication.class);
                System.out.println(juzcarClasses.size());
            }
        }).run();
    }
}

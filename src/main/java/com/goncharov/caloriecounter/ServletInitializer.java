package com.goncharov.caloriecounter;

import com.goncharov.caloriecounter.CalorieCounterApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CalorieCounterApplication.class);
	}

}

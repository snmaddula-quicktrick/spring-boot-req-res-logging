package snmaddula.logging.poc;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author snmaddula
 *
 */
@Configuration
public class LoggingConfig {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingConfig.class);

	class RequestLog {
		private StringBuilder logBuilder;

		public RequestLog(HttpServletRequest request) {
			logBuilder = new StringBuilder("REQUEST  : ").append("METHOD=" + request.getMethod()).append(", ")
					.append("URI=").append(request.getRequestURI()).append(", ").append("PARAMS=" + params(request));
		}

		private Map<String, String> params(HttpServletRequest request) {
			Map<String, String> params = new LinkedHashMap<>();
			request.getParameterMap().entrySet().forEach(e -> {
				params.put(e.getKey(), Arrays.toString(e.getValue()));
			});
			return params;
		}

		public String toString() {
			return logBuilder.toString();
		}
	}

	class ResponseLog {
		private StringBuilder logBuilder;

		public ResponseLog(HttpServletResponse response) {
			logBuilder = new StringBuilder("RESPONSE : ").append("STATUS=").append(HttpStatus.valueOf(response.getStatus())).append(", ")
					.append("HEADERS=").append(headers(response));
		}

		private Map<String, String> headers(HttpServletResponse response) {
			Map<String, String> headers = new LinkedHashMap<>();
			response.getHeaderNames().forEach(header -> {
				headers.put(header, response.getHeader(header));
			});
			return headers;
		}

		public String toString() {
			return logBuilder.toString();
		}
	}

	@Bean
	@SuppressWarnings("all")
	public FilterRegistrationBean<?> filterRegistrationBean() {
		return new FilterRegistrationBean() {{
			setFilter((req, res, chain) -> {
				HttpServletRequest request = (HttpServletRequest) req;
				
				if ("/favicon.ico".equals(request.getRequestURI())) return;

				HttpServletResponse response = (HttpServletResponse) res;
				LOG.info("{}", new RequestLog(request));
				chain.doFilter(request, response);
				LOG.info("{}", new ResponseLog(response));
			});
		}};
	}
}

package cpt202.groupwork.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import cpt202.groupwork.Response;

/**
 * @className: ErrorAuthenticationEntryPoint
 * @description: return "401 unauthorized" when user access resource that beyond permission
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Component
public class ErrorAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 5200068540912465653L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // set json format to return
        response.setContentType("application/json;charset=UTF-8");
        // set HTTP status code 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // PrintWriter print Response information
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        Response<?> myResponse = Response.fail();
        writer.write(mapper.writeValueAsString(myResponse));
    }
}
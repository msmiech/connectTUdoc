package at.connectTUdoc.backend.config.security;

import com.google.api.client.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is a possibility to filter an Firebase Authentication Token
 */
public class FirebaseAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "X-Firebase-Auth";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authToken = request.getHeader(TOKEN_HEADER);

        if (Strings.isNullOrEmpty(authToken)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = new FirebaseAuthenticationToken(null, authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}

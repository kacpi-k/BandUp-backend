package dev.kkoncki.bandup.web.socket;

import dev.kkoncki.bandup.security.JwtAuthUser;
import dev.kkoncki.bandup.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            List<String> authorization = accessor.getNativeHeader("Authorization");
            String token = null;

            if (authorization != null && !authorization.isEmpty()) {
                String bearerToken = authorization.get(0);

                if (bearerToken.startsWith("Bearer ")) {
                    token = bearerToken.substring(7);
                }
            }

            if (token == null) {
                Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
                if (raw instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> connectHeaders = (Map<String, Object>) raw;
                    Object rawNativeHeaders = connectHeaders.get("nativeHeaders");
                    if (rawNativeHeaders instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> nativeHeaders = (Map<String, Object>) rawNativeHeaders;
                        Object queryParams = connectHeaders.get("queryParams");

                        if (queryParams instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, List<String>> params = (Map<String, List<String>>) queryParams;
                            if (params.containsKey("token")) {
                                List<String> tokenList = params.get("token");
                                if (!tokenList.isEmpty()) {
                                    token = tokenList.get(0);
                                }
                            }
                        }
                    }
                }
            }

            if (token != null) {
                try {
                    Claims claims = jwtUtil.resolveClaims(token);
                    if (claims != null && jwtUtil.validateClaims(claims)) {
                        String id = claims.getSubject();

                        JwtAuthUser userDetails = (JwtAuthUser) userDetailsService.loadUserByUsername(id);

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);

                    }
                } catch (Exception ignored) {
                }
            }
        }

        return message;
    }
}

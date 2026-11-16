package com.petshop.api;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> pedidoInvalido(IllegalArgumentException e) {
        String mensagem = e.getMessage() == null ? "Pedido inválido." : e.getMessage();
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> corpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("erro",
                "Pedido inválido: confira os campos (datas no formato aaaa-mm-dd e números válidos)."));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> erroComStatus(ResponseStatusException e) {
        String mensagem = e.getReason() == null ? "Erro." : e.getReason();
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", mensagem));
    }
}
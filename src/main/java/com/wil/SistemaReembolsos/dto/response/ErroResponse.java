package com.wil.SistemaReembolsos.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
    
    LocalDateTime timeStamp,
    Integer status,
    String erro,
    List<String> mensagem

) {

}

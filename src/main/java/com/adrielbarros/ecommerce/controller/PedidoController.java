package com.adrielbarros.ecommerce.controller;

import com.adrielbarros.ecommerce.dto.PedidoRequestDTO;
import com.adrielbarros.ecommerce.dto.PedidoResponseDTO;
import com.adrielbarros.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.criarPedido(userDetails.getUsername(), dto));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarMeusPedidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(pedidoService.listarMeusPedidos(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id, userDetails.getUsername()));
    }
}
package com.adrielbarros.ecommerce.service;

import com.adrielbarros.ecommerce.dto.ItemPedidoRequestDTO;
import com.adrielbarros.ecommerce.dto.PedidoRequestDTO;
import com.adrielbarros.ecommerce.dto.PedidoResponseDTO;
import com.adrielbarros.ecommerce.model.Pedido;
import com.adrielbarros.ecommerce.model.Produto;
import com.adrielbarros.ecommerce.model.StatusPedido;
import com.adrielbarros.ecommerce.model.Usuario;
import com.adrielbarros.ecommerce.repository.PedidoRepository;
import com.adrielbarros.ecommerce.repository.ProdutoRepository;
import com.adrielbarros.ecommerce.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuario;
    private Produto produto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Adriel Barros");
        usuario.setEmail("adriel@email.com");

        produto = new Produto(1L, "Mouse Gamer", "RGB", new BigDecimal("250.00"), 10, true);
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso e decrementar o estoque")
    void deveCriarPedidoComSucesso() {
        when(usuarioRepository.findByEmail("adriel@email.com")).thenReturn(Optional.of(usuario));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoRequestDTO dto = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(1L, 2)));

        PedidoResponseDTO resultado = pedidoService.criarPedido("adriel@email.com", dto);

        assertNotNull(resultado);
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, resultado.status());
        assertEquals(new BigDecimal("500.00"), resultado.valorTotal());
        assertEquals(8, produto.getEstoque(), "O estoque deve ter sido reduzido de 10 para 8");

        verify(produtoRepository, times(1)).save(produto);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção e não alterar estoque quando a quantidade for maior que a disponível")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        when(usuarioRepository.findByEmail("adriel@email.com")).thenReturn(Optional.of(usuario));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        PedidoRequestDTO dto = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(1L, 15)));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> pedidoService.criarPedido("adriel@email.com", dto)
        );

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        assertEquals(10, produto.getEstoque(), "O estoque não deve sofrer alteração após o erro");

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
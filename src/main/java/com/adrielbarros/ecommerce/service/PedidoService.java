package com.adrielbarros.ecommerce.service;

import com.adrielbarros.ecommerce.dto.ItemPedidoRequestDTO;
import com.adrielbarros.ecommerce.dto.PedidoRequestDTO;
import com.adrielbarros.ecommerce.dto.PedidoResponseDTO;
import com.adrielbarros.ecommerce.model.ItemPedido;
import com.adrielbarros.ecommerce.model.Pedido;
import com.adrielbarros.ecommerce.model.Produto;
import com.adrielbarros.ecommerce.model.Usuario;
import com.adrielbarros.ecommerce.repository.PedidoRepository;
import com.adrielbarros.ecommerce.repository.ProdutoRepository;
import com.adrielbarros.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(String emailUsuario, PedidoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Pedido pedido = new Pedido(usuario);

        for (ItemPedidoRequestDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto ID " + itemDto.produtoId() + " não encontrado."));

            if (!produto.getAtivo()) {
                throw new IllegalStateException("O produto '" + produto.getNome() + "' está inativo.");
            }

            if (produto.getEstoque() < itemDto.quantidade()) {
                throw new IllegalStateException("Estoque insuficiente para o produto '" + produto.getNome() +
                        "'. Disponível: " + produto.getEstoque() + ", Solicitado: " + itemDto.quantidade());
            }

            // Atualiza o estoque do produto
            produto.setEstoque(produto.getEstoque() - itemDto.quantidade());
            produtoRepository.save(produto);

            // Cria o item associado ao pedido
            ItemPedido itemPedido = new ItemPedido(pedido, produto, itemDto.quantidade(), produto.getPreco());
            pedido.adicionarItem(itemPedido);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    public List<PedidoResponseDTO> listarMeusPedidos(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        return pedidoRepository.findByUsuario(usuario)
                .stream()
                .map(PedidoResponseDTO::new)
                .toList();
    }

    public PedidoResponseDTO buscarPorId(Long id, String emailUsuario) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido ID " + id + " não encontrado."));

        if (!pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new SecurityException("Acesso negado: o pedido não pertence ao usuário logado.");
        }

        return new PedidoResponseDTO(pedido);
    }
}
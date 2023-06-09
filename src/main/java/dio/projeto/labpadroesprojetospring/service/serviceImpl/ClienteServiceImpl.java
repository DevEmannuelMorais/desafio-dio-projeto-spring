package dio.projeto.labpadroesprojetospring.service.serviceImpl;

import dio.projeto.labpadroesprojetospring.model.Cliente;
import dio.projeto.labpadroesprojetospring.model.Endereco;
import dio.projeto.labpadroesprojetospring.repository.ClienteRepository;
import dio.projeto.labpadroesprojetospring.repository.EnderecoRepository;
import dio.projeto.labpadroesprojetospring.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ViaCepService viaCepService;
    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe esse id"));
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);

    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            return viaCepService.consultaCep(cep);
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }


    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
}

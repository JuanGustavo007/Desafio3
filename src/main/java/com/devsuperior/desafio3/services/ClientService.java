package com.devsuperior.desafio3.services;

import com.devsuperior.desafio3.dto.ClientDto;
import com.devsuperior.desafio3.entities.Client;
import com.devsuperior.desafio3.repositories.ClientRepository;
import com.devsuperior.desafio3.services.exceptions.DatabaseException;
import com.devsuperior.desafio3.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ClientDto findById(Long id){
        Client result = clientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Erro ao encontrar o cliente"));
        return new ClientDto(result);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable){
        Page<Client> result = clientRepository.findAll(pageable);
        return result.map(c -> new ClientDto(c));
    }

    @Transactional
    public ClientDto insert(ClientDto clientDto){
        Client entity = new Client();

        entity.setName(clientDto.getName());
        entity.setCpf(clientDto.getCpf());
        entity.setBirthDate(clientDto.getBirthDate());
        entity.setIncome(clientDto.getIncome());
        entity.setChildren(clientDto.getChildren());
        entity = clientRepository.save(entity);

        return new ClientDto(entity);
    }

    @Transactional
    public ClientDto update(Long id,ClientDto clientDto){
        try {
            Client entidade = clientRepository.getReferenceById(id);
            entidade.setName(clientDto.getName());
            entidade.setCpf(clientDto.getCpf());
            entidade.setBirthDate(clientDto.getBirthDate());
            entidade.setIncome(clientDto.getIncome());
            entidade.setChildren(clientDto.getChildren());

            entidade = clientRepository.save(entidade);

            return new ClientDto(entidade);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Erro ao atualizar o cliente");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(@PathVariable Long id){

        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Erro ao excluir o cliente");
        }
        try{
            clientRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Erro ao excluir o cliente");
        }catch (DataIntegrityViolationException d){
            throw new DatabaseException("Falha na integridade referencial dos dados");
        }

    }
}

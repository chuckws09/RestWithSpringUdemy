package br.com.sgstudio.services;

import br.com.sgstudio.converter.DozerConverter;
import br.com.sgstudio.data.model.Person;
import br.com.sgstudio.data.vo.PersonVO;
import br.com.sgstudio.exception.ResourceNotFoundException;
import br.com.sgstudio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServices {

    @Autowired
    private PersonRepository personRepository;

    public PersonVO create(PersonVO person)
    {
        var entity = DozerConverter.parseObject(person, Person.class);
        return DozerConverter.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public Page<PersonVO> findAll(Pageable pageable)
    {
        var page = personRepository.findAll(pageable);

        return page.map(this::convertToPersonVO);
    }

    public Page<PersonVO> findPersonByName(String firstName, Pageable pageable)
    {
        var page = personRepository.findPersonByName(firstName, pageable);

        return page.map(this::convertToPersonVO);
    }

    private PersonVO convertToPersonVO(Person person) {
        return DozerConverter.parseObject(person, PersonVO.class);
    }

    public PersonVO findById(Long id)
    {
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
        return DozerConverter.parseObject(entity, PersonVO.class);
    }

    public PersonVO update(PersonVO person)
    {
        var entity = personRepository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerConverter.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public void delete(Long id)
    {
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
        personRepository.delete(person);
    }

}

package br.com.sgstudio.converter;


import br.com.sgstudio.converter.mocks.MockPerson;
import br.com.sgstudio.data.model.Person;
import br.com.sgstudio.data.vo.PersonVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DozerConverterTest {

    MockPerson inputObject;

    @Before
    public void setUp()
    {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToVOTest()
    {
        PersonVO output = DozerConverter.parseObject(inputObject.mockEntity(), PersonVO.class);
        Assert.assertEquals(Long.valueOf(0L), output.getKey());
        Assert.assertEquals("First name test 0", output.getFirstName());
        Assert.assertEquals("Last name test 0", output.getLastName());
        Assert.assertEquals("Address test 0", output.getAddress());
        Assert.assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityListToVOListTest()
    {
        List<PersonVO> outputList = DozerConverter.parseListObjects(inputObject.mockEntityList(), PersonVO.class);

        PersonVO output0 = outputList.get(0);

        Assert.assertEquals(Long.valueOf(0L), output0.getKey());
        Assert.assertEquals("First name test 0", output0.getFirstName());
        Assert.assertEquals("Last name test 0", output0.getLastName());
        Assert.assertEquals("Address test 0", output0.getAddress());
        Assert.assertEquals("Male", output0.getGender());

        PersonVO output6 = outputList.get(6);

        Assert.assertEquals(Long.valueOf(6L), output6.getKey());
        Assert.assertEquals("First name test 6", output6.getFirstName());
        Assert.assertEquals("Last name test 6", output6.getLastName());
        Assert.assertEquals("Address test 6", output6.getAddress());
        Assert.assertEquals("Male", output6.getGender());

        PersonVO output11 = outputList.get(11);

        Assert.assertEquals(Long.valueOf(11L), output11.getKey());
        Assert.assertEquals("First name test 11", output11.getFirstName());
        Assert.assertEquals("Last name test 11", output11.getLastName());
        Assert.assertEquals("Address test 11", output11.getAddress());
        Assert.assertEquals("Female", output11.getGender());
    }

    @Test
    public void parseVOToEntityTest()
    {
        Person output = DozerConverter.parseObject(inputObject.mockVO(), Person.class);
        Assert.assertEquals(Long.valueOf(0L), output.getId());
        Assert.assertEquals("First name test 0", output.getFirstName());
        Assert.assertEquals("Last name test 0", output.getLastName());
        Assert.assertEquals("Address test 0", output.getAddress());
        Assert.assertEquals("Male", output.getGender());
    }

    @Test
    public void parseVOListToEntityListTest()
    {
        List<Person> outputList = DozerConverter.parseListObjects(inputObject.mockVOList(), Person.class);

        Person output0 = outputList.get(0);

        Assert.assertEquals(Long.valueOf(0L), output0.getId());
        Assert.assertEquals("First name test 0", output0.getFirstName());
        Assert.assertEquals("Last name test 0", output0.getLastName());
        Assert.assertEquals("Address test 0", output0.getAddress());
        Assert.assertEquals("Male", output0.getGender());

        Person output6 = outputList.get(6);

        Assert.assertEquals(Long.valueOf(6L), output6.getId());
        Assert.assertEquals("First name test 6", output6.getFirstName());
        Assert.assertEquals("Last name test 6", output6.getLastName());
        Assert.assertEquals("Address test 6", output6.getAddress());
        Assert.assertEquals("Male", output6.getGender());

        Person output11 = outputList.get(11);

        Assert.assertEquals(Long.valueOf(11L), output11.getId());
        Assert.assertEquals("First name test 11", output11.getFirstName());
        Assert.assertEquals("Last name test 11", output11.getLastName());
        Assert.assertEquals("Address test 11", output11.getAddress());
        Assert.assertEquals("Female", output11.getGender());
    }
}
